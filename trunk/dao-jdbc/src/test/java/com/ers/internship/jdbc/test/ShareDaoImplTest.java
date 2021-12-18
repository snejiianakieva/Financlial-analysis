package com.ers.internship.jdbc.test;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.jdbc.JdbcPersistentStore;
import org.junit.Ignore;

/**
 * @author Snejina Yanakieva
 */
@Ignore
public class ShareDaoImplTest {
	private static JdbcPersistentStore jps;
	private Share testInstrumentSave = new Share();
	static String id = new String();

	@BeforeClass
	public static void createDatabase() {
		jps = new JdbcPersistentStore("jdbc:hsqldb:mem:Historized2B", "username", "password");

		jps.startTransaction();
		jps.createDB();
		jps.commitTransaction();
		id = UUID.randomUUID().toString();

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
		jps = new JdbcPersistentStore("jdbc:hsqldb:mem:Historized2B", "username", "password");
		jps.startTransaction();

		if (selectEntityId() != null) {
			deleteinstrument("IDB_instrument_state", "entity_id");
			deleteinstrument("IDB_instrument", "id");
		}
		jps.commitTransaction();
	}

	private void deleteinstrument(String table, String column) {
		String sql = "Delete FROM " + table + " WHERE " + column + " = ?";
		try (PreparedStatement stmt = jps.getConnection().prepareStatement(sql)) {
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
		String sql = "Select id FROM IDB_Instrument" + " WHERE instrument_id = ?";
		try (PreparedStatement stmt = jps.getConnection().prepareStatement(sql)) {
			stmt.setString(1, id);
			ResultSet rs = null;
			rs = stmt.executeQuery();
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
		testInstrumentSave.setID(id);
		testInstrumentSave.setCurrency("USD");
		testInstrumentSave.setIsin("aaaa");
		testInstrumentSave.setMarket("bbb");
		jps.startTransaction();
		jps.getInstrumentDao().save(testInstrumentSave);
		jps.commitTransaction();
	}

	public Share loadFromDatabase() {
		Share testInstrumentLoad = new Share();

		jps.startTransaction();
		testInstrumentLoad = (Share) jps.getInstrumentDao().loadById(id, new GregorianCalendar(2016, 5, 5));
		jps.commitTransaction();

		return testInstrumentLoad;
	}

	public List<Instrument> searchInstrumentByIsin() {
		List<Instrument> testInstrumentLoad = new ArrayList<Instrument>();

		jps.startTransaction();
		testInstrumentLoad = jps.getInstrumentDao().searchByName("aaaa");
		jps.commitTransaction();

		return testInstrumentLoad;
	}

	public void deleteInstrumentFromDatabase() {

		jps.startTransaction();
		jps.getInstrumentDao().delete(id);
		jps.commitTransaction();
	}

	@Test
	public void testSaveLoadInstrument() {

		saveIntoDatabase();
		Share testInstrument = loadFromDatabase();

		assertNotNull("Instrument is not loaded from database.", testInstrument);
		assertEquals("ID is not the same.", testInstrumentSave.getID(), testInstrument.getID());
		assertEquals("Currency is not the same.", testInstrumentSave.getCurrency(), testInstrument.getCurrency());
		assertEquals("Isin is not the same.", testInstrumentSave.getIsin(), testInstrument.getIsin());

	}

	@Test
	public void testDeleteInstrument() {

		saveIntoDatabase();
		Instrument testInstrument = loadFromDatabase();

		assertNotNull("Instrument is not loaded from database.", testInstrument);

		deleteInstrumentFromDatabase();
		testInstrument = loadFromDatabase();
		assertNull("Instrument is not deleted from database.", testInstrument);

	}

	@Test
	public void testSearchByIsin() {

		List<Instrument> testListInstrument = new ArrayList<Instrument>();

		saveIntoDatabase();
		Instrument testInstrument = loadFromDatabase();
		assertNotNull("Instrument is not loaded from database.", testInstrument);

		saveIntoDatabase();
		testInstrument = loadFromDatabase();
		assertNotNull("Instrument is not loaded from database.", testInstrument);

		testListInstrument = searchInstrumentByIsin();

		assertNotNull("List Instrument was not loaded from database.", testListInstrument);
		assertTrue("Records are not the same.", testListInstrument.size() >= 2);

	}

	@Test
	public void testHistorization() {
		testInstrumentSave = new Share();
		testInstrumentSave.setID(id);
		testInstrumentSave.setIsin("002");
		testInstrumentSave.setCurrency("BGN");
		testInstrumentSave.setMarket("qqq");

		jps.startTransaction();
		jps.getInstrumentDao().save(testInstrumentSave);
		jps.commitTransaction();
		GregorianCalendar time = new GregorianCalendar();
		saveIntoDatabase();

		Share testInstrument = new Share();

		jps.startTransaction();
		testInstrument = (Share) jps.getInstrumentDao().loadById(id, time);
		jps.commitTransaction();

		assertNotNull("Instrument is not loaded from database.", testInstrument);
		assertEquals("ID is not the same.", testInstrumentSave.getID(), testInstrument.getID());
		assertEquals("Isin is not the same.", "002", testInstrument.getIsin());
		assertEquals("Currency is not the same.", "BGN", testInstrument.getCurrency());

		testInstrument = loadFromDatabase();

		assertNotNull("Instrument is not loaded from database.", testInstrument);
		assertEquals("ID is not the same.", testInstrumentSave.getID(), testInstrument.getID());
		assertEquals("Isin is not the same.", testInstrumentSave.getIsin(), testInstrument.getIsin());
		assertEquals("Currency is not the same.", testInstrumentSave.getCurrency(), testInstrument.getCurrency());
	}
}