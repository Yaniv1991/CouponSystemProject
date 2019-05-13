package com.database.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Exception handler class for DB interactions.
 * @authors Yaniv Chen & Gil Gouetta.
 *
 */
public class DbExceptionHandler {
	private static String fileName = "DBExceptionLogger";
	private static String filePath = "D:\\Exceptions\\";
	private static String fileExtension = ".txt";

/**
 * 
 * {@code HandleException}</br>
 * </br>
 * Using {@link #logToFile(Exception) logToFile} method to log the exception, this method handles exceptions in the coupon system.
 * 
 * @param e - the exception to handle.
 *
 */
	public static void HandleException(Exception e)  {
		logToFile(e);
		if (e instanceof SQLException) {
//			System.out.println("SQL Exception. Inaal Rabak");
		}
		if (e instanceof InterruptedException) {
		}
		if(e instanceof ParseException) {
			
		}

	}
/**
 * 
 * {@code LogToFile} </br>
 * </br>
 * Creates a log file for DB Exceptions, using the exception.
 * 
 * @param e - the exception to log
 * 
 */
	private static void logToFile(Exception e) {
		String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
		StringBuilder sb = new StringBuilder(now);
		sb.append(" : ").append(e.toString());
		do {
			for (StackTraceElement element : e.getStackTrace()) {
				sb.append("\n").append(element).append("\n");
			}
			e = (Exception) e.getCause();
		} while (e != null);
		sb.append("\n");
		String trace = sb.toString();
		File directory = new File(filePath);
		File file = new File(
				filePath + fileName + " " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + fileExtension);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				System.out.println("Could not create file");
			}
		}
		try (FileWriter writer = new FileWriter(file, true);
				BufferedWriter bfWriter = new BufferedWriter(writer);) {
			bfWriter.write(trace);
		} catch (IOException e1) {
			System.out.println("IO Exception");
			System.out.println("Could not log exception ");
		}
	}
}
