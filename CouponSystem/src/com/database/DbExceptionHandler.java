package com.database;

import java.sql.SQLException;

public class DbExceptionHandler {

	public static void HandleException(Exception e) {
		if(e instanceof CouponSystemException) {
			//Do something
		}
		if(e instanceof SQLException) {
			System.out.println("SQL Exception. Inaal Rabak");
		}
	}
}
