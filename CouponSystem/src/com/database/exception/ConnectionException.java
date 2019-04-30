package com.database.exception;

import java.sql.Connection;

public class ConnectionException extends CouponSystemException{
private Connection connection;
	
	public Connection getConnection() {
	return connection;
}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
