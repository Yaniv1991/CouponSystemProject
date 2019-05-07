package com.sys.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sys.beans.Category;
import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.connection.ConnectionPool;
import com.sys.exception.ConnectionException;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CouponDBDAO implements ElementDAO<Coupon> {
	private static String sqlCreate = 		 "insert into coupons "
			+ "(company_id,category_id,title,description,start_date,end_date,amount,price,image) " + "values (?,?,?,?,?,?,?,?,?)";

	private static String sqlRead = "select * from coupons where id = ?";

	private static String sqlUpdate = "update coupons set " + "title = ?, Start_date = ?, end_date = ?,"
			+ "amount = ? , category = ? , description = ?,"  + "company_id = ? , category_id = ? ,"+ "price = ? ,image = ? where id = ?";

	private static String sqlDelete = "delete from coupons where id = ?";
	
	private static String sqlDeleteHistory = "delete from cutomers_v_coupons where coupon_id = ?";
	private static String sqlDeleteCustomerHistory = "delete from cutomers_v_coupons where customer_id = ?";

	
	private Connection connection;
	private Map<Integer,Category> categoryDictionary;
	private Map<Category,Integer> reverseCategoryDictionary;
	
	public CouponDBDAO(CategoryDBDAO categoryDao) {
		super();
		this.categoryDictionary = categoryDao.allCategoriesById();
		reverseCategoryDictionary = new HashMap<Category, Integer>();
		for(int i = 0;i<categoryDictionary.size()-1;i++) {
			reverseCategoryDictionary.put(categoryDictionary.get(i+1), i+1);
		}
	}

	@Override
	public void create(Coupon coupon) throws CouponException {
		
//		 "insert into coupons "
//					+ "(company_id,category_id,title,description,start_date,end_date,amount,price,image) " + "values (?,?,?,?,?,?,?,?,?)"
		
		connect();
		try (PreparedStatement create = connection.prepareStatement(sqlCreate)) {
			java.sql.Date startDate = (Date) coupon.getStartDate();
			java.sql.Date endDate = (Date) coupon.getEndDate();
			create.setInt(1, coupon.getCompanyId());
			create.setInt(2, coupon.getCategoryId());
			create.setString(3, coupon.getTitle());
			create.setString(4, coupon.getDescription());
			create.setDate(5, startDate);
			create.setDate(6, endDate);
			create.setInt(7, coupon.getAmount());
			create.setDouble(8, coupon.getPrice());
			create.setString(9, coupon.getImage());

			create.execute();
		} catch (SQLException e) {
			throw new CouponException("error in creating coupon",e);
		}
		finally {disconnect();}
	}

	@Override
	public Coupon read(int id) throws CouponException {
		// ("select * from ? where id = ?")
		Coupon result = null;
		connect();
		try (PreparedStatement read = connection.prepareStatement(sqlRead)) {
			read.setString(1, "coupon");
			read.setInt(2, id);
			ResultSet rs = read.executeQuery();
			if (rs.next()) {
				result = readFromActiveConnection(id, rs);
			}

		} catch (SQLException | CouponSystemException e) {
			throw new CouponException("error in reading coupon",e);
		}
		finally {disconnect();}
		return result;
	}

	private Coupon readFromActiveConnection(int id, ResultSet rs) throws SQLException, CouponSystemException {
		Coupon result;
		result = new Coupon();

		int amount = rs.getInt("amount");
		String title = rs.getString("title");
		String description = rs.getString("description");
		Category category = categoryDictionary.get(rs.getInt("category_id"));
		double price = rs.getDouble("price");
		Date startDate = rs.getDate("start_date");
		Date endDate = rs.getDate("end_date");
		String image = rs.getString("image");
		result.setAmount(amount);
		result.setCategory(category);
		result.setCategoryId(reverseCategoryDictionary.get(category));
		result.setEndDate(endDate);
		result.setStartDate(startDate);
		result.setId(id);
		result.setImage(image);
		result.setDescription(description);
		result.setPrice(price);
		result.setTitle(title);
		return result;
	}

	@Override
	public void update(Coupon coupon) throws CouponException {

//		"update coupons set " + "title = ?, Start_date = ?, end_date = ?,"
//				+ "amount = ? , category = ? , description = ?,"  + "company_id = ? , category_id = ? ,"+ "price = ? ,image = ? where id = ?"

		connect();
		try (PreparedStatement update = connection.prepareStatement(sqlUpdate)) {
			update.setString(1, coupon.getTitle());
			update.setDate(2, (Date) coupon.getStartDate());
			update.setDate(3, (Date) coupon.getEndDate());
			update.setInt(4, coupon.getAmount());
			update.setInt(5, coupon.getCategoryId());
			update.setString(6, coupon.getDescription());
			update.setInt(7, coupon.getCompanyId());
			update.setInt(8, coupon.getCategoryId());
			update.setDouble(9, coupon.getPrice());
			update.setString(10, coupon.getImage());
			update.setInt(11, coupon.getId());
			update.execute();
		} catch (SQLException e) {
			throw new CouponException("error in updating coupon " + coupon ,e);
		} finally {
			disconnect();
		}
	}

	@Override
	public void delete(int id) throws CouponException {
		connect();
		try (PreparedStatement delete = connection.prepareStatement(sqlDelete)) {
			delete.setInt(1, id);
			delete.execute();
		} catch (SQLException e) {
			throw new CouponException("error in deleting coupon",e);
		} finally {
			disconnect();
		}
	}

	@Override
	public Collection<Coupon> readAll() throws CouponException {
		List<Coupon> result = new ArrayList<>();
		connect();
		try (Statement readAll = connection.createStatement()) {
			String sql = "select * from coupons";
			ResultSet rs = readAll.executeQuery(sql);
			while (rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("id"), rs));
			}

		} catch (SQLException | CouponSystemException e) {
			throw new CouponException("error in reading all coupons",e);
		} finally {
			disconnect();
		}
		return result;
	}

	public Collection<Coupon> readAll(Company company) throws CouponException {
		List<Coupon> result = new ArrayList<>();
		connect();
		try (Statement readAll = connection.createStatement()) {
			String sql = "select * from coupons where company_id = " + company.getId();
			ResultSet rs = readAll.executeQuery(sql);
			while (rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("id"), rs));
			}

		} catch (SQLException | CouponSystemException e) {
			throw new CouponException("Exception raised in reading all coupons",e);
		} finally {
			disconnect();
		}

		return result;
	}

	public Collection<Coupon> readAll(Customer customer) throws CouponException{
		List<Coupon> result = new ArrayList<Coupon>();
		
		connect();
		String sql = "select * from customers_vs_coupons where customer_id = ?";
		try(PreparedStatement read = connection.prepareStatement(sql)){
			read.setInt(1, customer.getId());
			ResultSet rs = read.executeQuery();
			
			while(rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("coupon_id"),rs));
			}
		} catch (SQLException | CouponSystemException e) {
			throw new CouponException("error in reading all coupons of customer", e);
		}
		finally {
			disconnect();
		}
		return result;
	}

	private synchronized void connect() throws CouponException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (ConnectionException e) {
			throw new CouponException("error in connecting", e);
		}
	}

	private synchronized void disconnect() throws CouponException {
		try {
			ConnectionPool.getInstance().restoreConnection(connection);
		} catch (ConnectionException e) {
			throw new CouponException("error in disconnecting", e);
		}
		connection = null;
	}

	@Override
	public boolean exists(int customerId, int couponId) throws CouponException {
		boolean result = false;
		connect();
		String preparedSql = "select * from customers_vs_coupons where customer_id = ? , coupon_id = ?";
		try(PreparedStatement read = connection.prepareStatement(preparedSql)){
			read.setInt(1, customerId);
			read.setInt(2, couponId);
			ResultSet rs = read.executeQuery();
			result = rs.next();
			
		} catch (SQLException e) {
			throw new CouponException("error in fetching coupon from customers_vs_coupons",e);
		}
		finally {
			disconnect();
		}
		return result;
	}

	@Override
	public void addPurchase(int couponId) throws CouponException {
		readAndIncrement(couponId,-1);
	}

	@Override
	public void deletePurchase(int couponId) throws CouponException {
		readAndIncrement(couponId, 1);
		}
	
	private void readAndIncrement(int couponId,int increment) throws CouponException {
		Coupon coupon = read(couponId);
		coupon.setAmount(coupon.getAmount()+increment);
		update(coupon);
	}
	
	@Override
	public void deleteAllFromHistory(int couponId) throws CouponException {
		try (PreparedStatement delete = connection.prepareStatement(sqlDeleteHistory)) {
			delete.setInt(1, couponId);
			delete.execute();
		} catch (SQLException e) {
			throw new CouponException("error in deleting purchase history of coupons", e);
		}
	}
	
	//TODO remove purchases from the coupons so another can buy them
	public void deleteCouponsOfCustomer(int customerId) 
			throws CouponException{
		try (PreparedStatement delete = connection.prepareStatement(sqlDeleteCustomerHistory)) {
		delete.setInt(1, customerId);
		delete.execute();
	} catch (SQLException e) {
		throw new CouponException("error in deleting history of customer", e);
	}
	}
}