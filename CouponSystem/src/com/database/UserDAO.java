package com.database;

import com.database.exception.CouponSystemException;

public interface UserDAO<T> extends DAO<T> {
	boolean exists(String email, String password) throws CouponSystemException;
	int getIdByEmail(String email) throws CouponSystemException;
}
