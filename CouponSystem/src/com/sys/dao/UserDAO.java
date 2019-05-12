package com.sys.dao;

import com.sys.exception.CouponSystemException;

/**
 * 
 * {@code UserDAO}
 * General DAO class for user objects, extends {@link com.sys.dao.DAO DAO}.
 * @authors Gil Gouetta & Yaniv Chen
 * @param <T> - Generic type object.
 * 
 */

public interface UserDAO<T> extends DAO<T> {
	/**
	 * Check if the object exists in the DB.
	 * @param email
	 * @param password
	 * @return a Boolean value (true/false).
	 * @throws CouponSystemException
	 */
	boolean exists(String email, String password) throws CouponSystemException;
	/**
	 * Returns the id for the object using the email.
	 * @param email
	 * @return an Integer value for customer/company id.
	 * @throws CouponSystemException
	 */
	int getIdByEmail(String email) throws CouponSystemException;
	
}
