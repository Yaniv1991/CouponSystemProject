package com.sys.facades;

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

	private Customer customer;

	private CouponDBDAO couponDao;
	private CustomerDBDAO customerDao;

	public CustomerFacade(int id,CouponDBDAO couponDao,CustomerDBDAO customerDao) {
		customer = new Customer(id);
		this.couponDao=couponDao;
		this.customerDao=customerDao;
	}

	@Override
	boolean login(String email, String password) throws CustomerException {
		return (customerDao.exists(email, password));
	}

	public void purchaseCoupon(int couponId) throws CouponException {
		Coupon coupon = couponDao.read(couponId);
		if (couponDao.exists(customer.getId(), coupon.getId())) {
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

	public Collection<Coupon> getAllCopouns()throws CouponSystemException {
		return couponDao.readAll();
	}
	
	public Collection<Coupon> getAllCopounsOfCustomer()throws CouponSystemException {
		return couponDao.readAll(customer);
	}

	public Collection<Coupon> getAllCopounsByMaxPrice(double maxPrice) throws CouponException {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Collection<Coupon> allCoupons = new ArrayList<Coupon>();
		
		allCoupons = couponDao.readAll(customer);
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
		try {
			allCoupons = couponDao.readAll(customer);
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

	public Customer getCustomerDetails() throws CustomerException {
			return customerDao.read(customer.getId());
	}

	private boolean isToday(Date couponDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate today = LocalDate.now();
		return (formatter.format(today).equals(formatter.format((TemporalAccessor) couponDate)));
	}

}
