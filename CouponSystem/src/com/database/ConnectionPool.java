package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.database.exception.CouponSystemException;

public class ConnectionPool {

	private static final int MAX_CONNECTIONS = 10;
	private static ConnectionPool instance;
	private Set<Connection> connections = new HashSet<>();
	private String url = "jdbc:derby://localhost:1527/db1";
	private boolean poolIsClosing = false;

	private ConnectionPool() throws CouponSystemException {
		// add 10 connections to the set.
		while (connections.size() < MAX_CONNECTIONS) {
			try {
				for (int i = 0; i < MAX_CONNECTIONS; i++) {
					connections.add(DriverManager.getConnection(url));
				}
			}

			catch (SQLException e) {
				throw new CouponSystemException("Sql exception caused by Connection pool",e);
			}
		}
	}

	public static ConnectionPool getInstance() throws CouponSystemException {
		while (instance == null) {
				instance = new ConnectionPool();
		}
		return instance;
	}

	public synchronized void closeAllConnections() throws CouponSystemException {
		poolIsClosing = true;
		int numberOfClosedConnections = 0;
		while (numberOfClosedConnections < MAX_CONNECTIONS) {
			try {
				for (Connection connection : connections) {
					connection.close();
					numberOfClosedConnections++;
				}

				connections.clear();

				if (numberOfClosedConnections < MAX_CONNECTIONS) {
					wait();
				}

			} catch (SQLException | InterruptedException e) {
				throw new CouponSystemException("Exception raised in closing all connections", e);
			}
		}
	}

	public synchronized void restoreConnection(Connection connection) {
		connections.add(connection);
		notify();
	}

	public synchronized Connection getConnection() throws CouponSystemException {
		if (poolIsClosing) {
			throw new CouponSystemException("Pool is closing");
		}
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new CouponSystemException("Thread interrupted while getting a connection", e);

			}
		}
		Connection result = connections.iterator().next();
		connections.remove(result);
		return result;
	}

}
