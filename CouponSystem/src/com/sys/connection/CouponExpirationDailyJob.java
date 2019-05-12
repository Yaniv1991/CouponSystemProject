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

/**
 * 
 * Creates the daily job for the DB that deletes expired coupons.
 * 
 * @authors Yaniv Chen & Gil Gouetta.
 *
 */

public class CouponExpirationDailyJob implements Runnable {

	private boolean quit = false;
	private CouponDBDAO couponDao ;
	
	/**
	 * Generator for the daily job.
	 */
	public CouponExpirationDailyJob() {
		super();
		this.couponDao = LoginManager.getInstance().getCouponDao();
	}

	private final long sleepTime = 86400000;
	private List<Coupon> expiredCoupons;

	/**
	 * 
	 * Implements {@code runnable} method {@code run()}.</br>
	 * This Daily job is affectively running everyday while the system is up.
	 * 
	 */
	@Override
	public void run() {
		while (!quit) {
			try {
				expiredCoupons = (List<Coupon>) couponDao.readAll();
//				System.out.println(expiredCoupons);
				removeUnexpiredCouponsFromList();
				System.out.println(expiredCoupons);
				removeExpiredCouponsFromDB();
//				if (allCouponsWereDeleted()) {
//					expiredCoupons.clear();
//				}
				
				Thread.sleep(sleepTime);
			} catch (CouponSystemException | InterruptedException e) {
				if(quit) {
					System.out.println("Thread ended safely");
				}
				else {
				DbExceptionHandler.HandleException(e);
				continue;}
			}
		}
	}

	/**
	 * 
	 * {@code removeExpiredCouponsFromDB}</br></br>
	 * Removes expired coupons from the DB.</br>
	 * Iterates through all coupons in the list, pulled from the DB, </br>
	 * and "cleaned" using {@link #removeUnexpiredCouponsFromList() removeUnexpiredCouponsFromList} and deletes all of them.
	 * 
	 * @throws CouponSystemException
	 */
	private void removeExpiredCouponsFromDB() throws CouponSystemException {
		for (Coupon expiredCoupon : expiredCoupons) {
				couponDao.delete(expiredCoupon.getId());
		}
	}

	/**
	 * {@code allCouponsWereDeleted}</br></br>
	 * Checks if all expired coupons were deleted.	
	 * 
	 * @return True if the list of coupons to delete is empty, False otherwise.
	 * @throws CouponSystemException
	 */
	private boolean allCouponsWereDeleted() throws CouponSystemException {

		for (Coupon expiredCoupon : expiredCoupons) {
				if (couponDao.read(expiredCoupon.getId()) != null) {
					return false;
			}
		}

		return true;
	}

	/**
	 * {@code removeUnexpiredCouponsFromList}</br></br>
	 * Iterates through the list of coupons, pulled from the DB,</br>
	 * and removes the ones that haven't expired.
	 * 
	 */
	private void removeUnexpiredCouponsFromList() {
		for (int i = 0; i < expiredCoupons.size(); i++) {
			if (expiredCoupons.get(i).getEndDate().isAfter(LocalDate.now(Clock.systemDefaultZone()))) {
//				System.out.println(expiredCoupons.get(i).getEndDate() +  "is after" + LocalDate.now(Clock.systemDefaultZone()));
				expiredCoupons.remove(i);
				i--;
			}
		}
		System.out.print(expiredCoupons.size());
	}

	/**
	 * {@code stop}</br></br>
	 * Stops the thread. Sets {@code quit} to true. 
	 * 
	 */
	public void stop() {
		quit = true;
	}
}
