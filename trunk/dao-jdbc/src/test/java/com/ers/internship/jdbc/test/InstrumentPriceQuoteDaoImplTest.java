
package com.ers.internship.jdbc.test;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.jdbc.InstrumentPriceQuoteDaoImpl;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import org.junit.Ignore;

/**
 *
 * @author Snejina Yanakieva
 *
 * 
 */
@Ignore
public class InstrumentPriceQuoteDaoImplTest {
	private static JdbcPersistentStore store;
	private InstrumentPriceQuoteDaoImpl testPriceQuoteImpl;
	private InstrumentPriceQuoteId id = new InstrumentPriceQuoteId("abcd", new GregorianCalendar(2011, 5, 5));

	@BeforeClass
	public static void beforeClass() throws Exception {
		store = new JdbcPersistentStore("jdbc:hsqldb:mem:target/HistorizedDB", "username", "password");
		store.startTransaction();
		store.createDB();
		store.commitTransaction();
		store.startTransaction();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		store.startTransaction();
		store.dropDB();
		store.commitTransaction();
		store.close();
	}

	@Before
	public void before() {
		store.startTransaction();
		String loadedId = getIdPrimaryKey("IDB_InstrumentPriceQuote", id);
		deleteEntityInDBTable("IDB_InstrumentPriceQuote_STATE", "entity_id", loadedId);
		deleteEntityInDBTable("IDB_InstrumentPriceQuote", "id", loadedId);
		store.commitTransaction();
		testPriceQuoteImpl = new InstrumentPriceQuoteDaoImpl(store);

	}

	@After
	public void after() {
		store.startTransaction();
		String loadedId = getIdPrimaryKey("IDB_InstrumentPriceQuote", id);
		deleteEntityInDBTable("IDB_InstrumentPriceQuote_STATE", "entity_id", loadedId);
		deleteEntityInDBTable("IDB_InstrumentPriceQuote", "id", loadedId);
		store.commitTransaction();
		testPriceQuoteImpl = null;
	}

	@Test
	public final void testSaveInstrumentPriceQuote() {
		store.startTransaction();
		InstrumentPriceQuote instrumentPriceQuote = createInstrumentPriceQuote(id);
		testPriceQuoteImpl.save(instrumentPriceQuote);

		Calendar date = new GregorianCalendar(2050, 5, 5);
		InstrumentPriceQuote loadedPriceQuote = null;

		loadedPriceQuote = testPriceQuoteImpl.loadById(id, date);

		assertNotNull("Should be not null! save method", loadedPriceQuote);
		assertEqualsEntities(instrumentPriceQuote, loadedPriceQuote);
		store.commitTransaction();
	}

