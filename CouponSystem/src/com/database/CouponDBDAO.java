package com.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CouponDBDAO implements ElementDAO<Coupon> {
//TODO Change the sqlCreate Statement
	private static String sqlCreate = "insert into coupons "
			+ "(id,title,start_date,end_date,amount,type,description,price,image) " + "values (?,?,?,?,?,?,?,?,?)";

	private static String sqlRead = "select * from coupons where id = ?";

	private static String sqlUpdate = "update coupons set " + "title = ?, Start_date = ?, end_date = ?,"
			+ "amount = ? , category = ? , description = ?," + "price = ? ,image = ? where id = ?";

	private static String sqlDelete = "delete from coupons where id = ?";

	private Connection connection;

	@Override
	public void create(Coupon coupon) throws CouponSystemException {
		connect();
		try (PreparedStatement create = connection.prepareStatement(sqlCreate)) {
			java.sql.Date startDate = (Date) coupon.getStartDate();
			java.sql.Date endDate = (Date) coupon.getEndDate();
			create.setInt(1, coupon.getId());
			create.setString(2, coupon.getTitle());
			create.setDate(3, startDate);
			create.setDate(4, endDate);
			create.setInt(5, coupon.getAmount());
			create.setString(6, coupon.getCouponType().toString());
			create.setString(7, coupon.getMessage());
			create.setDouble(8, coupon.getPrice());
			create.setString(9, coupon.getImage());

			create.execute();
		} catch (Exception e) {
			DbExceptionHandler.HandleException(e);
		}
		finally {disconnect();}
	}

	@Override
	public Coupon read(int id) throws CouponSystemException {
		// ("select * from ? where id = ?")
		Coupon result = null;
		connect();
		try (PreparedStatement read = connection.prepareStatement(sqlRead)) {
			read.setString(1, "coupon");
			read.setInt(2, id);
			ResultSet rs = read.executeQuery();
			if (rs.next()) {
				result = readFromActiveConnection(id, rs);
			}

		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}
		finally {disconnect();}
		return result;
	}

	private Coupon readFromActiveConnection(int id, ResultSet rs) throws SQLException {
		Coupon result;
		result = new Coupon();

		int amount = rs.getInt("amount");
		String title = rs.getString("title");
		String message = rs.getString("message");
		Category type = Category.valueOf(rs.getString("type"));
		double price = rs.getDouble("price");
		Date startDate = rs.getDate("start_date");
		Date endDate = rs.getDate("end_date");
		String image = rs.getString("image");
		result.setAmount(amount);
		result.setCouponType(type);
		result.setEndDate(endDate);
		result.setStartDate(startDate);
		result.setId(id);
		result.setImage(image);
		result.setMessage(message);
		result.setPrice(price);
		result.setTitle(title);
		return result;
	}

	@Override
	public void update(Coupon coupon) throws CouponSystemException {
		/*
		 * "update coupon set " + "title = ?, Start_date = ?, end_date = ?," +
		 * "amount = ? , category = ? , description = ?," +
		 * "price = ? ,image = ? where id = ?"
		 */

		connect();
		try (PreparedStatement update = connection.prepareStatement(sqlUpdate)) {
			update.setString(1, coupon.getTitle());
			update.setDate(2, (Date) coupon.getStartDate());
			update.setDate(3, (Date) coupon.getEndDate());
			update.setInt(4, coupon.getAmount());
			update.setString(5, coupon.getCategory().toString());
			update.setString(6, coupon.getDescription());
			update.setDouble(7, coupon.getPrice());
			update.setString(8, coupon.getImage());
			update.setInt(9, coupon.getId());
			update.execute();
		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		} finally {
			disconnect();
		}
	}

	@Override
	public void delete(int id) throws CouponSystemException {
		connect();
		try (PreparedStatement delete = connection.prepareStatement(sqlDelete)) {
			delete.setInt(1, id);
			delete.execute();
		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		} finally {
			disconnect();
		}
	}

	@Override
	public Collection<Coupon> readAll() throws CouponSystemException {
		List<Coupon> result = new ArrayList<>();
		connect();
		try (Statement readAll = connection.createStatement()) {
			String sql = "select * from coupons";
			ResultSet rs = readAll.executeQuery(sql);
			while (rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("id"), rs));
			}

		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		} finally {
			disconnect();
		}
		return result;
	}

	public Collection<Coupon> readAll(Company company) throws CouponSystemException {
		List<Coupon> result = new ArrayList<>();
		connect();
		try (Statement readAll = connection.createStatement()) {
			String sql = "select * from coupons where company_id = " + company.getId();
			ResultSet rs = readAll.executeQuery(sql);
			while (rs.next()) {
				result.add(readFromActiveConnection(rs.getInt("id"), rs));
			}

		} catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		} finally {
			disconnect();
		}

		return result;
	}

	{
//	// FIXME Do an elegant override for dis shiet
//	@Override
//	public boolean exists(String... arguments) throws CouponSystemException {
//
//		boolean result = false;
//		int id;
//		if (arguments.length!=1) {
//			throw new CouponSystemException("invalid arguments. CouponDao Accepts only an id of type int");
//		}
//		try {
//			id = Integer.parseInt(arguments[0]);
//		}
//		catch(NumberFormatException e) {
//			throw new CouponSystemException("invalid arguments. CouponDao Accepts only an id of type int",e);
//		}
//		connect();
//		try (PreparedStatement read = connection.prepareStatement(sqlRead)) {
//			read.setInt(1, id);
//			ResultSet rs = read.executeQuery();
//			result = rs.next();
//		} catch (SQLException e) {
//			DbExceptionHandler.HandleException(e);
//		} finally {
//			disconnect();
//		}
//		return result;
//	}
	}
	private synchronized void connect() throws CouponSystemException {
		connection = ConnectionPool.getInstance().getConnection();
	}

	private synchronized void disconnect() {
		ConnectionPool.getInstance().restoreConnection(connection);
		connection = null;
	}

	@Override
	public boolean exists(int customerId, int companyId) throws CouponSystemException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addPurchase(int customerId, int companyId) throws CouponSystemException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePurchase(int customerId, int companyId) throws CouponSystemException {
		// TODO Auto-generated method stub
		
	}

}