package com.database;

import java.util.Collection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CouponDAO implements DbInteractor {

	private String url;
	private PreparedStatement createCustomer, updateCustomer, createCompany, updateCompany, createCoupon, updateCoupon,
			read, delete;

	@Override
	public void createCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		// ("insert into coupon "
		// + "(id,title,start_date,end_date,amount,type,message,price,image) "
		// + "values (?,?,?,?,?,?,?,?,?)");
		try (Connection con = DriverManager.getConnection(url)) {
//			java.sql.Date startDate = (Date) coupon.getStartDate();
//			java.sql.Date endDate = (Date) coupon.getEndDate();
//			createCoupon.setLong(1, coupon.getId());
//			createCoupon.setString(2, coupon.getTitle());
//			createCoupon.setDate(3, startDate);
//			createCoupon.setDate(4, endDate);
//			createCoupon.setInt(5, coupon.getAmount());
//			createCoupon.setString(6, coupon.getCouponType().toString());
//			createCoupon.setString(7, coupon.getMessage());
//			createCoupon.setDouble(8, coupon.getPrice());
//			createCoupon.setString(9, coupon.getImage());

			transactUpdates(con, coupon.create());
		} catch (SQLException e) {
			System.out.println("Inaal Rabak");
		}
	}

	@Override
	public Coupon readCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		// ("select * from ? where id = ?")
		Coupon result = null;
		try (Connection con = DriverManager.getConnection(url)) {
			read.setString(1, "coupon");
			read.setLong(2, coupon.getId());
			ResultSet rs = read.executeQuery();
			if (rs.next()) {
				result = new Coupon();

				int amount = rs.getInt("amount");
				long id = rs.getLong("id");
				String title = rs.getString("title");
				String message = rs.getString("message");
				CouponType type = CouponType.valueOf(rs.getString("type"));
				double price = rs.getDouble("price");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				String image = rs.getString("image");
				result.setAmount(amount);
				result.setCouponType(type);
				result.setEndDate(endDate);
				result.setStartDate(startDate);
				result.setId(id);
				result.setImage(image);
				result.setMessage(message);
				result.setPrice(price);
				result.setTitle(title);
			}

//			transactUpdates(con, read);
		} catch (SQLException e) {
			System.out.println("Inaal Rabak");
		}
		return result;
	}

	@Override
	public void updateCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
//		("update coupon set "
//				+ "title = ?, Start_date = ?, end_date = ?,"
//				+ "amount = ? , type = ? , message = ?,"
//				+ "price = ? ,image = ? where id = ?")
		try (Connection con = DriverManager.getConnection(url)) {
//			java.sql.Date startDate = (Date)coupon.getStartDate(); 
//			java.sql.Date endDate = (Date)coupon.getEndDate(); 
//			updateCustomer.setString(1, coupon.getTitle());
//			updateCustomer.setDate(2, startDate);
//			updateCustomer.setDate(3, endDate);
//			updateCustomer.setLong(4, coupon.getAmount());
//			updateCustomer.setString(5, coupon.getCouponType().toString());
//			updateCustomer.setString(6, coupon.getMessage());
//			updateCustomer.setDouble(7, coupon.getPrice());
//			updateCustomer.setString(8, coupon.getImage());
//			updateCustomer.setLong(9, coupon.getId());
//			
//			transactUpdates(con, updateCustomer);
			transactUpdates(con,coupon.update());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deleteCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		// ("delete from ? where id = ?")
		try (Connection con = DriverManager.getConnection(url)) {
//			read.execute(coupon.delete());
//			delete.setString(1, "coupon");
//			delete.setLong(2, coupon.getId());
			
			transactUpdates(con, coupon.delete());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createCompany(Company company) {
		// TODO Auto-generated method stub
		// ("insert into company (id,comp_name,password,email) values(?,?,?,?)");
// insert into company (?) values(?)
		// ?1 = "id,comp_name....
		// ?2 = <values>
	}

	@Override
	public Company readCompany(Coupon coupon) {
		// TODO Auto-generated method stub
		// ("select * from ? where id = ?")
		return null;
	}

	@Override
	public void updateCompany(Company company) {
		// TODO Auto-generated method stub
		// ("update company set"
//		+ " comp_name = ?,password = ?,email = ? where id = ?");

	}

	@Override
	public void deleteCompany(Company company) {
		// TODO Auto-generated method stub
		// ("delete from ? where id = ?")
	}

	@Override
	public void createCustomer(Customer customer) {
		// TODO Auto-generated method stub
		// ("insert into customer (id,CUST_NAME,PASSWORD) values(?,?,?)");

	}

	@Override
	public Customer readCustomer(Customer customer) {
		// TODO Auto-generated method stub
		// ("select * from ? where id = ?")
		return null;
	}

	@Override
	public void updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		// ("update customer set"
//		+ " CUST_NAME = ?, PASSWORD = ? where id= ?");
	}

	@Override
	public void deleteCustomer(Customer customer) {
		// TODO Auto-generated method stub
		// ("delete from ? where id = ?")
	}

	@Override
	public Collection<Coupon> readAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public CouponDAO(String url) {
		super();
		this.url = url;
		generatePreparedStatements(url);
	}

	private void generatePreparedStatements(String url) {
		try (Connection con = DriverManager.getConnection(url)) {
			createCustomer = con.prepareStatement("insert into customer (id,CUST_NAME,PASSWORD) values(?,?,?)");
			createCompany = con.prepareStatement("insert into company (id,comp_name,password,email) values(?,?,?,?)");
			createCoupon = con.prepareStatement("insert into coupon "
					+ "(id,title,start_date,end_date,amount,type,message,price,image) " + "values (?,?,?,?,?,?,?,?,?)");
			updateCustomer = con.prepareStatement("update customer set" + " CUST_NAME = ?, PASSWORD = ? where id= ?");
			updateCompany = con
					.prepareStatement("update company set" + " comp_name = ?,password = ?,email = ? where id = ?");
			updateCoupon = con.prepareStatement("update coupon set " + "title = ?, Start_date = ?, end_date = ?,"
					+ "amount = ? , type = ? , message = ?," + "price = ? ,image = ? where id = ?");

			read = con.prepareStatement("select * from ? where id = ?");
			delete = con.prepareStatement("delete from ? where id = ?");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void transactUpdates(Connection con, PreparedStatement... statement) {
		try {
			con.setAutoCommit(false);
			for (PreparedStatement preparedStatement : statement) {
				preparedStatement.execute();
			}
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error in commiting");
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private static void transactUpdates(Connection con, String... statements) {
		try (Statement stmt = con.createStatement()){
			con.setAutoCommit(false);
			for (String sql : statements) {
				stmt.execute(sql);
			}
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error in commiting");
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
