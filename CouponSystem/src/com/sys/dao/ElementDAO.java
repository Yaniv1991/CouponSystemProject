package com.sys.dao;

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
	void addPurchase(int couponId) throws CouponSystemException;
	/**
	 * {@code deletePurchase}</br></br>
	 * Removes a coupon from the customer entry in the DB.
	 * @param {@ code couponId} for the coupon returned/refunded.
	 * @throws CouponSystemException
	 */
	void deletePurchase(int couponId) throws CouponSystemException;
	/**
	 * {@code deleteCouponsOfCustomer}</br></br>
	 * Removes all coupon purchase history by the customer from the DB.
	 * @param {@code customerId}
	 * @throws CouponException
	 */
	void deleteCouponsOfCustomer(int customerId) throws CouponException;
	/**
	 * {@code deleteAllFromHistory}</br></br>
	 * Removes the purchase history for a single coupon by all customers.
	 * @param {@code couponId}
	 * @throws CouponException
	 */
	void deleteAllFromHistory(int couponId) throws CouponException;
}
