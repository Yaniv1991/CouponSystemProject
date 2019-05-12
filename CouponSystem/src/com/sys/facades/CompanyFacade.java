package com.sys.facades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.sys.beans.Category;
import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

public class CompanyFacade extends ClientFacade{

	private Company company;
	public Company getCompany() {
		return company;
	}

	private CompanyDBDAO companyDao;
	private CouponDBDAO couponDao;
	public CompanyFacade(int id,CompanyDBDAO companyDao,CouponDBDAO couponDao) {
		super();
		company = new Company(id);
		this.companyDao=companyDao;
		this.couponDao = couponDao;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
			return companyDao.exists(email, password);	
	}
	
	public void addCoupon(Coupon coupon) throws CouponException {
		
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(company);
		//TODO maybe change to a Map<String,Coupon>....
		for (Coupon couponFromList : allCoupons) {
			if(couponFromList.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				throw new CouponException("Coupon already exists with the same title");
			}
		}
		couponDao.create(coupon);
	}
	
	public void updateCoupon (Coupon coupon) throws CouponException {
		Coupon existingCoupon = couponDao.read(coupon.getId());
				
		if (existingCoupon.getCompanyId() != coupon.getCompanyId() 
				|| existingCoupon.getId() != coupon.getId()) {
			throw new CouponException("cannot update coupon id and company id");
		}
		couponDao.update(coupon);
	}
	
	public void RemoveCoupon (Coupon coupon) throws CouponException {
			couponDao.delete(coupon.getId());
	}
	
	public Coupon read(int couponId) throws CouponException {
		return couponDao.read(couponId);
	}
	
	public Collection<Coupon> returnAllCoupons () throws CouponException {
		return couponDao.readAll(company);
	}
	
	public List<Coupon> returnAllCouponsByCategory (Category category) throws CouponException {
		List<Coupon> coupons = new ArrayList<>();
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(company);
		for (Coupon coupon : allCoupons) {
			if(coupon.getCategory().equals(category)) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	public List<Coupon> returnAllCouponsByMaxPrice (double maxPrice) throws CouponException {
		List<Coupon> coupons = new ArrayList<>();
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(company);
		for (Coupon coupon : allCoupons) {
			if(coupon.getPrice()<=maxPrice) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}
	
	public Company getCompanyDetails () throws CouponSystemException {
		return companyDao.read(company.getId());
	}
 
}