package com.database.exception;

import com.database.Customer;

public class CustomerException extends CouponSystemException {

	private Customer customer;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public CustomerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public CustomerException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CustomerException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
	public CustomerException(String arg0, Throwable arg1,Customer customer) {
		this(arg0,arg1);
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	
}
