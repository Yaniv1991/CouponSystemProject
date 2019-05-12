package com.sys.exception;

/**
 * {@code CouponSystemException} extends the class {@code Exception} and is used to manage
 * custom exceptions for the system.
 * @authors Yaniv Chen & Gil Gouetta
 */
public class CouponSystemException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4977947184190737549L;

	public CouponSystemException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CouponSystemException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public CouponSystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public CouponSystemException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CouponSystemException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
}
