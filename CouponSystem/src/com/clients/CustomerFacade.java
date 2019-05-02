package com.clients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import com.sys.*;
import com.sys.beans.Coupon;
import com.sys.exception.CouponSystemException;
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

	public void purchaseCoupon(int couponId) {

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
				} else if (isSameDay(coupon.getEndDate())) {
					throw new CouponSystemException("Coupon expired");
				} else {
					try (PreparedStatement purchasedCoupon = connection.prepareStatement("INSET INTO customers_v_coupons (customer_id,coupon_id) VALUE(??)")) {
						purchasedCoupon.setInt(1, customerId);
						purchasedCoupon.setInt(2, coupon.getId());
						purchasedCoupon.executeUpdate();
						coupon.setAmount(coupon.getAmount()-1);
						coupondao.update(coupon);
					} catch (SQLException e) {
						DbExceptionHandler.HandleException(e);
					}
				}} catch (SQLException e) {
					DbExceptionHandler.HandleException(e);
				}
		} catch (CouponSystemException e) {
			DbExceptionHandler.HandleException(e);
		} finally {
			disconnect();
		}

	}
	
	private boolean isSameDay (Date couponDate) {
		
		
		return false;
	}
	
	private synchronized void connect() throws CouponSystemException {
		connection = ConnectionPool.getInstance().getConnection();
	}

	private synchronized void disconnect() {
		ConnectionPool.getInstance().restoreConnection(connection);
		connection = null;
	}

}
