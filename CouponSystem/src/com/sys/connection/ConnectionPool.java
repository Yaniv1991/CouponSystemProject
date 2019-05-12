package com.sys.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.sys.exception.ConnectionException;

/**
 * {@code ConnectionPool}</br></br>
 * Singleton class that creates a connection pool to use throughout the coupon system.
 * @authors Yaniv Chen & Gil Gouetta.
 *
 */
public class ConnectionPool {

	private static final int MAX_CONNECTIONS = 10;
	private static ConnectionPool instance;
	private Set<Connection> connections = new HashSet<>();
	private String url = "jdbc:derby://localhost:1527/CouponSystemDb";
	private boolean poolIsClosing = false;

/**
 * Private constructor for the singleton class {@code ConnectionPool} that creaes a dedicated number of connections.	
 * @throws ConnectionException
 */
	
	private ConnectionPool() throws ConnectionException {
		// add 10 connections to the set.
		while (connections.size() < MAX_CONNECTIONS) {
			try {
				for (int i = 0; i < MAX_CONNECTIONS; i++) {
					connections.add(DriverManager.getConnection(url));
				}
			}

			catch (SQLException e) {
				throw new ConnectionException("Sql exception caused by Connection pool",e);
			}
		}
	}

/**
 * {@code getInstance}</br></br>
 * Used to return a connection instance to use for DAO objects.
 * @return One instance of {@code ConnectionPool}.
 * @throws ConnectionException
 */
	
	public static ConnectionPool getInstance() throws ConnectionException {
		while (instance == null) {
				instance = new ConnectionPool();
		}
		return instance;
	}

/**
 * {@code closeAllConnections}</br></br>
 * Closes all connections to the DB.</br>
 * Used on system shut-down.
 * @throws ConnectionException
 */

	public synchronized void closeAllConnections() throws ConnectionException {
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
				throw new ConnectionException("Exception raised in closing all connections", e);
			}
		}
	}

/**
 * {@code restoreConnection}</br></br>
 * Returns a {@code connection} to the pool.</br>	
 * @param connection
 */

	public synchronized void restoreConnection(Connection connection) {
		connections.add(connection);
		notify();
	}

/**
 * {@code getConnection}</br></br>	
 * Used to get one DB {@code connection} instance.
 * @return {@code connection} instance.
 * @throws ConnectionException
 */
	
	public synchronized Connection getConnection() throws ConnectionException {
		if (poolIsClosing) {
			throw new ConnectionException("Pool is closing");
		}
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new ConnectionException("Thread interrupted while getting a connection", e);

			}
		}
		Connection result = connections.iterator().next();
		connections.remove(result);
		return result;
	}

}
