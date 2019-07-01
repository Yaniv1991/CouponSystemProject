package com.database.utils.testerClasses;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import com.sys.beans.Category;
import com.sys.beans.Coupon;
import com.sys.dao.CategoryDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.exception.CouponSystemException;

public class ReadAllExpiredCouponsTester {

	public static void main(String[] args) throws CouponSystemException {
		CouponDBDAO couponDao = new CouponDBDAO(new CategoryDBDAO());
		for (int i = 0; i < 5; i++) {
			
		LocalDate endDate = LocalDate.now(Clock.systemDefaultZone()).minusDays(i);
		Coupon coupon = new Coupon();
		coupon.setTitle("tester " + i);
		coupon.setCompanyId(101);
		coupon.setCategory(Category.COMPUTER);
		coupon.setStartDate(LocalDate.now(Clock.systemDefaultZone()).minusDays(i*2));
		coupon.setEndDate(endDate);
		couponDao.create(coupon);
		
		}
		
		List<Coupon> coupons = (List<Coupon>) couponDao.readAllExpiredCoupons();
		for (Coupon coupon : coupons) {
			System.out.println(coupon);
		}
	}

}
