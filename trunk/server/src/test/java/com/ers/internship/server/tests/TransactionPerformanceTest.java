package com.ers.internship.server.tests;

import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ers.internship.transaction.Transaction;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class TransactionPerformanceTest extends EntityPerformanceTest<String, Transaction> {
	private static String trID = "trID";
	
	@BeforeClass
	public static void beforeClassTransaction(){
		CREATE_URL = "/transaction/create/";
		UPDATE_URL = "/transaction/update/";
		DELETE_URL = "/transaction/delete/" + trID;
		LOAD_URL = "/transaction/loadById/" + trID;
		
		CREATE_MSG = "POST Transaction CREATE ";
		UPDATE_MSG = "POST Transaction UPDATE ";
		DELETE_MSG = "DELETE Transaction DELETE ";
		LOAD_MSG = "GET Transaction LOADED ";
		
		ENTITY_TABLE = "IDB_TRANSACTION";
		ENTITY_STATE_TABLE = "IDB_TRANSACTION_STATE";
	}
	@AfterClass
	public static void afterClassTransaction(){
		CREATE_URL = null;
		UPDATE_URL = null;
		DELETE_URL = null;
		LOAD_URL = null; 
		
		CREATE_MSG = null;
		UPDATE_MSG = null;
		DELETE_MSG = null;
		LOAD_MSG = null;
		
		ENTITY_TABLE = null;
		ENTITY_STATE_TABLE = null;
		
		trID = null;
	}

	@Before
	public void beforeTrTest(){
		System.out.println("beforeTest");
		deleteEntityInDB();
	}
	@After
	public void afterTrTest(){
		System.out.println("afterTest");
		deleteEntityInDB();
	}
	@Override
	protected String getPrimaryKey() {
		String pKey = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM " + ENTITY_TABLE + " WHERE transaction_id=?");
		try(PreparedStatement stmt = persistentStore.getConnection().prepareStatement(query.toString())) {
			stmt.setString(1, trID);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				pKey = rs.getString("id");
			}
		} catch (SQLException e) {
			fail("Fail select " + e.getMessage());
		}
		return pKey;
	}
	@Override
	protected Transaction createEntity() {
		Transaction tr = new Transaction(trID);
		tr.setCurrency("EUR");
		tr.setName("trName");
		tr.setPaidAmount(500);
		tr.setPositionId("posID");
		tr.setReceiver("rec");
		tr.setSender("sender");
		tr.setVolume(100);
		return tr;
	}

}
