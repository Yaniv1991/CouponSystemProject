package com.sys.connection;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import com.database.utils.DbExceptionHandler;
import com.sys.beans.Coupon;
import com.sys.dao.CouponDBDAO;
import com.sys.exception.CouponSystemException;

/**
 * 
 * @authors Yaniv Chen & Gil Gouetta.
 * 
 * Creates the daily job for the DB that deletes expired coupons.
 *
 */


public class CouponExpirationDailyJob implements Runnable {

	private boolean quit = false;
	private CouponDBDAO dao = new CouponDBDAO();
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
				dao.delete(expiredCoupon.getId());
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
				if (dao.read(expiredCoupon.getId()) != null) {
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
		ZonedDateTime today = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		for (int i = 0; i < expiredCoupons.size(); i++) {
			if (expiredCoupons.get(i).getEndDate().before(Date.from(today.toInstant()))) {
				expiredCoupons.remove(i--);
			}
		}
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
