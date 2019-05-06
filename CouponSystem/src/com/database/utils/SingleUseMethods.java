package com.database.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sys.beans.Category;
import com.sys.connection.ConnectionPool;
import com.sys.exception.ConnectionException;

public class SingleUseMethods {

	public static void main(String[] args) {
		try {
			createCategories();
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void createCategories() throws ConnectionException {
		Connection con = ConnectionPool.getInstance().getConnection();
		String sql = "insert into categories(name) values(?)";
		try {
			PreparedStatement insert = con.prepareStatement(sql);
			for (Category category : Category.values()) {
				insert.setString(1, category.toString());
				insert.execute();
				System.out.println("inserted " + category + " to table");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().restoreConnection(con);
		}
	}
}
