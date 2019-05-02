package com.database.utils.testerClasses;

import java.io.IOException;
import java.sql.SQLException;

import com.sys.DbExceptionHandler;
import com.sys.exception.CouponSystemException;

public class DBExceptionHandlerTester {

	public static void main(String[] args) {
		try {
			throw new Exception("User was very very stupid and needs to go to jail");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			DbExceptionHandler.HandleException(e);
		}
		try {
			throw new CouponSystemException("Coupon was very very naughty and needs to calm down");
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			DbExceptionHandler.HandleException(e);
		}
		try {
			throw new InterruptedException("Thread could not finish his job because his wife didnt let him");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			DbExceptionHandler.HandleException(e);
		}
		try {
			throw new IOException("File was too handsome for java to handle");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			DbExceptionHandler.HandleException(e);
		}
		try {
			throw new SQLException("SQL is an exceptional language");
		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}

	}

}
