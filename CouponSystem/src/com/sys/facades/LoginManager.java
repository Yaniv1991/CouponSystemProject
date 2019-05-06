package com.sys.facades;

import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.exception.CouponSystemException;

public class LoginManager {

	private CouponDBDAO couponDao;
	private CompanyDBDAO companyDao;
	private CustomerDBDAO customerDao;

	private static LoginManager instance = new LoginManager();

	public static LoginManager getInstance() {
		return instance;
	}

	private LoginManager() {
		// TODO Auto-generated constructor stub
		couponDao = new CouponDBDAO();
		companyDao = new CompanyDBDAO(couponDao);
		customerDao = new CustomerDBDAO(couponDao);
	}

	public ClientFacade login(String email, String password) throws CouponSystemException {

		ClientFacade facade = null;
		if (email == "admin@admin.com" && password == "admin") {
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
