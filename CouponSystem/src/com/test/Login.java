package com.test;

import java.util.Scanner;

import com.sys.exception.CouponSystemException;
import com.sys.facades.ClientFacade;
import com.sys.facades.LoginManager;

public class Login implements Action {
private String description = "Login";
private Scanner sc;	
private ClientFacade facade;

	public ClientFacade getFacade() {
	return facade;
}

	public Login(Scanner sc) {
	super();
	this.sc = sc;
}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void act() throws CouponSystemException {
		facade = LoginManager.getInstance().login(inputData(sc,"email"), inputData(sc,"password"));
		
	}

	@Override
	public String inputData(Scanner sc,String property) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
