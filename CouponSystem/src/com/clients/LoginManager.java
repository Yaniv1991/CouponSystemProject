package com.clients;

import com.database.CompanyDBDAO;
import com.database.CouponSystemException;
import com.database.CustomerDBDAO;

public class LoginManager {
private static LoginManager instance = new LoginManager();
	
	public static LoginManager getInstance() {
	return instance;
}

	private LoginManager() {
		// TODO Auto-generated constructor stub
	}

	//TODO
	
	public ClientFacade login(String email, String password) {
		
		ClientFacade facade = null;
		CompanyDBDAO company = new CompanyDBDAO();
		CustomerDBDAO customer = new CustomerDBDAO();
		if (email == "admin@admin.com" && password == "admin") {
			facade = new AdminFacade(email,password);
		} else if (company.exists(email, password)) {
			facade = new CompanyFacade(email,password);
		} else if (customer.exists(email, password)) {
			facade = new CustomerFacade(email,password);
		} else throw new CouponSystemException("No matching credentials found in the system");
		return facade;
	}
//	public 
	
	
}
