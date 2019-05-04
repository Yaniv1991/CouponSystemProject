package com.sys.facades;

import java.util.Collection;
import java.util.List;

import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.exception.CompanyException;
import com.sys.exception.CouponException;
import com.sys.exception.CustomerException;

public class AdminFacade extends ClientFacade {
private CustomerDBDAO customerDao;
private CompanyDBDAO companyDao;
private CouponDBDAO couponDao;

	
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

	public void addCompany(Company company) throws CompanyException {
		if (companyDao.read(companyDao.getIdByEmail(company.getEmail())) != null) {
			throw new CompanyException("Company with same email already exists");
		}
		List<Company> allCompanies = (List<Company>) companyDao.readAll();

		for (Company companyToCheck : allCompanies) {
			if (company.getName().equalsIgnoreCase(companyToCheck.getName())) {
				throw new CompanyException("Company with same name already exists");
			}
		}
		companyDao.create(company);
	}

	public void updateCompany(Company company) throws CompanyException {
		Company existingCompany = companyDao.read(company.getId());

		if (existingCompany.getId() != company.getId()
				|| !existingCompany.getName().equalsIgnoreCase(company.getName())) {
			throw new CompanyException("cannot update company id and name");
		}
		companyDao.update(company);
	}

	public void deleteCompany(Company company) throws CouponException, CompanyException {
		for (Coupon coupon : company.getCoupons()) {
			couponDao.delete(coupon.getId());
		}
		companyDao.delete(company.getId());
	}

	public Collection<Company> getAllCompanies() throws CompanyException {
		return companyDao.readAll();
	}

	public Company getCompanyById(int companyId) throws CompanyException {
		return companyDao.read(companyId);
	}

	public void addCustomer(Customer customer) throws CustomerException {
		if (customerDao.read(customerDao.getIdByEmail(customer.getEmail())) != null) {
			throw new CustomerException("Customer exists with the same email");
		}
		customerDao.create(customer);
	}

	public void updateCustomer(Customer customer) throws CustomerException {
		Customer existingCustomer = customerDao.read(customer.getId());
		if (customer.getId() != existingCustomer.getId()) {
			throw new CustomerException("cannot update customer id");
		}
		customerDao.update(customer);
	}

	public void removeCustomer(Customer customer) throws CustomerException {
		try {
			couponDao.deleteCouponsOfCustomer(customer.getId());
		} catch (CouponException e) {
			throw new CustomerException("error in deleting all coupons of customer",e);
		}
		
		customerDao.delete(customer.getId());
	}

	public Collection<Customer> getAllCustomers() throws CustomerException {
		return customerDao.readAll();
	}

	public Customer returnCustomerById(int customerId) throws CustomerException {
		return customerDao.read(customerId);
	}

}
