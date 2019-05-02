package com.clients;

import java.util.ArrayList;
import java.util.List;
import com.database.*;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;
import com.sys.beans.Category;
import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.exception.CouponSystemException;

public class CompanyFacade extends ClientFacade{

	@Override
	public boolean login(String email, String password) {
		//TODO Complete or Remove this method.
		boolean login = false;
		CompanyDBDAO companydao = new CompanyDBDAO();
		try {
			login = companydao.exists(email, password);	
		} catch (CouponSystemException e) {
			// TODO Add exception handle!	
			e.printStackTrace();
		}
		return login;
	}
	
	public void addCoupon(Coupon coupon) {
		
		CouponDBDAO coupondao = new CouponDBDAO();
		Coupon existingCoupon = new Coupon();
		try {
			existingCoupon = coupondao.read(coupon.getId());
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		if (coupon.getCompanyId() == existingCoupon.getCompanyId() && coupon.getTitle().equalsIgnoreCase(existingCoupon.getTitle())) {
			try {
				throw new CouponSystemException("Cannot input two coupons with the same title and company");
			} catch (CouponSystemException e) {
				// TODO Seriously?!
				e.printStackTrace();
			}
		} else {
			try {
				coupondao.create(coupon);
			} catch (CouponSystemException e) {
				// TODO Add exception handle!
				e.printStackTrace();
			}
		}
	}
	
	public void updateCoupon (Coupon coupon) {
		
		CouponDBDAO coupondao = new CouponDBDAO();
		Coupon testCoupon = new Coupon();
		try {
			testCoupon = coupondao.read(coupon.getId());
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		if (testCoupon.getCompanyId() == coupon.getCompanyId() && testCoupon.getId() == coupon.getId()) {
			try {
				coupondao.update(coupon);
			} catch (CouponSystemException e) {
				// TODO Add exception handle!
				e.printStackTrace();
			}
		}
	}
	
	public void RemoveCoupon (Coupon coupon) {
		
		CouponDBDAO coupondao = new CouponDBDAO();
		try {
			coupondao.delete(coupon.getId());
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		try {
			coupondao.deleteHistory(coupon.getId());
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		
	}
	
	public List<Coupon> returnAllCoupons (Company company) {
		
		List coupons = new ArrayList<>();
		return coupons;
	}
	
	public List<Coupon> returnAllCouponsByCategory (Company company, Category category) {
		
		List coupons = new ArrayList<>();
		return coupons;
	}

	public List<Coupon> returnAllCouponsByMaxPrice (Company company, double maxPrice) {
	
		List coupons = new ArrayList<>();
		return coupons;
	}
	
	public Company getCompanyDetails (int companyId) {
		
		
		Company company = new Company();
		CompanyDBDAO companydao = new CompanyDBDAO();
		try {
			company = companydao.read(companyId);
		} catch (CouponSystemException e) {
			// TODO Add exception handle!	
			e.printStackTrace();
		}
		return company;
	}
	
public Company getCompanyDetailsByEmail (String email) {
		
		
		Company company = new Company();
		CompanyDBDAO companydao = new CompanyDBDAO();
		try {
			company = companydao.read(companydao.getIdByEmail(email));
		} catch (CouponSystemException e) {
			// TODO Add exception handle!	
			e.printStackTrace();
		}
		return company;
	}
 
}
