
package com.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompanyDBDAO implements DAO<Company> {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	private static String sqlCreate = "insert into companies (name,email,password) values(?,?,?)";
	private static String sqlRead = "select * from companies where id = ?";
	private static String sqlUpdate = "update companies set name = ?,password = ?,email = ? where id = ?";
	private static String sqlDelete = "delete * from companies where id = ?";

	private Connection connection;

	@Override
	public boolean exists(String email, String password) {
		boolean result = false;
			connect();

			try (Statement stmt = connection.createStatement()) {
				String sql = "select * from companies where email = '" + email + "'" + " and password = '" + password
						+ "'";
				ResultSet rs = stmt.executeQuery(sql);
				result = rs.next();
				disconnect();
			} catch (SQLException e) {
				DbExceptionHandler.HandleException(e);
			}
		
		return result;
	}

	
	@Override
	public void create(Company company) {
		// "insert into companies (name,email,password) values(?,?,?)"
		connect();
		try (PreparedStatement create = connection.prepareStatement(sqlCreate)) {

			create.setString(1, company.getName());
			create.setString(2, company.getEmail());
			create.setString(1, company.getPassword());
			create.execute();
			disconnect();

		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}
	}

	@Override
	public Company read(int id) {
		Company result = null;
		connect();
		try {
		result = readFromActiveConnection(id);
			disconnect();
		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}
		return result;
	}


	private Company createCompany(int id, ResultSet rs) throws SQLException {
		Company result;
		result = new Company(id);
		result.setName(rs.getString("name"));
		result.setPassword(rs.getString("password"));
		result.setEmail(rs.getString("email"));
		result.setCoupons((List<Coupon>) new CouponDAO().readAll());
		return result;
	}

	@Override
	public void update(Company company) {
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
			DbExceptionHandler.HandleException(e);
		}
	}

	@Override
	public void delete(int id) {
		connect();
		try (PreparedStatement delete = connection.prepareStatement(sqlDelete)) {

			delete.setInt(1, id);
			delete.execute();
			disconnect();
		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}

	}

	@Override
	public Collection<Company> readAll() {
		List<Company> result = new ArrayList<>();
		connect();

		try (Statement stmt = connection.createStatement()) {
			String sql = "select * from companies";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result.add(createCompany(rs.getInt("id"), rs));
			}
			disconnect();
		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}

		return result;

	}
	
	
	private synchronized void connect() {
		if (connection == null) {
			connection = connectionPool.getConnection();
		}
	}

	private synchronized void disconnect() throws SQLException {
		connectionPool.restoreConnection(connection);
		connection = null;
	}

	
	private Company readFromActiveConnection(int id) {
		Company result = null;
		try (PreparedStatement read = connection.prepareStatement(sqlRead)) {

			read.setInt(1, id);
			ResultSet rs = read.executeQuery();
			if (rs.next()) {
				result = createCompany(id, rs);
			}
			
		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}
		return result;
	}

}
