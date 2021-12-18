package com.ers.internship.jdbc.test;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.market.FxQuote;
import com.ers.internship.identificators.FxQuoteId;
import org.junit.Ignore;

/**
 * @author Snejina Yanakieva
 */
@Ignore
public class FxQuoteDaoImplTest {

	private static JdbcPersistentStore jps;
	private FxQuote testFxQuoteSave = new FxQuote();
	static FxQuoteId id = new FxQuoteId("USD", "BGN", new GregorianCalendar(2014, 5, 5));

	@BeforeClass
	public static void createDatabase() {
		jps = new JdbcPersistentStore("jdbc:hsqldb:mem:Historized2B", "username", "password");

		jps.startTransaction();
		jps.createDB();
		jps.commitTransaction();

	}

	@AfterClass
	public static void closePs() {
		try {
			jps.dropDB();
			jps.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@After
	public void clearAfter() {
		try {
			deleteTestRecordsFromDB();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Before
	public void clearBefore() {
		try {
			deleteTestRecordsFromDB();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void deleteTestRecordsFromDB() {
		jps.startTransaction();

		if (selectEntityId() != null) {
			deleteFxQUote("IDB_FxQuote_state", "entity_id");
			deleteFxQUote("IDB_FxQuote", "id");
		}
		jps.commitTransaction();
	}

	private void deleteFxQUote(String table, String column) {
		String sql = "Delete FROM " + table + " WHERE " + column + " = ?";
		try(			PreparedStatement stmt = jps.getConnection().prepareStatement(sql)) {

			stmt.setString(1, selectEntityId());
			stmt.executeUpdate();
			jps.commitTransaction();
			stmt.close();
		} catch (SQLException e) {

			fail("Records are not deleted");
		}
	}

	private String selectEntityId() {
		jps.startTransaction();
		String eId = new String();
		String sql = "Select id FROM IDB_FxQuote WHERE from_currency = ? and to_currency = ? and at_date = ?";
		try (PreparedStatement stmt = jps.getConnection().prepareStatement(sql)){
			stmt.setString(1, id.getFromCurrency());
			stmt.setString(2, id.getToCurrency());
			stmt.setTimestamp(3, new Timestamp(id.getAtDate().getTimeInMillis()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				eId = rs.getString("id");
			}
			stmt.close();
			jps.commitTransaction();

		} catch (SQLException e) {
			fail("Id not selected");
		}
		return eId;
	}

	public void saveIntoDatabase() {
		testFxQuoteSave = new FxQuote();
		testFxQuoteSave.setID(id);
		testFxQuoteSave.setValue(500);

		jps.startTransaction();
		jps.getFxQuoteDao().save(testFxQuoteSave);
		jps.commitTransaction();
	}

	public FxQuote loadFromDatabase() {
		FxQuote testFxQuoteLoad = new FxQuote();

		jps.startTransaction();
		testFxQuoteLoad = jps.getFxQuoteDao().loadById(id, new GregorianCalendar(2016, 5, 5));
		jps.commitTransaction();

		return testFxQuoteLoad;
	}

	public FxQuote getLatestFxQuote() {
		FxQuote testFxQuoteLoad = new FxQuote();

		jps.startTransaction();
		testFxQuoteLoad = jps.getFxQuoteDao().loadLatest("USD", "BGN", new GregorianCalendar(2014, 5, 5));
		jps.commitTransaction();

		return testFxQuoteLoad;
	}

	public void deleteFxQuoteFromDatabase() {

		jps.startTransaction();
		jps.getFxQuoteDao().delete(id);
		jps.commitTransaction();

	}

	@Test
	public void testSaveLoadFxQuote() {

		saveIntoDatabase();
		FxQuote testFxQuote = loadFromDatabase();

		assertNotNull("FxQUote is not loaded from database.", testFxQuote);
		assertEquals("ID is not the same.", testFxQuoteSave.getID(), testFxQuote.getID());
		assertEquals("Base currency is not the same.", testFxQuoteSave.getBaseCurrency(),
				testFxQuote.getBaseCurrency());
		assertEquals("Target currency is not the same.", testFxQuoteSave.getTargetCurrency(),
				testFxQuote.getTargetCurrency());
		assertEquals("Target currency is not the same.", testFxQuoteSave.getDate(), testFxQuote.getDate());
		assertTrue("Value is not the same.", testFxQuoteSave.getValue() == testFxQuote.getValue());

	}

	@Test
	public void testDeleteFxQuote() {

		saveIntoDatabase();
		FxQuote testFxQuote = loadFromDatabase();

		assertNotNull("FxQUote is not loaded from database.", testFxQuote);

		deleteFxQuoteFromDatabase();
		testFxQuote = loadFromDatabase();

		assertNull("FxQUote was not deleted from database.", testFxQuote);

	}

	@Test
	public void testLoadLatest() {

		FxQuote testListFxQuote = new FxQuote();

		saveIntoDatabase();
		FxQuote testFxQuote = loadFromDatabase();
		assertNotNull("FxQUote is not loaded from database.", testFxQuote);

		saveIntoDatabase();
		testFxQuote = loadFromDatabase();
		assertNotNull("FxQUote is not loaded from database.", testFxQuote);

		testListFxQuote = getLatestFxQuote();

		assertNotNull("List FxQUote was not loaded from database.", testListFxQuote);
		assertEquals("Records are not the same.", testListFxQuote.getBaseCurrency(), "USD");
		assertEquals("Records are not the same.", testListFxQuote.getTargetCurrency(), "BGN");

	}

	@Test
	public void testHistorization() {
		testFxQuoteSave = new FxQuote();
		testFxQuoteSave.setID(id);
		testFxQuoteSave.setValue(110);
		jps.startTransaction();
		jps.getFxQuoteDao().save(testFxQuoteSave);
		jps.commitTransaction();
		GregorianCalendar time = new GregorianCalendar();
		saveIntoDatabase();

		FxQuote testFxQuote = new FxQuote();

		jps.startTransaction();
		testFxQuote = jps.getFxQuoteDao().loadById(id, time);
		jps.commitTransaction();

		assertNotNull("FxQuote is not loaded from database.", testFxQuote);
		assertEquals("ID is not the same.", testFxQuoteSave.getID(), testFxQuote.getID());
		assertEquals("Name is not the same.", 110, testFxQuote.getValue(), 0.1);

		testFxQuote = loadFromDatabase();

		assertNotNull("FxQuote is not loaded from database.", testFxQuote);
		assertEquals("ID is not the same.", testFxQuoteSave.getID(), testFxQuote.getID());
		assertEquals("Name is not the same.", testFxQuoteSave.getValue(), testFxQuote.getValue(), 0.1);
	}
}