	@Test
	public final void testDeleteString() {
		store.startTransaction();
		InstrumentPriceQuote instrumentPriceQuote = createInstrumentPriceQuote(id);
		testPriceQuoteImpl.save(instrumentPriceQuote);

		Calendar date = new GregorianCalendar();
		InstrumentPriceQuote loadedPriceQuote = null;
		loadedPriceQuote = testPriceQuoteImpl.loadById(id, date);

		assertNotNull("Should be not null", loadedPriceQuote);
		assertEqualsEntities(instrumentPriceQuote, loadedPriceQuote);

		date = new GregorianCalendar();
		testPriceQuoteImpl.delete(id);

		InstrumentPriceQuote loadedPriceQuoteAfterUpdate = null;
		loadedPriceQuoteAfterUpdate = testPriceQuoteImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedPriceQuoteAfterUpdate);
		assertEqualsEntities(instrumentPriceQuote, loadedPriceQuoteAfterUpdate);
		store.commitTransaction();

	}

	@Test
	public void testLoadByIdStringCalendar() {
		store.startTransaction();
		InstrumentPriceQuote instrumentPriceQuote = createInstrumentPriceQuote(id);
		testPriceQuoteImpl.save(instrumentPriceQuote);
		Calendar date = new GregorianCalendar();
		InstrumentPriceQuote loadedPriceQuote = null;
		loadedPriceQuote = testPriceQuoteImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedPriceQuote);
		assertEqualsEntities(instrumentPriceQuote, loadedPriceQuote);
		store.commitTransaction();
	}

	@Test
	public void testLoadLatestPriceInstrumentPriceQuote() {
		store.startTransaction();

		InstrumentPriceQuote instrumentPriceQuote1 = createInstrumentPriceQuote(id);
		testPriceQuoteImpl.save(instrumentPriceQuote1);
		InstrumentPriceQuote loadedPriceQuote = null;
		Calendar date = new GregorianCalendar();
		loadedPriceQuote = testPriceQuoteImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedPriceQuote);
		assertEqualsEntities(instrumentPriceQuote1, loadedPriceQuote);

		loadedPriceQuote = testPriceQuoteImpl.loadLatestPrice("abcd", new GregorianCalendar(2011,5,5));
		assertNotNull("Should be not null", loadedPriceQuote);
		assertEqualsEntities(instrumentPriceQuote1, loadedPriceQuote);
		store.commitTransaction();
	}
	@Test
	public void testHistorization() {
		store.startTransaction();
		// first price quote
		InstrumentPriceQuote instrumentPriceQuote1= createInstrumentPriceQuote(id);
		testPriceQuoteImpl.save(instrumentPriceQuote1);
		Calendar date = new GregorianCalendar();
		// second price quote
		InstrumentPriceQuote instrumentPriceQuote2= createInstrumentPriceQuote(id);
		testPriceQuoteImpl.save(instrumentPriceQuote2);
		Calendar date2 = new GregorianCalendar(2050, 5, 5);
		//
		InstrumentPriceQuote loadedPriceQuote = testPriceQuoteImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedPriceQuote);
		assertEqualsEntities(instrumentPriceQuote1, loadedPriceQuote);
		assertEqualsEntities(instrumentPriceQuote2, loadedPriceQuote);
		
		InstrumentPriceQuote loadedPriceQuote2 = testPriceQuoteImpl.loadById(id, date2);
		assertNotNull("Should be not null", loadedPriceQuote);
		assertEqualsEntities(loadedPriceQuote2, loadedPriceQuote);
		store.commitTransaction();

	}
	protected InstrumentPriceQuote createInstrumentPriceQuote(InstrumentPriceQuoteId id) {
		InstrumentPriceQuote instrumentPriceQuote = new InstrumentPriceQuote();
		instrumentPriceQuote.setID(id);
		instrumentPriceQuote.setCurrency("BGN");
		instrumentPriceQuote.setInstrumentPrice(24.55);
		return instrumentPriceQuote;
	}

	protected void deleteEntityInDBTable(String nameTable, String columnName, String id) {
		String deleteQuery = "DELETE FROM " + nameTable + " WHERE " + columnName + "=?";
		try(PreparedStatement stmt = store.getConnection().prepareStatement(deleteQuery)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			fail("Fail delete");
		}
	}

	protected String getIdPrimaryKey(String nameTable, InstrumentPriceQuoteId id) {
		String idPKey = null;
		String selectQuery = "SELECT id FROM " + nameTable + " WHERE instrument_id=? AND at_date=?";
		try(PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery)) {
			stmt.setString(1, id.getInstrumentId());
			stmt.setTimestamp(2, new Timestamp(id.getAtDate().getTimeInMillis()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				idPKey = rs.getString("id");
			}
			stmt.close();
		} catch (SQLException e) {
			fail("Fail select");
		}
		return idPKey;
	}

	protected void assertEqualsEntities(InstrumentPriceQuote priceQuote1, InstrumentPriceQuote priceQuote2) {
		assertEquals("Not equals by id the entities!", priceQuote1.getID(), priceQuote2.getID());
		assertEquals("Not equals by instrument id the entities!", priceQuote1.getInstrumentId(),
				priceQuote2.getInstrumentId());
		assertEquals("Not equals by currency the entities!", priceQuote1.getCurrency(), priceQuote2.getCurrency());
		assertEquals("Not equals by date the entities!", priceQuote1.getDate(), priceQuote2.getDate());
		assertEquals("Not equals by instrument price the entities!", priceQuote1.getInstrumentPrice(),
				priceQuote2.getInstrumentPrice(), 0.1);

	}
}
