package com.sys.dao;

import java.util.Collection;

import com.sys.exception.CouponSystemException;

/**
 * 
 * * DAO general interface.
 * @authors Gil Gouetta & Yaniv Chen
 * @param <T> - Generic type object.
 */

public interface DAO <T>{
	
	/**
<<<<<<< HEAD
	* {@code create}</br></br>
	* Method used to write data into the DB.
	* @param Generic type object.
	* @throws CouponSystemException
	*/

	void create(T t) throws CouponSystemException;

	/**
	 * {@code read}</br></br>
	 * Method used to read data from the DB.
	 * @param {{@code id} for the object (Coupon,Company or Customer) to look for in the DB.
	 * @throws CouponSystemException
	 */
	T read(int id) throws CouponSystemException;

	/**
	 * {@code update}</br></br>
	 * Used to update entries in the DB.
	 * @param Generic type object.
	 * @throws CouponSystemException
	 */
	void update(T t) throws CouponSystemException;

	/**
	 * {@code delete}</br></br>
	 * Used to remove entries from the DB.
	 * @param {@code id} for the object (Coupon,Company or Customer) to look for in the DB.
	 * @throws CouponSystemException
	 */
	void delete(int id) throws CouponSystemException;

	/**
	* {@code readAll}</br></br>
	* Used to fetch a collection of objects (Coupon, Company or Customer) from the DB.
	* @return a Collection of generic type objects.
	* @throws CouponSystemException
	*/

	Collection<T> readAll() throws CouponSystemException;

}
