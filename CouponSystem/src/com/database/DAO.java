package com.database;

import java.util.Collection;

import com.database.exception.CouponSystemException;

public interface DAO <T>{
	
	void create(T t) throws CouponSystemException;

	T read(int id) throws CouponSystemException;

	void update(T t) throws CouponSystemException;

	void delete(int id) throws CouponSystemException;

	Collection<T> readAll() throws CouponSystemException;



}
