package com.clients;

import com.sys.exception.CouponSystemException;

public abstract class ClientFacade {
	
	abstract boolean login (String email, String password) throws CouponSystemException;

}
