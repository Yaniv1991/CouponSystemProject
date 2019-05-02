package com.clients;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.sys.beans.Category;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;
import com.sys.exception.CustomerException;

//revised
public class CustomerFacade extends ClientFacade {

	private int id;

	public CustomerFacade(int id) {
		this.id = id;
	}

	@Override
	boolean login(String email, String password) throws CustomerException {
		return (new CustomerDBDAO().exists(email, password));
	}

	public void purchaseCoupon(int couponId) throws CouponException {
		CouponDBDAO couponDao = new CouponDBDAO();
		Coupon coupon = couponDao.read(couponId);
		if (couponDao.exists(id, coupon.getId())) {
			throw new CouponException("Customer already purchased this coupon");
		}
		if (coupon.getAmount() == 0) {
			throw new CouponException("Coupon out of stock");
		}
		if (isToday(coupon.getEndDate())) {
			throw new CouponException("Coupon has expired");
		}
		couponDao.addPurchase(coupon.getId());
	}

	public Collection<Coupon> getAllCopouns(int customerId) throws CouponSystemException {
		CouponDBDAO couponDao = new CouponDBDAO();
		return couponDao.readAll(new Customer(id));
	}

	public Collection<Coupon> getAllCopounsByMaxPrice(double maxPrice) throws CouponException {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Collection<Coupon> allCoupons = new ArrayList<Coupon>();
		CouponDBDAO couponDao = new CouponDBDAO();
		allCoupons = couponDao.readAll(new Customer(id));
		for (Coupon coupon : allCoupons) {
			if (coupon.getPrice() <= maxPrice) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	public Collection<Coupon> getAllCopounsByCategory(Category category) throws CustomerException {

		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Collection<Coupon> allCoupons = new ArrayList<Coupon>();
		CouponDBDAO couponDao = new CouponDBDAO();
		CustomerDBDAO customerDao = new CustomerDBDAO();
		try {
			allCoupons = couponDao.readAll(customerDao.read(id));
			for (Coupon coupon : allCoupons) {
				if (coupon.getCategory().equals(category)) {
					coupons.add(coupon);
				}
			}
		} catch (CouponException e) {
			throw new CustomerException("error in getting all coupons by category", e);
		}
		return coupons;
	}

	public Customer getCustomerDetails(String email) throws CustomerException {
		CustomerDBDAO customerDao = new CustomerDBDAO();
			return customerDao.read(customerDao.getIdByEmail(email));
	}

	private boolean isToday(Date couponDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate today = LocalDate.now();
		return (formatter.format(today).equals(formatter.format((TemporalAccessor) couponDate)));
	}

}
