package com.sys.exception;

import java.sql.Connection;

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
		// TODO Auto-generated constructor stub
	}

	public ConnectionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public ConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ConnectionException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ConnectionException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ConnectionException(String arg0, Throwable arg1,Connection connection) {
		this(arg0,arg1);
		this.connection = connection;
	}


}
