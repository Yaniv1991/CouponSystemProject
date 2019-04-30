package com.database;

import com.database.exception.CouponSystemException;

public interface ElementDAO<T> extends DAO<T> {
	boolean exists(int customerId, int companyId) throws CouponSystemException;
	void addPurchase(int customerId, int companyId) throws CouponSystemException;
	void deletePurchase(int customerId, int companyId) throws CouponSystemException;
}
