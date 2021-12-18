package com.ers.internship.jdbc;

import org.hsqldb.jdbc.JDBCDataSource;

import com.ers.internship.persistentstore.PersistentStore;

/**
 * @author Snejina Yanakieva
 * 
 */
public class JdbcPersistentStoreFactory {

	public PersistentStore make(String dbUrl, String driver, String user, String pass) {
		if ("org.hsqldb.jdbc.JDBCDriver".equals(driver)) {
			return new JdbcPersistentStore(dbUrl, user, pass);
		}
		return null;
	}

	public PersistentStore make(JDBCDataSource ds, String user, String pass) {
		return new JdbcPersistentStore(ds, user, pass);
	}
}
