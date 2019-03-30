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

public Set<Connection> getConnections() {
	return connections;
}

private ConnectionPool() {
	//add 10 connections to the set.
	try {
	for (int i = 0; i < MAX_CONNECTIONS; i++) {
		connections.add(DriverManager.getConnection(url));
	}
	}
	
	catch(SQLException e) {
		
	}
}

public static ConnectionPool getInstance() {
	return instance;
}

public void closeAllConnections() {
	try {
	for (Connection connection : connections) {
		connection.close();
	}}
	catch(Exception e) {
		
	}
}

public void restoreConnections(Connection connection) {
	
}

public Connection getConnection() throws SQLException {
	Connection result = null;
	for (Connection connection : connections) {
		if(connection.isClosed()) {
			result = connection;
		}
	}
	return result;
}

}
