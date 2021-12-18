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
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.transaction.Transaction;
import org.junit.Ignore;

/**
 * @author Snejina Yanakieva
 */
@Ignore
public class TransactionDaoImplTest {
	private static JdbcPersistentStore jps;
	private Transaction testTransactionSave = new Transaction();
	static String id = new String();

	@BeforeClass
	public static void createDatabase() {
		jps = new JdbcPersistentStore("jdbc:hsqldb:mem:Historized2B", 
				"username", "password");

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
			deleteTransaction("IDB_Transaction_state", "entity_id");
			deleteTransaction("IDB_Transaction", "id");
		}
		jps.commitTransaction();
	}

	private void deleteTransaction(String table, String column) {
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
		String sql = "Select id FROM IDB_Transaction" + " WHERE Transaction_id = ?";
		try (PreparedStatement stmt = jps.getConnection().prepareStatement(sql)) {
			stmt.setString(1, id);
			ResultSet rs = null;
			rs = stmt.executeQuery();
			if (rs.next()) {
				eId = rs.getString("id");
			}
			jps.commitTransaction();
			stmt.close();
		} catch (SQLException e) {
			fail("Id not selected");
		}
		return eId;
	}

	public void saveIntoDatabase() {
		testTransactionSave = new Transaction();
		testTransactionSave.setID(id);
		testTransactionSave.setName("001");
		testTransactionSave.setVolume(1000);
		testTransactionSave.setPaidAmount(600);
		testTransactionSave.setCurrency("USD");
		testTransactionSave.setSender("a");
		testTransactionSave.setReceiver("b");
		testTransactionSave.setPositionId("abc");
		jps.startTransaction();
		jps.getTransactionDao().save(testTransactionSave);
		jps.commitTransaction();

	}

	public Transaction loadFromDatabase() {
		Transaction testTransactionLoad = new Transaction();

		jps.startTransaction();
		testTransactionLoad = jps.getTransactionDao().loadById(id, new GregorianCalendar(2016, 5, 5));
		jps.commitTransaction();

		return testTransactionLoad;
	}

	public List<Transaction> searchTransactionByName() {
		List<Transaction> testTransactionLoad = new ArrayList<Transaction>();

		jps.startTransaction();
		testTransactionLoad = jps.getTransactionDao().searchByName("001");
		jps.commitTransaction();

		return testTransactionLoad;
	}

	public void deleteTransactionFromDatabase() {

		jps.startTransaction();
		jps.getTransactionDao().delete(id);
		jps.commitTransaction();
	}

	public void savePortfolio() {
		Portfolio testPortfolioSave = new Portfolio();
		testPortfolioSave.setID("p1");
		testPortfolioSave.setName("001");
		testPortfolioSave.setCurrency("USD");

		jps.startTransaction();
		jps.getPortfolioDao().save(testPortfolioSave);
		jps.commitTransaction();
	}

	public void savePosition() {
		Position testPositionSave = new Position();
		testPositionSave.setID("pos1");
		testPositionSave.setName("001");
		testPositionSave.setInstrument(saveInstrument());
		testPositionSave.setLongSide("c");
		testPositionSave.setShortSide("d");
		testPositionSave.setPortfolioId("p1");
		jps.startTransaction();
		jps.getPositionDao().save(testPositionSave);
		jps.commitTransaction();
	}

	public Instrument saveInstrument() {
		Share testShareSave = new Share();
		testShareSave.setID("i1");
		testShareSave.setIsin("001");
		testShareSave.setCurrency("USD");
		testShareSave.setMarket("a");

		jps.startTransaction();
		jps.getInstrumentDao().save(testShareSave);
		jps.commitTransaction();
		return testShareSave;
	}

	@Test
	public void testLoadPortfolioTransaction() {
		savePosition();
		savePortfolio();
		List<Transaction> testListTransaction = new ArrayList<Transaction>();
		testTransactionSave = new Transaction();
		testTransactionSave.setID("testLoadPortfolio");
		testTransactionSave.setName("005");
		testTransactionSave.setVolume(1020);
		testTransactionSave.setPaidAmount(900);
		testTransactionSave.setCurrency("USD");
		testTransactionSave.setSender("c");
		testTransactionSave.setReceiver("d");
		testTransactionSave.setPositionId("pos1");
		jps.startTransaction();
		jps.getTransactionDao().save(testTransactionSave);
		jps.commitTransaction();

		jps.startTransaction();
		testListTransaction = jps.getTransactionDao().loadPortfolioTransactions("p1",
				new GregorianCalendar(2020, 10, 10));
		jps.commitTransaction();

		assertNotNull("List Transaction was not loaded from database.", testListTransaction);
		assertEquals("Records are not the same.", testListTransaction.get(0).getName(), testTransactionSave.getName());
		assertEquals("Records are not the same.", testListTransaction.get(0).getID(), testTransactionSave.getID());
		assertTrue("Records are not the same.", testListTransaction.size() >= 1);
	}

	@Test
	public void testSaveLoadTransaction() {

		saveIntoDatabase();
		Transaction testTransaction = loadFromDatabase();
		assertNotNull("Transaction is not loaded from database.", testTransaction);
		assertEquals("ID is not the same.", testTransactionSave.getID(), testTransaction.getID());
		assertEquals("Name is not the same.", testTransactionSave.getName(), testTransaction.getName());
		assertEquals("Currency is not the same.", testTransactionSave.getCurrency(), testTransaction.getCurrency());

	}

	@Test
	public void testDeleteTransaction() {

		saveIntoDatabase();
		Transaction testTransaction = loadFromDatabase();

		assertNotNull("Transaction is not loaded from database.", testTransaction);

		deleteTransactionFromDatabase();
		testTransaction = loadFromDatabase();

		assertNull("Transaction was not deleted from database.", testTransaction);

	}

	@Test
	public void testSearchByName() {

		List<Transaction> testListTransaction = new ArrayList<Transaction>();

		saveIntoDatabase();
		Transaction testTransaction = loadFromDatabase();
		assertNotNull("Transaction is not loaded from database.", testTransaction);

		saveIntoDatabase();
		testTransaction = loadFromDatabase();
		assertNotNull("Transaction is not loaded from database.", testTransaction);

		testListTransaction = searchTransactionByName();

		assertNotNull("List Transaction was not loaded from database.", testListTransaction);
		assertEquals("Records are not the same.", testListTransaction.get(0).getName(), "001");
		assertTrue("Records are not the same.", testListTransaction.size() >= 2);

	}

	@Test
	public void testHistorization() {
		testTransactionSave = new Transaction();
		testTransactionSave.setID(id);
		testTransactionSave.setName("002");
		testTransactionSave.setVolume(1300);
		testTransactionSave.setPaidAmount(700);
		testTransactionSave.setCurrency("BGN");
		testTransactionSave.setSender("c");
		testTransactionSave.setReceiver("d");
		testTransactionSave.setPositionId("abc");
		jps.startTransaction();
		jps.getTransactionDao().save(testTransactionSave);
		jps.commitTransaction();
		GregorianCalendar time = new GregorianCalendar();
		saveIntoDatabase();

		Transaction testTransaction = new Transaction();

		jps.startTransaction();
		testTransaction = jps.getTransactionDao().loadById(id, time);
		jps.commitTransaction();

		assertNotNull("Transaction is not loaded from database.", testTransaction);
		assertEquals("ID is not the same.", testTransactionSave.getID(), testTransaction.getID());
		assertEquals("Name is not the same.", "002", testTransaction.getName());
		assertEquals("Currency is not the same.", "BGN", testTransaction.getCurrency());

		testTransaction = loadFromDatabase();

		assertNotNull("Transaction is not loaded from database.", testTransaction);
		assertEquals("ID is not the same.", testTransactionSave.getID(), testTransaction.getID());
		assertEquals("Name is not the same.", testTransactionSave.getName(), testTransaction.getName());
		assertEquals("Currency is not the same.", testTransactionSave.getCurrency(), testTransaction.getCurrency());
	}
}