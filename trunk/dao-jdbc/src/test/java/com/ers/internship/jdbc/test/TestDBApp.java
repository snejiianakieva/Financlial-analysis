package com.ers.internship.jdbc.test;

import com.ers.internship.jdbc.JdbcPersistentStoreFactory;
import com.ers.internship.persistentstore.PersistentStore;

public class TestDBApp {

	public static void main(String[] args) {

		PersistentStore ps = new JdbcPersistentStoreFactory().make("jdbc:hsqldb:file:target\\HistorizedDB",
				"org.hsqldb.jdbc.JDBCDriver", "username", "password");
		ps.startTransaction();
		ps.dropDB();
		ps.createDB();
		ps.commitTransaction();

	}
}
