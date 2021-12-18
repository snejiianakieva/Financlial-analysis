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

import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.market.InstrumentPriceQuote;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class InstrumentPriceQuotePerformanceTest extends EntityPerformanceTest<InstrumentPriceQuoteId, InstrumentPriceQuote> {
	private static InstrumentPriceQuoteId pcId = new InstrumentPriceQuoteId("instrID", new GregorianCalendar(2015, 5, 6));
	@BeforeClass
	public static void beforeClassInstrumentPriceQuote(){
		CREATE_URL = "/instrumentPriceQuote/create/";
		UPDATE_URL = "/instrumentPriceQuote/update/";
		DELETE_URL = "/instrumentPriceQuote/delete/" + pcId.getInstrumentId() + "/" + pcId.getAtDate().getTimeInMillis();
		LOAD_URL = "/instrumentPriceQuote/loadById/" + pcId.getInstrumentId() + "/" + pcId.getAtDate().getTimeInMillis();
		
		CREATE_MSG = "POST InstrumentPriceQuote CREATE ";
		UPDATE_MSG = "POST InstrumentPriceQuote UPDATE ";
		DELETE_MSG = "DELETE InstrumentPriceQuote DELETE ";
		LOAD_MSG = "GET InstrumentPriceQuote LOADED ";

		ENTITY_TABLE = "IDB_InstrumentPriceQuote";
		ENTITY_STATE_TABLE = "IDB_InstrumentPriceQuote_STATE";
	}
	@AfterClass
	public static void afterClassInstrumentPriceQuote(){
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
		
		pcId = null;
	}
	@Before
	public void beforeInstrPQTest(){
		System.out.println("beforeTest");
		deleteEntityInDB();
	}
	@After
	public void afterInstrPQTest(){
		System.out.println("afterTest");
		deleteEntityInDB();
	}
	@Override
	protected InstrumentPriceQuote createEntity() {
		InstrumentPriceQuote ipq = new InstrumentPriceQuote(pcId);
		ipq.setCurrency("BGN");
		ipq.setInstrumentPrice(10);
		return ipq;
	}
	@Override
	protected String getPrimaryKey() {
		String pKey = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM " + ENTITY_TABLE + " WHERE instrument_id=? AND  at_date=?");
		try(PreparedStatement stmt = persistentStore.getConnection().prepareStatement(query.toString())) {
			stmt.setString(1, pcId.getInstrumentId());
			stmt.setTimestamp(2, new Timestamp(pcId.getAtDate().getTimeInMillis()));
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
