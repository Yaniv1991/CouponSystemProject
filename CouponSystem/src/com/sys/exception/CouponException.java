package com.sys.exception;

import com.sys.beans.Coupon;

/**
 * {@code CouponException} extends {@code CouponSystemException} for exceptions in the
 * coupon facade.
 * @authors Yaniv Chen & Gil Gouetta
 */

public class CouponException extends CouponSystemException{

	private  Coupon coupon;
	
	public Coupon getCoupon() {
		return coupon;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CouponException() {
		super();
		
	}

	public CouponException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		
	}

	public CouponException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

	public CouponException(String arg0) {
		super(arg0);
		
	}

	public CouponException(Throwable arg0) {
		super(arg0);
		
	}

	public CouponException(String arg0, Throwable arg1,Coupon coupon) {
		this(arg0,arg1);
		this.coupon = coupon;
	}

	
	
}
