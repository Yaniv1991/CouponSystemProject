package com.sys.facades;

import java.util.Collection;

import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.dao.ElementDAO;
import com.sys.dao.UserDAO;
import com.sys.exception.CompanyException;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;
import com.sys.exception.CustomerException;
/**
 * 
 * Facade consisting of actions available for an "Admin" type client of the coupon system.
 * @authors Yaniv Chen & Gil Gouetta
 *
 */
public class AdminFacade extends ClientFacade {
private UserDAO<Customer> customerDao;
private UserDAO<Company> companyDao;
private ElementDAO<Coupon> couponDao;

	
	public AdminFacade(CustomerDBDAO customerDao, CompanyDBDAO companyDao, CouponDBDAO couponDao) {
	this.customerDao = customerDao;
	this.companyDao = companyDao;
	this.couponDao = couponDao;
}

	@Override
	boolean login(String email, String password) {

		String correctEmail = "admin@admin.com";
		String correctPassword = "admin";
		return (email.equals(correctEmail) && password.equals(correctPassword));
	}

	/**
	 * Add a single company to the system.
	 * @param company - a {@link com.sys.beans.Company Company} object.
	 * @throws CouponSystemException 
	 */
	public void addCompany(Company company) throws CouponSystemException {
		if (companyDao.read(companyDao.getIdByEmail(company.getEmail())) != null) {
			throw new CompanyException("Company with same email already exists");
		}
		Collection<Company> allCompanies =  companyDao.readAll();

		for (Company companyToCheck : allCompanies) {
			if (company.getName().equalsIgnoreCase(companyToCheck.getName())) {
				throw new CompanyException("Company with same name already exists");
			}
		}
		companyDao.create(company);
	}

	/**
	 * Update a company's details in the system.<br>
	 * Restrictions: Cannot update company's name or ID.
	 * @param company - a {@link com.sys.beans.Company Company} object.
	 * @throws CouponSystemException 
	 */
	public void updateCompany(Company company) throws CouponSystemException {
		Company existingCompany = companyDao.read(company.getId());

		if (existingCompany.getId() != company.getId()
				|| !existingCompany.getName().equalsIgnoreCase(company.getName())) {
			throw new CompanyException("cannot update company id and name");
		}
		companyDao.update(company);
	}

	/**
	 * Deletes a single company from the system.
	 * Also deletes all coupons under that company, and coupon purchase history for all coupons.
	 * @param company - a {@link com.sys.beans.Company Company} object.
	 * @throws CouponSystemException 
	 */
	public void removeCompany(Company company) throws CouponSystemException {
		company = companyDao.read(company.getId());
		for (Coupon coupon : company.getCoupons()) {
			couponDao.deleteCouponFromHistory(coupon.getId());
			couponDao.delete(coupon.getId());
			System.out.println("deleted " + coupon);
		}
		
		companyDao.delete(company.getId());
	}

	/**
	 * Returns all companies currently in the system.
	 * @return a collection of {@link com.sys.beans.Company Company} objects.
	 * @throws CouponSystemException 
	 */
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		return companyDao.readAll();
	}

	/**
	 * Returns a single company's details, using the company ID.
	 * @param companyId - Integer representing the company Id.
	 * @return a {@link com.sys.beans.Company Company} object.
	 * @throws CouponSystemException 
	 */
	public Company getCompanyById(int companyId) throws CouponSystemException {
		return companyDao.read(companyId);
	}

	/**
	 * Adds a single customer to the system.<br>
	 * Restrictions: Cannot add two customers with the same email address.
	 * @param customer - {@link com.sys.beans.Customer Customer} object.
	 * @throws CouponSystemException 
	 */
	public void addCustomer(Customer customer) throws CouponSystemException {
		if (customerDao.read(customerDao.getIdByEmail(customer.getEmail())) != null) {
			throw new CustomerException("Customer exists with the same email");
		}
		customerDao.create(customer);
	}

	/**
	 * Updates a single customer in the system.<br>
	 * Restrictions: Cannot update customer's ID. 
	 * @param customer - {@link com.sys.beans.Customer Customer} object.
	 * @throws CouponSystemException 
	 */
	public void updateCustomer(Customer customer) throws CouponSystemException {
		Customer existingCustomer = customerDao.read(customer.getId());
		if (customer.getId() != existingCustomer.getId()) {
			throw new CustomerException("cannot update customer id");
		}
		customerDao.update(customer);
	}

	/**
	 * Deletes a single customer from the system.<br>
	 * Also deleting the customer's history of coupons purchased.
	 * @param customer - {@link com.sys.beans.Customer Customer} object.
	 * @throws CouponSystemException 
	 */
	public void removeCustomer(Customer customer) throws CouponSystemException {
		try {
			Collection<Coupon> couponsToDelete = couponDao.readAll(customer);		
			for (Coupon coupon : couponsToDelete) {
				couponDao.deletePurchase(coupon.getId(), customer);
			}
		} catch (CouponException e) {
			throw new CustomerException("error in deleting all coupons of customer",e);
		}
		
		customerDao.delete(customer.getId());
	}

	/**
	 * Returns all customers in the system.
	 * @return a collection of {@link com.sys.beans.Customer Customer} objects.
	 * @throws CouponSystemException 
	 */
	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		return customerDao.readAll();
	}

	/**
	 * Returns a single customer from the system per ID given.
	 * @param customerId - an Integer for the customer's ID.
	 * @return {@link com.sys.beans.Customer Customer} object.
	 * @throws CouponSystemException 
	 */
	public Customer getCustomerById(int customerId) throws CouponSystemException {
		return customerDao.read(customerId);
	}

}
