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
import java.time.Clock;
import java.time.LocalDate;

/**
 * DAO objects to handle Coupons table in the DB and {@link com.sys.beans.Coupon
 * Coupon} java bean.<br>
 * Implements the {@link com.sys.dao.ElementDAO ElementDAO} interface.
 * 
 * @authors Yaniv Chen & Gil Gouetta
 */

public class CouponDBDAO implements ElementDAO<Coupon> {
	private static String sqlCreate = "insert into coupons "
			+ "(company_id,category_id,title,description,start_date,end_date,amount,price,image) "
			+ "values (?,?,?,?,?,?,?,?,?)";

	private static String sqlRead = "select * from coupons where id = ?";

	private static String sqlUpdate = "update coupons set " + "title = ?, Start_date = ?, end_date = ?,"
			+ "amount = ? , description = ?," + "company_id = ? , category_id = ? ,"
			+ "price = ? ,image = ? where id = ?";

	private static String sqlDelete = "delete from coupons where id = ?";

	private static String sqlDeleteHistory = "delete from customers_vs_coupons where coupon_id = ?";

	private static String sqlReadExpiredCoupons = "select * from coupons where end_date < ?";
	
	private Connection connection;
	private Map<Integer, Category> categoryDictionary;
	private Map<Category, Integer> reverseCategoryDictionary;

	/**
	 * Generator for this DAO object, also used to establish the categoryDictionary
	 * and<br>
	 * reverseCategoryDictionary hash map objects.
	 * 
	 * @param categoryDao - a {@link com.sys.dao.CategoryDBDAO CtegoryDBDAO} object.
	 */
	public CouponDBDAO(CategoryDBDAO categoryDao) {
		super();
		this.categoryDictionary = categoryDao.allCategoriesById();
		reverseCategoryDictionary = new HashMap<Category, Integer>();
		for (int i = 0; i < categoryDictionary.size() - 1; i++) {
			reverseCategoryDictionary.put(categoryDictionary.get(i + 1), i + 1);
		}
	}

	@Override
	public void create(Coupon coupon) throws CouponException {

//		 "insert into coupons "
//					+ "(company_id,category_id,title,description,start_date,end_date,amount,price,image) " + "values (?,?,?,?,?,?,?,?,?)"

		connect();
		try (PreparedStatement create = connection.prepareStatement(sqlCreate)) {
			create.setInt(1, coupon.getCompanyId());
			create.setInt(2, reverseCategoryDictionary.get(coupon.getCategory()));
			create.setString(3, coupon.getTitle());
			create.setString(4, coupon.getDescription());
			create.setDate(5, Date.valueOf(coupon.getStartDate()));
			create.setDate(6, Date.valueOf(coupon.getEndDate()));
			create.setInt(7, coupon.getAmount());
			create.setDouble(8, coupon.getPrice());
			create.setString(9, coupon.getImage());

			create.execute();
		} catch (SQLException e) {
			throw new CouponException("error in creating coupon", e);
		} finally {
			disconnect();
		}
	}

	@Override
	public Coupon read(int id) throws CouponException {
		// ("select * from coupons where id = ?")
		Coupon result = null;
		connect();
		try (PreparedStatement read = connection.prepareStatement(sqlRead)) {
			read.setInt(1, id);
			ResultSet rs = read.executeQuery();
			if (rs.next()) {
				result = readFromActiveConnection(id, rs);
			}

		} catch (SQLException | CouponSystemException e) {
			throw new CouponException("error in reading coupon", e);
		} finally {
			disconnect();
		}
		return result;
	}

	/**
	 * Returns a {@link com.sys.beans.Coupon Coupon} object using a result set from
	 * a DB query, and a coupon id.
	 * 
	 * @param id - Integer
	 * @param rs - Result set
	 * @return a {@link com.sys.beans.Coupon Coupon} object.
	 * @throws SQLException
	 * @throws CouponSystemException
	 */
	private Coupon readFromActiveConnection(int id, ResultSet rs) throws SQLException, CouponSystemException {
		Coupon result;
		result = new Coupon();

		int amount = rs.getInt("amount");
		int companyId = rs.getInt("company_id");
		String title = rs.getString("title");
		String description = rs.getString("description");
		Category category = categoryDictionary.get(rs.getInt("category_id"));
		double price = rs.getDouble("price");
		LocalDate startDate = rs.getDate("start_date").toLocalDate();
		LocalDate endDate = rs.getDate("end_date").toLocalDate();
		String image = rs.getString("image");
		result.setCompanyId(companyId);
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
//				+ "amount = ? ,  description = ?,"  + "company_id = ? , category_id = ? ,"+ "price = ? ,image = ? where id = ?"

		connect();
		try (PreparedStatement update = connection.prepareStatement(sqlUpdate)) {
			update.setString(1, coupon.getTitle());
			update.setDate(2, Date.valueOf(coupon.getStartDate()));
			update.setDate(3, Date.valueOf(coupon.getEndDate()));
			update.setInt(4, coupon.getAmount());
			update.setString(5, coupon.getDescription());
			update.setInt(6, coupon.getCompanyId());
			update.setInt(7, coupon.getCategoryId());
			update.setDouble(8, coupon.getPrice());
			update.setString(9, coupon.getImage());
			update.setInt(10, coupon.getId());
			update.execute();
		} catch (SQLException e) {
			throw new CouponException("error in updating coupon " + coupon, e);
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
			throw new CouponException("error in deleting coupon", e);
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
			throw new CouponException("error in reading all coupons", e);
		} finally {
			disconnect();
		}
		return result;
	}

