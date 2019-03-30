package com.database;

import java.util.Collection;

public interface DbInteractor {
	void createCoupon(Coupon coupon);

	Coupon readCoupon(Coupon coupon);

	void updateCoupon(Coupon coupon);

	void deleteCoupon(Coupon coupon);

	void createCompany(Company company);

	Company readCompany(Coupon coupon);

	void updateCompany(Company company);

	void deleteCompany(Company company);

	void createCustomer(Customer customer);

	Customer readCustomer(Customer customer);

	void updateCustomer(Customer customer);

	void deleteCustomer(Customer customer);

	Collection<Coupon> readAll();

}
