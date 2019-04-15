package com.database;

import java.util.Collection;

public interface DAO <T>{
	boolean exists(String...arguments) throws CouponSystemException;
	
	void create(T t) throws CouponSystemException;

	T read(int id) throws CouponSystemException;

	void update(T t) throws CouponSystemException;

	void delete(int id) throws CouponSystemException;

	Collection<T> readAll() throws CouponSystemException;
//	void createCompany(Company company);
//
//	Company readCompany(Coupon coupon);
//
//	void updateCompany(Company company);
//
//	void deleteCompany(Company company);
//
//	void createCustomer(Customer customer);
//
//	Customer readCustomer(Customer customer);
//
//	void updateCustomer(Customer customer);
//
//	void deleteCustomer(Customer customer);



}
