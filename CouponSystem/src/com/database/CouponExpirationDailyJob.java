package com.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CouponExpirationDailyJob implements Runnable {
//TODO a better implementation
	
	private boolean quit = false;
	private CouponDBDAO dao = new CouponDBDAO();
	private final long sleepTime = 86400000;
	private List<Coupon> expiredCoupons;

	@Override
	public void run() {

		try {
			expiredCoupons = (List<Coupon>) dao.readAll();
			removeUnexpiredCouponsFromList();
			if (!removeExpiredCouponsFromDB()) {
				throw new CouponSystemException("Daily job could not deete all coupons");
			}
			if (quit) {
				return;
			}
			Thread.sleep(sleepTime);
		} catch (CouponSystemException | InterruptedException e) {
			DbExceptionHandler.HandleException(e);
		}
	}

	private boolean removeExpiredCouponsFromDB() {
		for (Coupon expiredCoupon : expiredCoupons) {
			dao.delete(expiredCoupon.getId());
		}
		return checkIfAllCouponsDeleted();
	}

	private boolean checkIfAllCouponsDeleted() {
		
		for(Coupon expiredCoupon : expiredCoupons) {
			if(dao.read(expiredCoupon.getId()) != null) {
				return false;
			}
		}
		expiredCoupons.clear();
		return true;
	}

	private void removeUnexpiredCouponsFromList() {
		// TODO Auto-generated method stub
		Date today = new Date();
		for (int i = 0; i < expiredCoupons.size(); i++) {
			if (expiredCoupons.get(i).getEndDate().equals(today)) {
				expiredCoupons.remove(i--);
			}
		}
	}

	public void stop() {
		quit = true;
	}
}
