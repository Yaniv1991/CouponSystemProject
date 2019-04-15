package com.database;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class CouponExpirationDailyJob implements Runnable {

	private boolean quit = false;
	private CouponDBDAO dao = new CouponDBDAO();
	private final long sleepTime = 86400000;
	private List<Coupon> expiredCoupons;

	@Override
	public void run() {
		while (!quit) {
			try {
				expiredCoupons = (List<Coupon>) dao.readAll();
				removeUnexpiredCouponsFromList();
				removeExpiredCouponsFromDB();
				if (!allCouponsWereDeleted()) {
					throw new CouponSystemException("Daily job could not delete all coupons");
				}
				expiredCoupons.clear();
				
				Thread.sleep(sleepTime);
			} catch (CouponSystemException | InterruptedException e) {
				
				DbExceptionHandler.HandleException(e);
				continue;
			}
		}
	}

	private void removeExpiredCouponsFromDB() {
		for (Coupon expiredCoupon : expiredCoupons) {
			try {
				dao.delete(expiredCoupon.getId());
			} catch (CouponSystemException e) {
				DbExceptionHandler.HandleException(e);
			}
		}
	}

	private boolean allCouponsWereDeleted() {

		for (Coupon expiredCoupon : expiredCoupons) {
			try {
				if (dao.exists(String.valueOf(expiredCoupon.getId()))) {
					return false;
				}
			} catch (CouponSystemException e) {
				DbExceptionHandler.HandleException(e);
				return false;
			}
		}

		return true;
	}

	private void removeUnexpiredCouponsFromList() {
		ZonedDateTime today = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		for (int i = 0; i < expiredCoupons.size(); i++) {
			if (expiredCoupons.get(i).getEndDate().before(Date.from(today.toInstant()))) {
				expiredCoupons.remove(i--);
			}
		}
	}

	public void stop() {
		quit = true;
	}
}
