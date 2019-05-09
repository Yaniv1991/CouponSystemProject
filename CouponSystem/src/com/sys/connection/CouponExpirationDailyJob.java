package com.sys.connection;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import com.database.utils.DbExceptionHandler;
import com.sys.beans.Coupon;
import com.sys.dao.CouponDBDAO;
import com.sys.exception.CouponSystemException;
import com.sys.facades.LoginManager;

public class CouponExpirationDailyJob implements Runnable {

	private boolean quit = false;
	private CouponDBDAO couponDao ;
	public CouponExpirationDailyJob() {
		super();
		this.couponDao = LoginManager.getInstance().getCouponDao();
	}

	private final long sleepTime = 86400000;
	private List<Coupon> expiredCoupons;

	@Override
	public void run() {
		while (!quit) {
			try {
				expiredCoupons = (List<Coupon>) couponDao.readAll();
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
				couponDao.delete(expiredCoupon.getId());
		}
	}

	private boolean allCouponsWereDeleted() throws CouponSystemException {

		for (Coupon expiredCoupon : expiredCoupons) {
				if (couponDao.read(expiredCoupon.getId()) != null) {
					return false;
			}
		}

		return true;
	}

	private void removeUnexpiredCouponsFromList() {
		ZonedDateTime today = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		for (int i = 0; i < expiredCoupons.size(); i++) {
			if (expiredCoupons.get(i).getEndDate().isBefore(LocalDate.now(Clock.systemDefaultZone()))) {
				expiredCoupons.remove(i--);
			}
		}
	}

	public void stop() {
		quit = true;
	}
}
