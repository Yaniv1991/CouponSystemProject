package com.sys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sys.beans.Category;
import com.sys.connection.ConnectionPool;
import com.sys.exception.ConnectionException;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

/**
 * DAO objects to handle Categories table in the DB and {@link com.sys.beans.Category Category} java bean.
 * @authors Yaniv Chen & Gil Gouetta
 */

public class CategoryDBDAO implements DAO<Category>{

	private Connection connection;
	
	private static String readCategoryById = "select * from categories where id = ?";
	private static String createCategory= "insert into categories(name) values(?)";
	private static String deleteCategory= "DELETE FROM categories WHERE id = ?";

@Override
public void create(Category category) throws CouponSystemException {
	connect();
	try(PreparedStatement create = connection.prepareStatement(createCategory)){
		create.setString(1, category.toString());
		create.execute();
	} catch (SQLException e) {
		throw new CouponSystemException("error in creating category entry",e);
	} finally {disconnect();
	}
}

@Override
public Category read(int categoryId) throws CouponSystemException {
	Category category = null;
	connect();
	try(PreparedStatement read = connection.prepareStatement(readCategoryById)){
		read.setInt(1, categoryId);
		ResultSet result = read.executeQuery();
		if(result.next()) {
			category = Category.valueOf(result.getString("name"));
		}
	} catch (SQLException e) {
		throw new CouponSystemException("error in reading category entry",e);
	} finally {disconnect();
	}
	return category;
}

/**
 * Fetches the ID for the category from the Categories table in the DB.
 * @param category
 * @return an Integers for the category ID in the categories table in the DB.
 * @throws CouponSystemException
 */
public int getCategoryId(Category category) throws CouponSystemException {
	int result = 1;
	String sql = "select id from categories where name = ?";
	try(PreparedStatement read = connection.prepareStatement(sql)){
		read.setString(1, category.toString());
		ResultSet rs = read.executeQuery();
		if(rs.next()) {
			result = rs.getInt("id");
		}
	} catch (SQLException e) {
		throw new CouponSystemException("error in creating category entry",e);
	} finally {disconnect();
	}
	
	return result;
}

@Override
public void update(Category category) throws CouponSystemException {
	throw new CouponSystemException("cannot update an Enum constant");
}

@Override
public void delete(int categoryId) throws CouponSystemException {
	connect();
	try(PreparedStatement delete = connection.prepareStatement(deleteCategory)){
		delete.setInt(1, categoryId);;
		delete.execute();
	} catch (SQLException e) {
		throw new CouponSystemException("error in deleting category entry",e);
	} finally {disconnect();
	}
}

@Override
public Collection<Category> readAll() throws CouponSystemException {
	List<Category> result = new ArrayList<>(Category.values().length);
	for(Category category : Category.values()) {
		result.add(category);
	}
	return result;
	
}

/**
 * {@code allCategoriesById}<br><br>
 * Creates a HashMap assorted collection of categories and their respective id's.
 * @return a HashMap collection of categories.
 */
public Map<Integer,Category> allCategoriesById(){
	Map<Integer,Category> result = new HashMap<Integer,Category>();
	for(int i = 0; i<Category.values().length;i++) {
		result.put(i+1, Category.values()[i]);
	}
	return result;
}

/**
 * Receives a connection from the {@link com.sys.connection.ConnectionPool ConnectionPool}
 * @throws CouponException
 */
private synchronized void connect() throws CouponException {
	try {
		connection = ConnectionPool.getInstance().getConnection();
	} catch (ConnectionException e) {
		throw new CouponException("error in connecting", e);
	}
}

/**
 * Returns the connection to the {@link com.sys.connection.ConnectionPool ConnectionPool}
 * @throws CouponException
 */
private synchronized void disconnect() throws CouponException {
	try {
		ConnectionPool.getInstance().restoreConnection(connection);
	} catch (ConnectionException e) {
		throw new CouponException("error in disconnecting", e);
	}
	connection = null;
}

}
