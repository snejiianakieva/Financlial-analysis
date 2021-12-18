package com.ers.internship.server.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ers.internship.jdbc.JdbcPersistentStore;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class CreateTestDB {
	private static final String DB_USER = "dbUser";
	private static final String DB_PASS = "dbPass";
	private static final String DB_TYPE = "dbType";
	private static final String DB_NAME = "dbName";
	
	public static void main(String[] args) {
		CreateTestDB testDB = new CreateTestDB();
		System.out.println(testDB);
	}
	
	private JdbcPersistentStore jps;
	
	public CreateTestDB(){
		Properties property = new Properties();
	
	try (InputStream inputSteam = getClass().getClassLoader().getResourceAsStream("settings.properties")) {
		
		property.load(inputSteam);
		
		String dbUrl = String.format("jdbc:hsqldb:%s:db/%s", property.getProperty(DB_TYPE),
				property.getProperty(DB_NAME));
		jps = new JdbcPersistentStore(dbUrl, 
									 property.getProperty(DB_USER), 
									 property.getProperty(DB_PASS)
													  );
		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public void dropDB(){
		jps.startTransaction();
		jps.dropDB();
		jps.commitTransaction();
		
	}
	
	public void createDB(){
		jps.startTransaction();
		jps.dropDB();
		jps.createDB();
		jps.commitTransaction();
		
	}
}
