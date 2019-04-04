package com.database;

import java.util.Collection;

public interface DAO <T>{
	boolean exists(String...arguments) throws CouponSystemException;
	
	void create(T t);

	T read(int id);

	void update(T t);

	void delete(int id);

	Collection<T> readAll();
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
