package com.sys.exception;

import com.sys.beans.Company;

public class CompanyException extends CouponSystemException{
private Company company;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public CompanyException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompanyException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public CompanyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public CompanyException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CompanyException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CompanyException(String arg0, Throwable arg1,Company company) {
		this(arg0,arg1);
		this.company = company;
	}
	
	public Company getCompany() {
		return company;
	}

}
