package com.sys;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sys.exception.CouponSystemException;

public class DbExceptionHandler {
	private static String fileName = "DBExceptionLogger";
	private static String filePath = "D:\\Exceptions\\";
	private static String fileExtension = ".txt";


	public static void HandleException(Exception e)  {
		logToFile(e);
	String message = null;
		if (e instanceof SQLException) {
//			System.out.println("SQL Exception. Inaal Rabak");
			message = "SQL exception";
		}
		if (e instanceof InterruptedException) {
			message = "Interruped by " + e.getClass();
		}
		if(e instanceof ParseException) {
			
		}

	}

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
