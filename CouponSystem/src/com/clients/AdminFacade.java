package com.clients;

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

	@Override
	boolean login(String email, String password) {

		String correctEmail = "admin@admin.com";
		String correctPassword = "admin";
		return (email.equals(correctEmail) && password.equals(correctPassword));
	}

	public void addCompany(Company company) throws CompanyException {

		CompanyDBDAO companyDao = new CompanyDBDAO();
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

		CompanyDBDAO CompanyDao = new CompanyDBDAO();
		Company existingCompany = CompanyDao.read(company.getId());

		if (existingCompany.getId() != company.getId()
				|| !existingCompany.getName().equalsIgnoreCase(company.getName())) {
			throw new CompanyException("cannot update company id and name");
		}
		CompanyDao.update(company);
	}

	public void deleteCompany(Company company) throws CouponException, CompanyException {
		CouponDBDAO couponDao = new CouponDBDAO();
		CompanyDBDAO companyDao = new CompanyDBDAO();
		for (Coupon coupon : company.getCoupons()) {
			couponDao.delete(coupon.getId());
		}
		companyDao.delete(company.getId());
	}

	public Collection<Company> getAllCompanies() throws CompanyException {
		return new CompanyDBDAO().readAll();
	}

	public Company getCompanyById(int companyId) throws CompanyException {
		return new CompanyDBDAO().read(companyId);
	}

	public void addCustomer(Customer customer) throws CustomerException {
		CustomerDBDAO customerDao = new CustomerDBDAO();
		if (customerDao.read(customerDao.getIdByEmail(customer.getEmail())) != null) {
			throw new CustomerException("Customer exists with the same email");
		}
		customerDao.create(customer);
	}

	public void updateCustomer(Customer customer) throws CustomerException {
		CustomerDBDAO customerDao = new CustomerDBDAO();
		Customer existingCustomer = customerDao.read(customer.getId());
		if (customer.getId() != existingCustomer.getId()) {
			throw new CustomerException("cannot update customer id");
		}
		customerDao.update(customer);
	}

	public void removeCustomer(Customer customer) throws CustomerException {
		new CustomerDBDAO().delete(customer.getId());
	}

	public Collection<Customer> getAllCustomers() throws CustomerException {
		return new CustomerDBDAO().readAll();
	}

	public Customer returnCustomerById(int customerId) throws CustomerException {
		return new CustomerDBDAO().read(customerId);
	}

}
