package com.sys.facades;

import com.sys.dao.CategoryDBDAO;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.exception.CouponSystemException;
/**
 * Singleton class used to manage login requests to the DB.
 * @authors Gil Gouetta & Yaniv Chen.
 *
 */
public class LoginManager {

	private CouponDBDAO couponDao;
	private CompanyDBDAO companyDao;
	private CustomerDBDAO customerDao;
	private CategoryDBDAO categoryDao;

	public CouponDBDAO getCouponDao() {
		return couponDao;
	}

	public CompanyDBDAO getCompanyDao() {
		return companyDao;
	}

	public CustomerDBDAO getCustomerDao() {
		return customerDao;
	}

	public CategoryDBDAO getCategoryDao() {
		return categoryDao;
	}

	private static LoginManager instance = new LoginManager();

	public static LoginManager getInstance() {
		return instance;
	}

	private LoginManager() {
		categoryDao = new CategoryDBDAO();
		couponDao = new CouponDBDAO(categoryDao);
		companyDao = new CompanyDBDAO(couponDao);
		customerDao = new CustomerDBDAO(couponDao);
	}

	/**
	 * Login method for the coupon system, returns the relevant facade according to the login details.
	 * @param email String
	 * @param password String
	 * @return an instance of a {@link com.sys.facades.ClientFacade ClientFacade}
	 * @throws CouponSystemException
	 */
	public ClientFacade login(String email, String password) throws CouponSystemException {

		ClientFacade facade = null;
		if (email.equalsIgnoreCase("admin@admin.com") && password.equals("admin")) {
			facade = new AdminFacade(customerDao, companyDao, couponDao);
		} else if (companyDao.exists(email, password)) {
			facade = new CompanyFacade(companyDao.getIdByEmail(email), companyDao, couponDao);
		} else if (customerDao.exists(email, password)) {
			facade = new CustomerFacade(customerDao.getIdByEmail(email), couponDao, customerDao);
		} else
			throw new CouponSystemException("No matching credentials found in the system");
		return facade;
	}

}