	/**
	 * Returns all coupons posted by a specific company.
	 * 
	 * @param company - a {@link com.sys.beans.Company Company} object.
	 * @return a collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
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
			throw new CouponException("Exception raised in reading all coupons", e);
		} finally {
			disconnect();
		}

		return result;
	}

	/**
	 * Returns all coupons purchased by a specific customer.
	 * 
	 * @param customer - a {@link com.sys.beans.Customer Customer} object.
	 * @return a collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
	public Collection<Coupon> readAll(Customer customer) throws CouponException {
		List<Coupon> result = new ArrayList<Coupon>();

		connect();
		String sql = "SELECT customers_vs_coupons.customer_id AS customer_id, coupons.* FROM customers_vs_coupons RIGHT JOIN coupons ON customers_vs_coupons.coupon_id = coupons.id"
				+ " WHERE customers_vs_coupons.customer_id = ?";
		try (PreparedStatement readFromCustomersVsCoupons = connection.prepareStatement(sql);) {
			readFromCustomersVsCoupons.setInt(1, customer.getId());
			ResultSet rs = readFromCustomersVsCoupons.executeQuery();
			while (rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("id"), rs));
			}
		} catch (SQLException | CouponSystemException e) {
			throw new CouponException("error in reading all coupons of customer", e);
		} finally {
			disconnect();
		}
		return result;
	}

	/**
	 * Receives a connection from the {@link com.sys.connection.ConnectionPool
	 * ConnectionPool}
	 * 
	 * @throws CouponException
	 */
	private synchronized void connect() throws CouponException {
		try {
			connection = ConnectionPool.getInstance().getConnection();
		} catch (ConnectionException e) {
			throw new CouponException("error in connecting", e);
		}
	}

	/**
	 * Returns the connection to the {@link com.sys.connection.ConnectionPool
	 * ConnectionPool}
	 * 
	 * @throws CouponException
	 */
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
		String preparedSql = "select * from customers_vs_coupons where customer_id = ? AND coupon_id = ?";
		try (PreparedStatement read = connection.prepareStatement(preparedSql)) {
			read.setInt(1, customerId);
			read.setInt(2, couponId);
			ResultSet rs = read.executeQuery();
			result = rs.next();

		} catch (SQLException e) {
			throw new CouponException("error in fetching coupon from customers_vs_coupons", e);
		} finally {
			disconnect();
		}
		return result;
	}

	@Override
	public void addPurchase(int couponId, Customer customer) throws CouponException {
		readAndIncrement(couponId, -1);
		changeCustomersVsCoupons(couponId, customer, true);
	}

	/**
	 * Makes the necessary changes in Customers_vs_Coupons table for a purchase or a
	 * refund/return.
	 * 
	 * @param couponId
	 * @param customer        a {@link com.sys.beans.Customer Customer} object.
	 * @param insertIntoTable a boolean parameter to define if the command shall be
	 *                        "insert into" or "delete"
	 * @throws CouponException
	 */
	private void changeCustomersVsCoupons(int couponId, Customer customer, boolean insertIntoTable)
			throws CouponException {
		String sql;
		if (insertIntoTable) {
			sql = "insert into customers_vs_coupons (coupon_id,customer_id) VALUES(?,?)";
		} else {
			sql = "delete from customers_vs_coupons WHERE coupon_id = ? AND customer_id = ?";
		}
		connect();
		try (PreparedStatement insert = connection.prepareStatement(sql)) {
			insert.setInt(1, couponId);
			insert.setInt(2, customer.getId());
			insert.execute();
		} catch (SQLException e) {
			throw new CouponException("error in inserting data to customers_vs_coupons", e);
		} finally {
			disconnect();
		}
	}

	@Override
	public void deletePurchase(int couponId, Customer customer) throws CouponException {
		readAndIncrement(couponId, 1);
		changeCustomersVsCoupons(couponId, customer, false);
	}

	/**
	 * Makes the appropriate increment in the {@code amount} field in the
	 * {@code Coupon} entry in the DB.<br>
	 * Works for both purchase (increment will be -1) and return (increment will be
	 * +1)
	 * 
	 * @param couponId
	 * @param increment
	 * @throws CouponException
	 */
	private void readAndIncrement(int couponId, int increment) throws CouponException {
		Coupon coupon = read(couponId);
		coupon.setAmount(coupon.getAmount() + increment);
		update(coupon);
	}

	@Override
	public void deleteCouponFromHistory(int couponId) throws CouponException {
		connect();
		try (PreparedStatement delete = connection.prepareStatement(sqlDeleteHistory)) {
			delete.setInt(1, couponId);
			delete.execute();
		} catch (SQLException e) {
			throw new CouponException("error in deleting purchase history of coupons", e);
		}
		finally {disconnect();}
	}

	public Collection<Coupon> readAllExpiredCoupons() throws CouponSystemException {
		List<Coupon> result = new ArrayList<Coupon>();
		connect();
		try {
			PreparedStatement read = connection.prepareStatement(sqlReadExpiredCoupons);
			LocalDate today = LocalDate.now(Clock.systemDefaultZone());
			read.setDate(1, Date.valueOf(today));
			ResultSet rs = read.executeQuery();
			while(rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("id"), rs));
			}
		} catch (SQLException e) {
			throw new CouponException("error in getting all expired coupons",e);
		}
		finally {disconnect();}
		
		return result;
	}
}