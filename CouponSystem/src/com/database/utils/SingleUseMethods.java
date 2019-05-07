package com.database.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.sys.beans.Category;
import com.sys.connection.ConnectionPool;
import com.sys.dao.CategoryDBDAO;
import com.sys.exception.ConnectionException;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

public class SingleUseMethods {

	private static Connection connection;
	private static String url = "jdbc:derby://localhost:1527/CouponSystemDb;create = true";

	public static void main(String[] args) {
		try {
			createTables();
			createCategories();
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void createTables() throws CouponSystemException {
		try {
			connect();
			connection.setAutoCommit(false);
			createCustomerTable();
			createCompanyTable();
			createCategoryTable();
			createCouponTable();
			createCustomersVsCouponsTable();
			connection.commit();
			System.out.println("all done");
		} catch (CouponException | SQLException e) {
			try {
				connection.rollback();
				System.out.println("error in committing");

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	private static void createCompanyTable() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "create table companies (id integer generated always as identity(start with 1, increment by 1),"
				+ "name varchar(20),email varchar(30) unique not null,password varchar(20) not null,primary key(id))";
		try (Statement create = connection.createStatement()) {
			create.execute(sql);
		}
	}

	private static void createCategoryTable() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "create table categories (id integer generated always as identity(start with 1, increment by 1),"
				+ "name varchar(20) unique not null,primary key(id))";
		try (Statement create = connection.createStatement()) {
			create.execute(sql);
		}
	}

	private static void createCustomerTable() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "create table customers (id integer generated always as identity(start with 1, increment by 1),"
				+ "first_name varchar(20) not null,last_name varchar(20),email varchar(30) unique not null,password varchar(20) not null,primary key(id))";
		try (Statement create = connection.createStatement()) {
			create.execute(sql);
		}
	}

	private static void createCouponTable() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "create table coupons(id integer GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)"
				+ ",company_id INTEGER,category_id INTEGER,title VARCHAR(20),description VARCHAR(20),"
				+ "start_date DATE,end_date DATE,amount INTEGER NOT NULL, price INTEGER NOT NULL,"
				+ "image VARCHAR(20),PRIMARY KEY(id),"
				+ "FOREIGN KEY(company_id) REFERENCES companies(id),FOREIGN KEY(category_id) REFERENCES categories(id))";
		try (Statement createCouponTable = connection.createStatement();) {
			createCouponTable.execute(sql);
		}
	}

	private static void createCustomersVsCouponsTable() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "create table customers_vs_coupons (customer_id integer ,"
				+ "coupon_id integer, foreign key (customer_id) references customers(id), foreign key (coupon_id) references coupons(id))";
		try (Statement create = connection.createStatement()) {
			create.execute(sql);
		}
	}

	private static void createCategories() throws CouponSystemException {
		CategoryDBDAO categoryDao = new CategoryDBDAO();
//		if (categoryDao.readAll().size() == 0) {
			for (Category category : Category.values()) {
				{
					categoryDao.create(category);
				}
			}
//		}
		System.out.println("finished creating categories");
//		Connection con = ConnectionPool.getInstance().getConnection();
//		String sql = "insert into categories(name) values(?)";
//		try {
//			PreparedStatement insert = con.prepareStatement(sql);
//			for (Category category : Category.values()) {
//				insert.setString(1, category.toString());
//				insert.execute();
//				System.out.println("inserted " + category + " to table");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			ConnectionPool.getInstance().restoreConnection(con);
//		}
	}

	private static synchronized void connect() throws CouponException {
		try {
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new CouponException("error in connecting", e);
		}
	}

	private static void disconnect() throws CouponException {
		try {
			ConnectionPool.getInstance().restoreConnection(connection);
		} catch (ConnectionException e) {
			throw new CouponException("error in disconnecting", e);
		}
		connection = null;
	}
}