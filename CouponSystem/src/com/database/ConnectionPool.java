package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ConnectionPool {

	private static final int MAX_CONNECTIONS = 10;
	private static ConnectionPool instance = new ConnectionPool();
	private Set<Connection> connections = new HashSet<>();
	private String url = "jdbc:derby://localhost:1527/db1";
	private boolean poolIsClosing = false;

	private ConnectionPool() {
		// add 10 connections to the set.
		try {
			for (int i = 0; i < MAX_CONNECTIONS; i++) {
				connections.add(DriverManager.getConnection(url));
			}
		}

		catch (SQLException e) {
			DbExceptionHandler.HandleException(e);
		}
	}

	public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	public synchronized void closeAllConnections() {
		poolIsClosing = true;
		int numberOfClosedConnections = 0;
		Set<Connection> connectionsToRemove = new HashSet<>();
		while (numberOfClosedConnections < MAX_CONNECTIONS) {
			try {
				for (Connection connection : connections) {
					connection.close();
					connectionsToRemove.add(connection);
					numberOfClosedConnections++;
				}
				
				for (Connection connection : connectionsToRemove) {
					connections.remove(connection);
				}
				connectionsToRemove.clear();
				
				if (numberOfClosedConnections < MAX_CONNECTIONS ) {
					wait();
				}
				
			} catch (SQLException | InterruptedException e) {
				DbExceptionHandler.HandleException(e);
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
				DbExceptionHandler.HandleException(e);

			}
		}
		Connection result = connections.iterator().next();
		connections.remove(result);
		return result;
	}

}
