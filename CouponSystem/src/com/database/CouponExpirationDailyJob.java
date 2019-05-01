package com.database;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import com.database.exception.CouponSystemException;

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
				if (allCouponsWereDeleted()) {
					expiredCoupons.clear();
				}
				
				Thread.sleep(sleepTime);
			} catch (CouponSystemException | InterruptedException e) {
				
				DbExceptionHandler.HandleException(e);
				continue;
			}
		}
	}

	private void removeExpiredCouponsFromDB() throws CouponSystemException {
		for (Coupon expiredCoupon : expiredCoupons) {
				dao.delete(expiredCoupon.getId());
		}
	}

	private boolean allCouponsWereDeleted() throws CouponSystemException {

		for (Coupon expiredCoupon : expiredCoupons) {
				if (dao.read(expiredCoupon.getId()) != null) {
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
