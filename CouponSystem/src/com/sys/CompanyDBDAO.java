
package com.sys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.exception.CompanyException;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

public class CompanyDBDAO implements UserDAO<Company> {

//	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	private static String sqlCreate = "insert into companies (name,email,password) values(?,?,?)";
	private static String sqlRead = "select * from companies where id = ?";
	private static String sqlUpdate = "update companies set name = ?,password = ?,email = ? where id = ?";
	private static String sqlDelete = "delete * from companies where id = ?";

	private Connection connection;

	@Override
	public boolean exists(String email, String password) throws CouponSystemException {
		boolean result = false;
		connect();

		if (email.isEmpty() || password.isEmpty()) {
			throw new CouponSystemException("Password or Email were empty");
		}

		try (Statement stmt = connection.createStatement()) {
			String sql = "select * from companies where email = '" + email + "'" + " and password = '" + password + "'";
			ResultSet rs = stmt.executeQuery(sql);
			result = rs.next();
		} catch (SQLException e) {
			throw new CompanyException("error in checking existance of company",e);
		}
		finally {
			disconnect();
		}

		return result;
	}

	@Override
	public void create(Company company) throws CouponSystemException {
		// "insert into companies (name,email,password) values(?,?,?)"
		connect();
		try (PreparedStatement create = connection.prepareStatement(sqlCreate)) {

			create.setString(1, company.getName());
			create.setString(2, company.getEmail());
			create.setString(1, company.getPassword());
			create.execute();
		} catch (SQLException e) {
			throw new CompanyException("error in creating company",e,company);
		}
		finally {
			disconnect();
		}
	}

	@Override
	public Company read(int id) throws CouponSystemException {
		Company result = null;
		connect();
			result = readFromActiveConnection(id);
			disconnect();
		return result;
	}

	private Company readFromActiveConnection(int id, ResultSet rs) throws  CompanyException {
		Company result;
		result = new Company(id);
		try {
		result.setName(rs.getString("name"));
		result.setPassword(rs.getString("password"));
		result.setEmail(rs.getString("email"));
			result.setCoupons((List<Coupon>) new CouponDBDAO().readAll());
		} catch (SQLException e) {
			throw new CompanyException("error in reading company", e,result);
		} catch (CouponSystemException e) {
			throw new CompanyException("error in reading coupons of company",e,result);
		}
		return result;
	}

	@Override
	public void update(Company company) throws CouponSystemException {
//		"update companies set name = ?,password = ?,email = ? where id = ?"
		connect();
		try (PreparedStatement update = connection.prepareStatement(sqlUpdate)) {

//			Company company = readFromActiveConnection(company.getId());

			update.setString(1, company.getName());
			update.setString(2, company.getPassword());
			update.setString(3, company.getEmail());
			update.setInt(4, company.getId());
			update.execute();
			disconnect();
		} catch (SQLException e) {
			throw new CompanyException("error in updating company",e,company);
		}
	}

	@Override
	public void delete(int id) throws CouponSystemException {
		connect();
		try (PreparedStatement delete = connection.prepareStatement(sqlDelete)) {

			delete.setInt(1, id);
			delete.execute();
			disconnect();
		} catch (SQLException e) {
			throw new CompanyException("error in deleting company",e);
		}

	}

	@Override
	public Collection<Company> readAll() throws CouponSystemException {
		List<Company> result = new ArrayList<>();
		connect();

		try (Statement stmt = connection.createStatement()) {
			String sql = "select * from companies";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("id"), rs));
			}
			disconnect();
		} catch (SQLException e) {
			throw new CompanyException("error in reading all companies",e);
		}

		return result;

	}

	private synchronized void connect() throws CouponSystemException {
		if (connection == null) {
			connection = ConnectionPool.getInstance().getConnection();
		}
	}

	private synchronized void disconnect() throws CouponSystemException {
		ConnectionPool.getInstance().restoreConnection(connection);
		connection = null;
	}

	private Company readFromActiveConnection(int id) throws CompanyException {
		Company result = null;
		try (PreparedStatement read = connection.prepareStatement(sqlRead)) {

			read.setInt(1, id);
			ResultSet rs = read.executeQuery();
			if (rs.next()) {
				result = readFromActiveConnection(id, rs);
			}

		} catch (SQLException e) {
			throw new CompanyException("error in reading company",e,result);
		}
		return result;
	}

	@Override
	public int getIdByEmail(String email) throws CouponSystemException {
		int id = -1;
		try (Statement stmt = connection.createStatement()) {
			String sql = "select id from companies where email = '" + email + "'";
			ResultSet rs = stmt.executeQuery(sql);
			 if(rs.next()) {
				 id = rs.getInt("id");
			 }
			disconnect();
		} catch (SQLException e) {
			throw new CompanyException("error in getting company id",e);
		}
		if(id==-1) {
			throw new CompanyException("email not found");
		}
		
		return id;
	}

}
