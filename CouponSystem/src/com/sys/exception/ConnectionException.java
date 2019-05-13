package com.sys.exception;

import java.sql.Connection;

/**
 * {@code ConnectionException} extends {@code CouponSystemException} and used for DB related
 * exceptions.
 * @authors Gil Gouetta & Yaniv Chen
 */

public class ConnectionException extends CouponSystemException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Connection connection;
	
	public Connection getConnection() {
	return connection;
}

	public ConnectionException() {
		super();
		
	}

	public ConnectionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		
	}

	public ConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

	public ConnectionException(String arg0) {
		super(arg0);
		
	}

	public ConnectionException(Throwable arg0) {
		super(arg0);
		
	}

	public ConnectionException(String arg0, Throwable arg1,Connection connection) {
		this(arg0,arg1);
		this.connection = connection;
	}


}
