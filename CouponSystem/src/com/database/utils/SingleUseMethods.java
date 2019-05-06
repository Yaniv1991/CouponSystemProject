package com.database.utils;

import com.sys.beans.Category;
import com.sys.dao.CategoryDBDAO;
import com.sys.exception.CouponSystemException;

public class SingleUseMethods {

	public static void main(String[] args) {
		try {
			createCategories();
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void createCategories() throws CouponSystemException {
		CategoryDBDAO categoryDao = new CategoryDBDAO();
		if (categoryDao.readAll().size() == 0) {
			for (Category category : Category.values()) {
				{
					categoryDao.create(category);
				}
			}
		}
//		Connection con = ConnectionPool.getInstance().getConnection();
//		String sql = "insert into categories(name) values(?)";
//		try {
//			PreparedStatement insert = con.prepareStatement(sql);
//			for (Category category : Category.values()) {
//				insert.setString(1, category.toString());
//				insert.execute();
//				System.out.println("inserted " + category + " to table");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			ConnectionPool.getInstance().restoreConnection(con);
//		}
	}
}
