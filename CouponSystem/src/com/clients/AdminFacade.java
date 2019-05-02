package com.clients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.dao.CompanyDBDAO;
import com.sys.dao.CouponDBDAO;
import com.sys.dao.CustomerDBDAO;
import com.sys.exception.CouponSystemException;

public class AdminFacade extends ClientFacade {

	@Override
	boolean login(String email, String password) {

		String correctEmail = "admin@admin.com";
		String correctPassword = "admin";
		return (email.equals(correctEmail)&& password.equals(correctPassword));
	}

	public void addCompany(Company company) {

		CompanyDBDAO companydao = new CompanyDBDAO();
		try {
			Company checkCompany = companydao.read(companydao.getIdByEmail(company.getEmail()));
			if (company.getEmail() != checkCompany.getEmail() && company.getName() != checkCompany.getName()) {
				companydao.create(company);
			} else {
				throw new CouponSystemException("Company with similar name or email exists");
			}
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}

	}

	public void updateCompany(Company company) {

		CompanyDBDAO companydao = new CompanyDBDAO();
		Company checkCompany = new Company();
		try {
			checkCompany = companydao.read(company.getId());
			if (checkCompany.getId() == company.getId() && checkCompany.getName() == company.getName()) {
				companydao.update(company);
			} else {
				throw new CouponSystemException("Cannot update company name or ID");
			}

		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}

	}

	public void deleteCompany(Company company) {

		CouponDBDAO coupondao = new CouponDBDAO();
		CompanyDBDAO companydao = new CompanyDBDAO();
		List<Coupon> coupons = new ArrayList<Coupon>();
		for (Coupon coupon : company.getCoupons()) {
			coupons.add(coupon);
		}
		for (Coupon coupon : coupons) {
			try {
				coupondao.delete(coupon.getId());
				coupondao.deleteHistory(coupon.getId());
			} catch (CouponSystemException e) {
				// TODO Add exception handle!
				e.printStackTrace();
			}
		}
		try {
			companydao.delete(company.getId());
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}

	}

	public Collection<Company> getAllCompanies() {

		CompanyDBDAO companydao = new CompanyDBDAO();
		Collection<Company> companies = new ArrayList<Company>();
		try {
			companies = companydao.readAll();
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		return companies;

	}

	public Company getCompanyById(int companyId) {

		CompanyDBDAO companydao = new CompanyDBDAO();
		Company result = new Company();
		try {
			result = companydao.read(companyId);
			return result;
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		return null;

	}

	public void addCustomer(Customer customer) {

		CustomerDBDAO customerdao = new CustomerDBDAO();
		boolean checkCustomer = false;
		try {
			checkCustomer = customerdao.exists(customer.getEmail(), customer.getPassword());
			if (!checkCustomer) {
				customerdao.create(customer);
			} else {
				throw new CouponSystemException("Customer with a similar email already exists");
			}
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}

	}

	public void updateCustomer(Customer customer) {

		CustomerDBDAO customerdao = new CustomerDBDAO();
		Customer checkCustomer = new Customer();
		try {
			checkCustomer = customerdao.read(customer.getId());
			if (customer.getId() == checkCustomer.getId()) {
				customerdao.update(customer);
			} else {
				throw new CouponSystemException("Customer ID cannot be updated");
			}
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}

	}

	public void removeCustomer(Customer customer) {

		CustomerDBDAO customerdao = new CustomerDBDAO();
		try {
			customerdao.delete(customer.getId());
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
	}

	public Collection<Customer> getAllCustomers() {

		CustomerDBDAO customerdao = new CustomerDBDAO();
		Collection<Customer> customers = new ArrayList<>();
		try {
			customers = customerdao.readAll();
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		return customers;

	}

	public Customer returnCustomerById(int customerId) {

		Customer customer = new Customer();
		CustomerDBDAO customerdao = new CustomerDBDAO();
		try {
			customer = customerdao.read(customerId);
		} catch (CouponSystemException e) {
			// TODO Add exception handle!
			e.printStackTrace();
		}
		return customer;

	}

}
