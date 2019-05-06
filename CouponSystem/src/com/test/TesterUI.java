package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.sys.beans.Category;
import com.sys.beans.Company;
import com.sys.beans.Coupon;
import com.sys.beans.Customer;
import com.sys.connection.CouponExpirationDailyJob;
import com.sys.exception.CouponSystemException;
import com.sys.facades.AdminFacade;
import com.sys.facades.ClientFacade;
import com.sys.facades.CompanyFacade;
import com.sys.facades.CustomerFacade;
import com.sys.facades.LoginManager;

public class TesterUI {
	private final Scanner in = new Scanner(System.in);
	private CouponExpirationDailyJob dailyJob;

	private ClientFacade facade;
	private boolean quit = false;
	private boolean hasStarted = false;

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
		String command = in.nextLine();
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
				AdminFacade facade = (AdminFacade) this.facade;
				/// ....Lots of options
				switch (command) {
				case "add company": {
					facade.addCompany(createCompany());
					break;
				}
				case "update company": {
					List<Company> companies = (List<Company>) facade.getAllCompanies();
					readList(companies);
					Company companyToUpdate = facade.getCompanyById(readInteger("company"));
					updateCompany(companyToUpdate);
					facade.updateCompany(companyToUpdate);
					break;
				}
				case "delete company": {
					facade.removeCompany(new Company(readInteger("company")));
					break;
				}
				case "show all companies": {
					readList(facade.getAllCompanies());
					break;
				}
				case "get company by id":{
					System.out.println(facade.getCompanyById(readInteger("company id")));
					break;
				}

				case "add customer": {
					facade.addCustomer(createCustomer());
					break;
				}
				
				case "delete customer": {
					facade.removeCustomer(new Customer(readInteger("customer")));
					break;
				}
				case "read all customers": {
					readList(facade.getAllCustomers());
					break;
				}
				case "update customer": {
					
					Customer customerToUpdate = facade.getCustomerById(readInteger("customer id"));
					updateCustomer(customerToUpdate);
					facade.updateCustomer(customerToUpdate);
					break;
				}
				case "get customer by id":{
					System.out.println(facade.getCustomerById(readInteger("customer id")));
				}
//				default: {
//					throw new CouponSystemException("invalid input");
//				}
				}
			}

			else if (facade instanceof CustomerFacade) {
				CustomerFacade facade = (CustomerFacade) this.facade;
				switch (command) {
				case "purchase coupon": {
					facade.purchaseCoupon(readInteger("coupon id"));
					break;
				}
				
				case "get all coupons": {
					readList(facade.getAllCopounsOfCustomer());
					break;
				}
				case "get coupons by category": {
					readList(facade.getAllCopounsByCategory(selectCategory()));
					break;
				}
				case "get coupons by max price": {
					readList(facade.getAllCopounsByMaxPrice(readDouble("coupon max price")));
					break;
				}
				case "get company details": {
					System.out.println(facade.getCustomerDetails());
					break;
				}

//				default: {
//					throw new CouponSystemException("invalid input");
//				}
				}

			} else if (facade instanceof CompanyFacade) {
				CompanyFacade facade = (CompanyFacade) this.facade;
				switch (command) {
				case "add coupon": {
					facade.addCoupon(createCoupon());
					break;
				}
				case "delete coupon": {
					facade.removeCoupon(new Coupon(readInteger("coupon id")));
					break;
				}
				case "update coupon": {

					Coupon couponToUpdate = facade.read(readInteger("coupon id"));
					updateCoupon(couponToUpdate);
					facade.updateCoupon(couponToUpdate);
					break;
				}
				case "get all coupons": {
					readList(facade.returnAllCoupons());
					break;
				}
				case "get coupons by category": {
					readList(facade.returnAllCouponsByCategory(selectCategory()));
					break;
				}
				case "get coupons by max price": {
					readList(facade.returnAllCouponsByMaxPrice(readDouble("coupon max price")));
					break;
				}
				case "get company details": {
					System.out.println(facade.getCompanyDetails());
					;
					break;
				}
//				default: {
//					throw new CouponSystemException("invalid input");
//				}
				}

			}
		}
	}

	private void updateCoupon(Coupon couponToUpdate) {
		Coupon updatedCoupon = createCoupon();
		couponToUpdate.setAmount(updatedCoupon.getAmount());
		couponToUpdate.setTitle(updatedCoupon.getTitle());
		couponToUpdate.setDescription(updatedCoupon.getDescription());
		couponToUpdate.setCategory(updatedCoupon.getCategory());
		couponToUpdate.setPrice(updatedCoupon.getPrice());
		couponToUpdate.setStartDate(updatedCoupon.getStartDate());
		couponToUpdate.setEndDate(updatedCoupon.getEndDate());
		couponToUpdate.setImage(updatedCoupon.getImage());
	}

	private Coupon createCoupon() {
		Coupon result = new Coupon();
		int amount = readInteger("coupon amount");
		String title = inputData("coupon title");
		String message = inputData("coupon message");
		Category type = selectCategory();
		double price = readDouble("coupon price");
		Date startDate = readDate("coupon start date");
		Date endDate = readDate("coupon end date");
		String image = inputData("coupon image");
		result.setAmount(amount);
		result.setCouponType(type);
		result.setEndDate(endDate);
		result.setStartDate(startDate);
		result.setImage(image);
		result.setDescription(message);
		result.setPrice(price);
		result.setTitle(title);
		return result;
	}

	private Date readDate(String message) {
		while (true) {
			System.out.println("enter" + message);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			String input = in.nextLine();
			try {
				Date result = sdf.parse(input);
				return result;
			} catch (ParseException e) {
				System.out.println("invalid date");
			}
		}
	}


	private Category selectCategory() {
		for (Category category : Category.values()) {
			System.out.println(category);
		}
		return Category.valueOf(in.nextLine());
	}


	private void updateCustomer(Customer customerToUpdate) {
		Customer updatedDetails = createCustomer();
		customerToUpdate.setFirstName(updatedDetails.getFirstName());
		customerToUpdate.setLastName(updatedDetails.getLastName());
		customerToUpdate.setEmail(updatedDetails.getEmail());
		customerToUpdate.setPassword(updatedDetails.getPassword());
	}

	private Customer createCustomer() {
		Customer result = new Customer();
		result.setFirstName(inputData("customer's first name"));
		result.setLastName(inputData("customer's last name"));
		result.setEmail(inputData("customer's email"));
		result.setPassword(inputData("customer's password"));

		return result;
	}

	private void updateCompany(Company companyToUpdate) {
		Company updatedCompany = createCompany();
		companyToUpdate.setName(updatedCompany.getName());
		companyToUpdate.setEmail(updatedCompany.getEmail());
		companyToUpdate.setPassword(updatedCompany.getPassword());
	}

	private <T> void readList(Collection<T> collection) {
		for (T t : collection) {
			System.out.println(t);
		}
	}

	private int readInteger(String message) {
		while (true) {
			try {
				System.out.println("enter " + message);
				Integer result = Integer.parseInt(inputData(message));
				return result;
			} catch (NumberFormatException e) {
				System.out.println("invalid number");
				continue;
			}
		}
	}

	private double readDouble(String message) {
		while (true) {
			try {
				Double result = Double.parseDouble(inputData(message));
				return result;
			} catch (NumberFormatException e) {
				System.out.println("invalid number");
				continue;
			}
		}
	}

	private Company createCompany() {
		Company result = new Company();
		result.setName(inputData("company name"));
		result.setEmail(inputData("company email"));
		result.setPassword(inputData("company password"));
		return result;
	}

	private void exit() {
		quit = true;
		dailyJob.stop();
	}

	private void startProgram() {
		hasStarted = true;
		dailyJob = new CouponExpirationDailyJob();
		Thread job = new Thread(dailyJob);
		job.start();
	}

	private void showOptions() {
		List<String> options = new ArrayList<String>();
		options.add("start");
		options.add("login");
		options.add("exit");

		if (facade instanceof AdminFacade) {
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
		if (facade instanceof CustomerFacade) {
			options.add("purchase coupon");
			options.add("get all coupons");
			options.add("get coupons by category");
			options.add("get coupons by max price");
			options.add("get customer details");
		}

		if (facade instanceof CompanyFacade) {
			options.add("add coupon");
			options.add("delete coupon");
			options.add("update coupon");
			options.add("get all coupons");
			options.add("get coupons by category");
			options.add("get coupons by max price");
			options.add("get company details");
		}

		System.out.println();
		System.out.println();
		System.out.println();
		
		for (String string : options) {
			System.out.println(string);
		}
	}

	private void login() throws CouponSystemException {
		String email = inputData("email");
		String password = inputData("password");
		facade = LoginManager.getInstance().login(email,password);
	}

	private String inputData(String propertyToInput) {
		System.out.println("enter " + propertyToInput);
		return in.nextLine();
	}
}
