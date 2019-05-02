
package com.sys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sys.ConnectionPool;
import com.sys.beans.Customer;
import com.sys.exception.ConnectionException;
import com.sys.exception.CustomerException;

public class CustomerDBDAO implements UserDAO<Customer> {

	private Connection connection;

	private static String sqlCreate = "insert into customers (first_name,last_name,password,email) VALUES(?,?,?,?)";
	private static String sqlRead = "select * from customers where id = ?";
	private static String sqlUpdate = "update customers set first_name = ? , last_name = ? , password = ? , email = ? WHERE id = ?";
	private static String sqlDelete = "delete from customers where id = ?";
	private static String sqlDeleteCustomerHistory = "delete from cutomers_v_coupons where customer_id = ?";

	@Override
	public boolean exists(String email, String password) throws CustomerException {
		boolean result = false;
		connect();
//		String email = arguments[0];
//		String password = arguments[1];
		if (email.isEmpty() || password.isEmpty()) {
			throw new CustomerException("Password or Email were empty");
		}
		try (Statement stmt = connection.createStatement()) {
			String sql = "select * from customers where email = '" + email + "'" + " , password = '" + password + "'";
			ResultSet rs = stmt.executeQuery(sql);
			result = rs.next();
		} catch (SQLException e) {
			throw new CustomerException("error in checking if customer exists", e);
		}
		finally {
			disconnect();
		}

		return result;
	}

	@Override
	public void create(Customer customer) throws CustomerException {
		// "insert into customers (first_name,last_name,password,email) VALUES(?,?,?,?)"
		connect();
		try (PreparedStatement create = connection.prepareStatement(sqlCreate)) {
			create.setString(1, customer.getFirstName());
			create.setString(2, customer.getLastName());
			create.setString(3, customer.getPassword());
			create.setString(4, customer.getEmail());
			create.execute();
		} catch (SQLException e) {
			throw new CustomerException("error in creating customer", e);
		} finally {
			disconnect();
		}
	}

	@Override
	public Customer read(int id) throws CustomerException {
		// "select * from customers where id = ?"
		Customer result = null;
		try {
			connect();
			result = readFromConnection(id);
		} finally {
			disconnect();
		}
		return result;
	}

	@Override
	public void update(Customer customer) throws CustomerException {
		// "update customers set first_name = ?
		// , last_name = ? , password = ? , email = ? WHERE id = ?"
		connect();
		try (PreparedStatement update = connection.prepareStatement(sqlUpdate)) {
//			Customer customer = readFromConnection(id);
			update.setString(1, customer.getFirstName());
			update.setString(2, customer.getLastName());
			update.setString(3, customer.getPassword());
			update.setString(4, customer.getEmail());
			update.setInt(5, customer.getId());
			update.execute();
		} catch (SQLException e) {
			throw new CustomerException("error in updating customer", e);
		}
		finally {
			disconnect();
		}

	}

	@Override
	public void delete(int id) throws CustomerException {
		connect();
		deleteCustomerHistory(id);

		try (PreparedStatement delete = connection.prepareStatement(sqlDelete)) {
			delete.setInt(1, id);
			delete.execute();
		} catch (SQLException e) {
			throw new CustomerException("error in deleting customer", e);
		} finally {
			disconnect();
		}
	}

	@Override
	public Collection<Customer> readAll() throws CustomerException {
		List<Customer> result = new ArrayList<>();
		connect();
		String sqlReadAll = "select * from customers";
		try (Statement readAll = connection.createStatement()) {
			ResultSet rs = readAll.executeQuery(sqlReadAll);
			while (rs.next()) {
				result.add(createCustomer(rs.getInt("id"), rs));
			}

		} catch (SQLException e) {
			throw new CustomerException("error in reading all customers", e);
		} finally {
			disconnect();
		}
		return result;
	}

	private synchronized void connect() throws CustomerException {
		if (connection == null) {
			try {
				connection = ConnectionPool.getInstance().getConnection();
			} catch (ConnectionException e) {
				throw new CustomerException("error in connecting", e);
			}
		}
	}

	private synchronized void disconnect() throws CustomerException {
		try {
			ConnectionPool.getInstance().restoreConnection(connection);
		} catch (ConnectionException e) {
			throw new CustomerException("error in disconnecting", e);
		}
		connection = null;
	}

	private Customer readFromConnection(int id) throws CustomerException {
		Customer result = null;
		try (PreparedStatement read = connection.prepareStatement(sqlRead)) {
			read.setInt(1, id);
			ResultSet rs = read.executeQuery();
			if (rs.next()) {
				result = createCustomer(id, rs);
			}

		} catch (SQLException e) {
			throw new CustomerException("error in reading from connection", e);
		}
		return result;
	}

	private Customer createCustomer(int id, ResultSet rs) throws SQLException {
		Customer result;
		result = new Customer(id);
		result.setFirstName(rs.getString("first_name"));
		result.setLastName(rs.getString("last_name"));
		result.setEmail(rs.getString("email"));
		result.setPassword(rs.getString("password"));
		return result;
	}

	@Override
	public int getIdByEmail(String email) throws CustomerException {
		int id = -1;
		try (Statement stmt = connection.createStatement()) {
			String sql = "select * from customers where email = '" + email + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			throw new CustomerException("error in getting customer id by email", e);
		} finally {
			disconnect();
		}
		if (id == -1) {
			throw new CustomerException("email not found");
		}

		return id;
	}

	private void deleteCustomerHistory(int id) throws CustomerException {
		try (PreparedStatement delete = connection.prepareStatement(sqlDeleteCustomerHistory)) {
			delete.setInt(1, id);
			delete.execute();
		} catch (SQLException e) {
			throw new CustomerException("error in deleting history", e);
		}
	}
}
