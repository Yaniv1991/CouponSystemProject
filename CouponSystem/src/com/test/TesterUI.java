package com.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.sys.beans.Category;
import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.exception.CouponSystemException;
import com.sys.facades.AdminFacade;
import com.sys.facades.ClientFacade;
import com.sys.facades.CompanyFacade;
import com.sys.facades.CustomerFacade;
import com.sys.facades.LoginManager;

public class TesterUI {
	private Scanner in = new Scanner(System.in);

	private ClientFacade facade;
	private boolean quit = false;
	private boolean hasStarted = false;

	private List<Action> actions = new ArrayList<>();

	public void startUI() {

		while (!quit) {
			try {
				showMenu();
				
			} catch (CouponSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showMenu() throws CouponSystemException {

		showOptions();
		String command = in.next();
		performAction(command);
	}

	private void performAction(String command) throws CouponSystemException {

		if (!hasStarted) {
			switch (command) {
			case "start": {
				startProgram();
				break;
			}
			default: {
				throw new CouponSystemException("program has not started");
			}
			}
		} else {
			switch (command) {
			case "login": {
				login();
				break;
			}
			case "exit": {
				exit();
				break;
			}
			
			}

			if (facade instanceof AdminFacade) {
				AdminFacade facade = (AdminFacade)this.facade;
				/// ....Lots of options
				switch (command) {
				case "add company": {
					facade.addCompany(createCompany());
					break;
				}
				case "remove company": {
					facade.deleteCompany(new Company(readById("company")));
					break;
				}
				case "read all companies": {
					readList(facade.getAllCompanies());
					break;
				}
				case "update company": {
					List<Company> companies = (List<Company>) facade.getAllCompanies();
					readList(companies);
					Company companyToUpdate = facade.getCompanyById(readById("company"));
					updateCompany(companyToUpdate);
					facade.updateCompany(companyToUpdate);
					break;
				}

				case "add customer": {
					facade.addCustomer(createCustomer());
					break;
				}
				case "remove customer": {
					facade.removeCustomer(new Customer(readById("customer")));
					break;
				}
				case "read all customers": {
					readList(facade.getAllCustomers());
					break;
				}
				case "update customer": {
					List<Customer> customers = (List<Customer>) facade.getAllCustomers();
					readList(customers);
					Customer customerToUpdate = facade.returnCustomerById(readById("customer"));
					updateCustomer(customerToUpdate);
					facade.updateCustomer(customerToUpdate);
					break;
				}
				default: {
					throw new CouponSystemException("invalid input");
				}
				}
			}

			else if (facade instanceof CustomerFacade) {
				CustomerFacade facade = (CustomerFacade)this.facade;
				switch (command) {
				case "purchase coupon": {
					facade.purchaseCoupon(selectFromList((List<Coupon>) facade.getAllCopouns()));
					break;
				}
				case "cancel purchase": {
					///...TODO ....
					break;
				}
				case "read all purchased coupons": {
					readList(facade.getAllCopounsOfCustomer());
					break;
				}
				case "read all purchased coupons by category": {
					readList(facade.getAllCopounsByCategory(selectCategory()));
					break;
				}
				case "read all purchased coupons by max price": {
					readList(facade.getAllCopounsByMaxPrice(getMaxPrice()));
					break;
				}
				case "read customer details": {
					System.out.println(facade.getCustomerDetails());
					break;
				}

				default: {
					throw new CouponSystemException("invalid input");
				}
				}

			} else if (facade instanceof CompanyFacade) {
				CompanyFacade facade = (CompanyFacade)this.facade;
				switch (command) {
				case "add coupon": {
					facade.addCoupon(createCoupon());
					break;
				}
				case "delete coupon": {
					// TODO ....
					
					break;
				}
				case "update coupon": {
					
					Coupon couponToUpdate = facade.read(readById("coupon id"));
					updateCoupon(couponToUpdate);
					facade.updateCoupon(couponToUpdate);
					break;
				}
				case "read all coupons": {
					readList(facade.returnAllCoupons());
					break;
				}
				case "read coupons by category": {
					readList(facade.returnAllCouponsByCategory(selectCategory()));
					break;
				}
				case "read coupons by max price": {
					readList(facade.returnAllCouponsByMaxPrice(getMaxPrice()));
					break;
				}
				case "get company details": {
					System.out.println(facade.getCompanyDetails());;
					break;
				}
				default: {
					throw new CouponSystemException("invalid input");
				}
				}

			}
		}
	}

	private void updateCoupon(Coupon couponToUpdate) {
		// TODO Auto-generated method stub
		
	}

	private Coupon createCoupon() {
		// TODO Auto-generated method stub
		return null;
	}

	

	private double getMaxPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	private Category selectCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	private <T> int selectFromList(Collection<T> listToSelectFrom) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void updateCustomer(Customer customerToUpdate) {
		// TODO Auto-generated method stub
		
	}

	private Customer createCustomer() {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateCompany(Company companyToUpdate) {
		// TODO Auto-generated method stub
		
	}

	private <T> void readList(Collection<T> allCompanies){
		// TODO Auto-generated method stub
		
	}

	private int readById(String string) {
		System.out.println("enter the id of the " + string);
		// TODO Auto-generated method stub
		return -1;
	}

	private Company createCompany() {
		// TODO Auto-generated method stub
		return null;
	}

	private void exit() {
		quit = false;
	}

	private void startProgram() {
		hasStarted = true;
	}

	private void showOptions() {
		List<String> options =new ArrayList<String>;
	options.add("start");
	options.add("login");
	options.add("exit");
		
	if(facade instanceof AdminFacade) {
		options.add("add company");
		options.add("update company");
		options.add("delete company");
		options.add("show all companies");
		options.add("get company by id");
		options.add("add customer");
		options.add("update customer");
		options.add("delete customer");
		options.add("show all customers");
		options.add("get customer by id");
	}
	}

	private void login() throws CouponSystemException {
		facade = LoginManager.getInstance().login(inputData("email"), inputData("password"));

	}

	private String inputData(String propertyToInput) {
		System.out.println("enter " + propertyToInput);

		return in.next();
	}
}
