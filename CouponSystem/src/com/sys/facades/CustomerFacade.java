package com.sys.facades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;

import com.sys.beans.Category;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;
import com.sys.exception.CustomerException;

// REVISED
/**
 * 
 * Facade consisting of actions available for a "Customer" type client of the coupon system.
 * @authors Yaniv Chen & Gil Gouetta
 *
 */
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

	/**
	 * Allows the customer to purchase a single coupon.<br>
	 * Updates all relevant tables in the DB.
	 * @param couponId - Integer representing the id for the purchased coupon.
	 * @throws CouponException
	 */
	public void purchaseCoupon(int couponId) throws CouponException {
		Coupon coupon = couponDao.read(couponId);
		if(coupon == null) {
			throw new CouponException("Coupon does not exist");
		}
		if (couponDao.exists(customer.getId(), couponId)) {
			throw new CouponException("Customer already purchased this coupon");
		}
		if (coupon.getAmount() == 0) {
			throw new CouponException("Coupon out of stock");
		}
		if (isToday(coupon.getEndDate())) {
			throw new CouponException("Coupon has expired");
		}
		couponDao.addPurchase(coupon.getId(),customer);
		
	}

	/**
	 * Returns a collection of all the coupons available in he system.
	 * @return a Collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllCoupons()throws CouponSystemException {
		return couponDao.readAll();
	}
	
	/**
	 * Returns all the coupons purchased by the customer.
	 * @return a Collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponSystemException
	 */
	public Collection<Coupon> getAllCouponsOfCustomer()throws CouponSystemException {
		return couponDao.readAll(customer);
	}

	/**
	 * Returns all the coupon purchased by the customer that are under the {@code maxPrice} limit.
	 * @param maxPrice - Double.
	 * @return a Collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
	public Collection<Coupon> getAllCouponsByMaxPrice(double maxPrice) throws CouponException {
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

	/**
	 * Returns all the coupon purchased by the customer that are of the {@code category}.
	 * @param category - a {link com.sys.beans.Category Category} ENUM value.
	 * @return a Collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CustomerException
	 */
	public Collection<Coupon> getAllCouponsByCategory(Category category) throws CustomerException {

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

	/**
	 * Return all the details for the customer from the DB.
	 * @return {@link com.sys.beans.Customer Customer} object
	 * @throws CustomerException
	 */
	public Customer getCustomerDetails() throws CustomerException {
			return customerDao.read(customer.getId());
	}

	/**
	 * Compares a date to today's date, returns true if the dates match.
	 * @param compareDate - A date object to compare to "today's date" (Taken from system clock).
	 * @return Boolean value: True - Coupon expire date is today, False - Coupon expire date is NOT today.
	 */
	private boolean isToday(LocalDate compareDate) { 
		// This is a mess. i should organize it TODO
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate today = LocalDate.now();
		return (formatter.format(today).equals(formatter.format((TemporalAccessor) compareDate)));
	}

}
