package com.clients;

import java.util.ArrayList;
import java.util.List;
import com.sys.beans.Category;
import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

public class CompanyFacade extends ClientFacade{

	private int id;
	
	public CompanyFacade(int id) {
		super();
		this.id = id;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
			return new CompanyDBDAO().exists(email, password);	
	}
	
	public void addCoupon(Coupon coupon) throws CouponException {
		
		CouponDBDAO couponDao = new CouponDBDAO();
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(new Company(id));
		//TODO maybe change to a Map<String,Coupon>....
		for (Coupon couponFromList : allCoupons) {
			if(couponFromList.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				throw new CouponException("Coupon already exists with the same title");
			}
		}
		couponDao.create(coupon);
	}
	
	public void updateCoupon (Coupon coupon) throws CouponException {
		CouponDBDAO couponDao = new CouponDBDAO();
		Coupon existingCoupon = couponDao.read(coupon.getId());
				
		if (existingCoupon.getCompanyId() != coupon.getCompanyId() 
				|| existingCoupon.getId() != coupon.getId()) {
			throw new CouponException("cannot update coupon id and company id");
		}
		
		couponDao.update(coupon);
	}
	
	public void RemoveCoupon (Coupon coupon) throws CouponException {
		CouponDBDAO coupondao = new CouponDBDAO();
			coupondao.delete(coupon.getId());
	}
	
	public List<Coupon> returnAllCoupons () throws CouponException {
		return (List<Coupon>) new CouponDBDAO().readAll(new Company(id));
	}
	
	public List<Coupon> returnAllCouponsByCategory (Category category) throws CouponException {
		CouponDBDAO couponDao = new CouponDBDAO();
		List<Coupon> coupons = new ArrayList<>();
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(new Company(id));
		for (Coupon coupon : allCoupons) {
			if(coupon.getCategory().equals(category)) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	public List<Coupon> returnAllCouponsByMaxPrice (double maxPrice) throws CouponException {
		CouponDBDAO couponDao = new CouponDBDAO();
		List<Coupon> coupons = new ArrayList<>();
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(new Company(id));
		for (Coupon coupon : allCoupons) {
			if(coupon.getPrice()<=maxPrice) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}
	
	public Company getCompanyDetails () throws CouponSystemException {
		return new CompanyDBDAO().read(id);
	}
 
}