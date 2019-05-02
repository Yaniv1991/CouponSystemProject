package com.clients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.sys.ConnectionPool;
import com.sys.DbExceptionHandler;
import com.sys.beans.Category;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.exception.ConnectionException;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;
import com.sys.exception.CustomerException;
//revised
public class CustomerFacade extends ClientFacade{

	private Connection connection;
	String email,password;
	@Override
	boolean login(String email, String password) {
		//TODO Complete or Remove this method.
		boolean login = false;
		return login;
	}

	//Constructor
	public CustomerFacade(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public void purchaseCoupon(int couponId) throws ConnectionException {

		Coupon coupon = new Coupon(couponId);
		Date today = new Date();
		CouponDBDAO coupondao = new CouponDBDAO();
		CustomerDBDAO customerdao = new CustomerDBDAO();
		int customerId=0;
		try {
			customerId = customerdao.getIdByEmail(email);
		} catch (CouponSystemException e) {
			DbExceptionHandler.HandleException(e);
		}
		try {
			coupon = coupondao.read(couponId);
		} catch (CouponSystemException e) {
			DbExceptionHandler.HandleException(e);
		}
		try {
			connect();
			try (PreparedStatement checkCoupon = connection.prepareStatement("SELECT * FROM customers_v_coupons WHERE customer_id = ? AND coupon_id = ?")) {
				checkCoupon.setInt(1, customerId);
				checkCoupon.setInt(2, coupon.getId());
				ResultSet exists = checkCoupon.executeQuery();
				if (exists.next()) {
					throw new CouponSystemException("You have already purchased this coupon");
				} else if (coupon.getAmount()==0) {
					throw new CouponSystemException("Coupon out of stock");
				} else if (isToday(coupon.getEndDate())) {
					throw new CouponSystemException("Coupon expired");
				} else {
					try (PreparedStatement purchasedCoupon = connection.prepareStatement("INSET INTO customers_v_coupons (customer_id,coupon_id) VALUE(??)")) {
						purchasedCoupon.setInt(1, customerId);
						purchasedCoupon.setInt(2, coupon.getId());
						purchasedCoupon.executeUpdate();
						coupon.setAmount(coupon.getAmount()-1);
						coupondao.update(coupon);
					} catch (SQLException e) {
						throw new CouponSystemException("Coupon expired"); //TODO Revise massage
					}
				}} catch (SQLException e) {
					throw new CouponSystemException("Coupon expired"); //TODO Revise massage
				}
		} catch (CouponSystemException e) {
			DbExceptionHandler.HandleException(e);
		} finally {
			disconnect();
		}

	}
	
	public Collection<Coupon> getAllCopouns (int customerId) throws CouponSystemException{
		
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		CouponDBDAO couponDao = new CouponDBDAO();
		CustomerDBDAO customerDao = new CustomerDBDAO();
		coupons = couponDao.readAll(customerDao.read(customerId));
		return coupons;
	}
	
	public Collection<Coupon> getAllCopounsByMaxPrice (int customerId, double maxPrice) throws CouponException {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Collection<Coupon> temp = new ArrayList<Coupon>();
		CouponDBDAO couponDao = new CouponDBDAO();
		CustomerDBDAO customerDao = new CustomerDBDAO();
		temp = couponDao.readAll();
		for (Coupon coupon : temp) {
			if (coupon.getPrice()<=maxPrice) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}
	
	public Collection<Coupon> getAllCopounsByCategory (int customerId, Category category) throws CustomerException{
		
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Collection<Coupon> temp = new ArrayList<Coupon>();
		CouponDBDAO couponDao = new CouponDBDAO();
		CustomerDBDAO customerDao = new CustomerDBDAO();
		try {
			temp = couponDao.readAll(customerDao.read(customerId));
		} catch (CouponException e) {
			throw new CustomerException("error in getting all coupons by category",e);
		}
		for (Coupon coupon : temp) {
			if (coupon.getCategory()== category) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}
	
	public Customer getCustomerDetails (String email) {
		
		CustomerDBDAO customerDao = new CustomerDBDAO();
		Customer result = new Customer();
		try {
			result = customerDao.read(customerDao.getIdByEmail(email));
		} catch (CouponSystemException e) {
			// TODO Add exception handle
			e.printStackTrace();
		}
		return result;
	}
	
	private boolean isToday (Date couponDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
		LocalDate today = LocalDate.now(); 
		return (formatter.format(today)==formatter.format((TemporalAccessor) couponDate));
	}
	
	private synchronized void connect() throws CouponSystemException {
		connection = ConnectionPool.getInstance().getConnection();
	}

	private synchronized void disconnect() throws ConnectionException {
		ConnectionPool.getInstance().restoreConnection(connection);
		connection = null;
	}

}
