package com.ers.internship.server.tests;

import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.FxQuote;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class FxQuotePerformanceTest extends EntityPerformanceTest<FxQuoteId, FxQuote>{

	private static FxQuoteId fxID = new FxQuoteId("BGN","EUR",new GregorianCalendar(2015, 6, 6));
	@BeforeClass
	public static void beforeClassFxQuote(){
		CREATE_URL = "/fxQuote/create/";
		UPDATE_URL = "/fxQuote/update/";
		DELETE_URL = "/fxQuote/delete/"
				+ fxID.getFromCurrency() + "/" + fxID.getToCurrency() + "/" + fxID.getAtDate().getTimeInMillis();
		LOAD_URL = "/fxQuote/loadById/" 
				+ fxID.getFromCurrency() + "/" + fxID.getToCurrency() + "/" + fxID.getAtDate().getTimeInMillis();
		
		CREATE_MSG = "POST FxQuote CREATE ";
		UPDATE_MSG = "POST FxQuote UPDATE ";
		DELETE_MSG = "DELETE FxQuote DELETE ";
		LOAD_MSG = "GET FxQuote LOADED ";
		
		ENTITY_TABLE = "IDB_FxQuote";
		ENTITY_STATE_TABLE = "IDB_FxQuote_STATE";
	}
	@AfterClass
	public static void afterClassFxQuote(){
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
		fxID = null;
	}
	@Before
	public void beforeFxQTest(){
		System.out.println("beforeTest");
		deleteEntityInDB();
	}
	@After
	public void afterFxQTest(){
		System.out.println("afterTest");
		deleteEntityInDB();
	}
	@Override
	protected FxQuote createEntity() {
		FxQuote fx = new FxQuote(fxID);
		fx.setValue(1.2);
		return fx;
	}
	@Override
	protected String getPrimaryKey() {
		String pKey = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM " + ENTITY_TABLE + " WHERE from_currency=? AND to_currency=? AND at_date=?");
		try(PreparedStatement stmt = persistentStore.getConnection().prepareStatement(query.toString())) {
			stmt.setString(1, fxID.getFromCurrency());
			stmt.setString(2, fxID.getToCurrency());
			stmt.setTimestamp(3, new Timestamp(fxID.getAtDate().getTimeInMillis()));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				pKey = rs.getString("id");
			}
		} catch (SQLException e) {
			fail("Fail select " + e.getMessage());
		}
		return pKey;
	}
}
