package com.sys.facades;

import com.sys.exception.CouponSystemException;

/**
 * Abstract class for the different facade classes.
 * @authors Gil Gouetta & Yaniv Chen
 *
 */

public abstract class ClientFacade {
	
	/**
	 * Simple login method to the coupon system.
	 * @param email - String, object email.
	 * @param password - String, object password.
	 * @return a Boolean object - True = login successful, False = login failed.
	 * @throws CouponSystemException
	 */
	abstract boolean login (String email, String password) throws CouponSystemException;

}
