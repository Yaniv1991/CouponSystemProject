package com.sys.facades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sys.beans.Category;
import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.ElementDAO;
import com.sys.dao.UserDAO;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

/**
 * Facade consisting of actions available for a "Company" type client of the
 * coupon system.
 * 
 * @authors Yaniv Chen & Gil Gouetta
 *
 */
public class CompanyFacade extends ClientFacade {

	private Company company;

	private UserDAO<Company> companyDao;
	private ElementDAO<Coupon> couponDao;

	public CompanyFacade(int id, CompanyDBDAO companyDao, CouponDBDAO couponDao) {
		super();
		company = new Company(id);
		this.companyDao = companyDao;
		this.couponDao = couponDao;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		return companyDao.exists(email, password);
	}

	/**
	 * Allows to add a coupon to the company's available coupons in the system.
	 * 
	 * @param coupon - a {@link com.sys.beans.Coupon Coupon} object.
	 * @throws CouponSystemException 
	 */
	public void addCoupon(Coupon coupon) throws CouponSystemException {

		Collection<Coupon> allCoupons = couponDao.readAll(company);

		for (Coupon couponFromList : allCoupons) {
			if (couponFromList.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				throw new CouponException("Coupon already exists with the same title");
			}
		}
		couponDao.create(coupon);
	}

	/**
	 * Updates a single coupon in the system.<br>
	 * Restrictions: Cannot update Coupon ID, Cannot update Company ID for the
	 * coupon.
	 * 
	 * @param coupon - a {@link com.sys.beans.Coupon Coupon} object.
	 * @throws CouponSystemException 
	 */
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Coupon existingCoupon = couponDao.read(coupon.getId());

		if (existingCoupon.getCompanyId() != coupon.getCompanyId() || existingCoupon.getId() != coupon.getId()) {
			throw new CouponException("cannot update coupon id and company id");
		}
		couponDao.update(coupon);
	}

	/**
	 * Deletes a single coupon from the system.<br>
	 * Also deletes coupon purchase history.
	 * 
	 * @param coupon - a {@link com.sys.beans.Coupon Coupon} object.
	 * @throws CouponSystemException 
	 */
	public void removeCoupon(Coupon coupon) throws CouponSystemException {
		couponDao.deleteCouponFromHistory(coupon.getId());
		couponDao.delete(coupon.getId());
	}

	/**
	 * Returns a single coupon from the system, using the coupon id.
	 * 
	 * @param couponId - coupon id.
	 * @return a {@link com.sys.beans.Coupon Coupon} object.
	 * @throws CouponSystemException 
	 */
	public Coupon read(int couponId) throws CouponSystemException {
		return couponDao.read(couponId);
	}

	/**
	 * Returns all the coupons the company has in the system.
	 * 
	 * @return a collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
	public Collection<Coupon> returnAllCoupons() throws CouponException {
		return couponDao.readAll(company);
	}

	/**
	 * Returns all the coupons the company has in the system that match the given
	 * category.
	 * 
	 * @param category - a {@link com.sys.beans.Category Category} ENUM value.
	 * @return a collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
	public List<Coupon> returnAllCouponsByCategory(Category category) throws CouponException {
		List<Coupon> coupons = new ArrayList<>();
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(company);
		for (Coupon coupon : allCoupons) {
			if (coupon.getCategory().equals(category)) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	/**
	 * Returns all the coupons the company has in the system that match the given
	 * maxPrice.
	 * 
	 * @param maxPrice - Double value for maximum price.
	 * @return - a collection of {@link com.sys.beans.Coupon Coupon} objects.
	 * @throws CouponException
	 */
	public List<Coupon> returnAllCouponsByMaxPrice(double maxPrice) throws CouponException {
		List<Coupon> coupons = new ArrayList<>();
		List<Coupon> allCoupons = (List<Coupon>) couponDao.readAll(company);
		for (Coupon coupon : allCoupons) {
			if (coupon.getPrice() <= maxPrice) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}

	/**
	 * Returns the logged-in company's details.
	 * 
	 * @return a {@link com.sys.beans.Company Company} object.
	 * @throws CouponSystemException
	 */
	public Company getCompanyDetails() throws CouponSystemException {
		return companyDao.read(company.getId());
	}

}