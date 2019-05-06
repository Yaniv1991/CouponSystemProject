package com.sys.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sys.beans.Category;
import com.sys.connection.ConnectionPool;
import com.sys.exception.ConnectionException;
import com.sys.exception.CouponException;
import com.sys.exception.CouponSystemException;

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
		throw new CouponSystemException("error in creating category entry",e);
	} finally {disconnect();
	}
	return category;
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
		throw new CouponSystemException("error in creating category entry",e);
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


private synchronized void connect() throws CouponException {
	try {
		connection = ConnectionPool.getInstance().getConnection();
	} catch (ConnectionException e) {
		throw new CouponException("error in connecting", e);
	}
}

private synchronized void disconnect() throws CouponException {
	try {
		ConnectionPool.getInstance().restoreConnection(connection);
	} catch (ConnectionException e) {
		throw new CouponException("error in disconnecting", e);
	}
	connection = null;
}

}
