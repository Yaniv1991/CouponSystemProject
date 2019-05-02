package com.sys.dao;

import com.sys.exception.CouponSystemException;

public interface UserDAO<T> extends DAO<T> {
	boolean exists(String email, String password) throws CouponSystemException;
	int getIdByEmail(String email) throws CouponSystemException;
}
