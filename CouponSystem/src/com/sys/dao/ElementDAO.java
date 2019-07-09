package com.sys.dao;

import java.util.Collection;

import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

/**
 * 
 * General DAO class for element (Non-User) objects.
 * @authors Gil Gouetta & Yaniv Chen.
 *
 * @param General object type.
 */
public interface ElementDAO<T> extends DAO<T> {
	/**
	* {@code exists}</br></br>
	* Checks if the object exists in the DB.
	* @param {@code customerId}
	* @param {@code couponId}
	* @return a {@code boolean} value - True if the object exists in the DB, False otherwise.
	* @throws CouponSystemException
	*/
	boolean exists(int customerId, int couponId) throws CouponSystemException;
	/**
	* {@code addPurchase}</br></br>
	* Adds a Coupon purchase to the customer entry in the DB.
	* @param {{@code couponId} for the purchased coupon.
	* @throws CouponSystemException
	*/
	void addPurchase(int couponId,Customer customer) throws CouponSystemException;
	/**
	* {@code deletePurchase}</br></br>
	* Removes a coupon from the customer entry in the DB.
	* @param {@ code couponId} for the coupon returned/refunded.
	* @throws CouponSystemException
	*/
	void deletePurchase(int couponId,Customer customer) throws CouponSystemException;

	/**
	* {@code deleteAllFromHistory}</br></br>
	* Removes the purchase history for a single coupon by all customers.
	* @param {@code couponId}
	* @throws CouponException
	*/
	void deleteCouponFromHistory(int couponId) throws CouponException;
	
	/**
	 * Returns all coupons posted by a specific company.
	 * 
	 * @param company - a {@link com.sys.beans.Company Company} object.
	 * @return a collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
	Collection<Coupon> readAll(Company company) throws CouponException;
	
	/**
	 * Returns all coupons purchased by a specific customer.
	 * 
	 * @param customer - a {@link com.sys.beans.Customer Customer} object.
	 * @return a collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
	 Collection<Coupon> readAll(Customer customer) throws CouponException;
	 
	 /**
	  * 
	  * Returns all coupons which have their expiration date passed.
	  * 
	  * @return A collection of {@link com.sys.beans.Coupon Coupon} objects.
	  * @throws CouponSystemException
	  */
	 Collection<Coupon> readAllExpiredCoupons() throws CouponSystemException; 
}
