package com.database.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import com.sys.beans.Company;
/**
 * 
 * @authors Yaniv Chen & Gil Gouetta
 * @deprecated
 *
 */
public class RandomGenerator {
private static Random rnd = new Random();
private static String url = "jdbc:derby://localhost:1527/random";
private static PreparedStatement read = null;

static {
	try(Connection con = DriverManager.getConnection(url)){
		String sql = "select * from company where id = ?";
		read = con.prepareStatement(sql);
		
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
}

public static Company generateCompany()  {
	Company result = null;
	try(Connection con = DriverManager.getConnection(url);
			Statement stmt = con.createStatement()){
			String sql = "select count(id) from company";
	ResultSet rs = stmt.executeQuery(sql);
	if(rs.next()) {
	int numberOfCompanies = rs.getInt("id");
	int random = rnd.nextInt(numberOfCompanies+1);
	read.setInt(1, random);
	rs = read.executeQuery();
	
	}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return result;
}


}
