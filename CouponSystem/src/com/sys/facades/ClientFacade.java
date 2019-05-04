package com.sys.facades;

import com.sys.exception.CouponSystemException;

public abstract class ClientFacade {
	
	abstract boolean login (String email, String password) throws CouponSystemException;

}
