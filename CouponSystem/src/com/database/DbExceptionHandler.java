package com.database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class DbExceptionHandler {
private static String fileName = "D:\\DBExceptionLogger.txt";
	
	public static void HandleException(Exception e) {
		logToFile(e);
		if(e instanceof CouponSystemException) {
			//Do something
		}
		if(e instanceof SQLException) {
//			System.out.println("SQL Exception. Inaal Rabak");
		}
		if(e instanceof InterruptedException) {
			
		}
		
	}

	private static void logToFile(Exception e) {
		String today = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());
		StringBuilder sb = new StringBuilder(today);
		sb.append(" : ").append(e.toString());
do {
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append("\n").append(element).append("\n");
		}
		e = (Exception)e.getCause();
		}
	while(e != null) ;

		String trace = sb.toString();
		Path file = Paths.get(fileName);
		if(Files.notExists(file, LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createFile(file );
			} catch (IOException e1) {
				System.out.println("IO Exception");
				System.out.println("Could not create file");
			}
		}
		try (FileWriter writer = new FileWriter(fileName,true);
				BufferedWriter bfWriter = new BufferedWriter(writer);){
			
			bfWriter.write(trace);
		} catch (IOException e1) {
			System.out.println("IO Exception");
			System.out.println("Could not log exception ");
		}
	}
}
