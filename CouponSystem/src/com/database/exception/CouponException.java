package com.database.exception;

import com.database.Coupon;

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
		// TODO Auto-generated constructor stub
	}

	public CouponException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public CouponException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public CouponException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CouponException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CouponException(String arg0, Throwable arg1,Coupon coupon) {
		this(arg0,arg1);
		this.coupon = coupon;
	}

	
	
}
