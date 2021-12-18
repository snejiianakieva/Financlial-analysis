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

import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.portfolio.Portfolio;
import org.junit.Ignore;

/**
 * @author Snejina Yanakieva
 */
@Ignore
public class PortfolioDaoImplTest {
	private static JdbcPersistentStore jps;
	private Portfolio testPortfolioSave = new Portfolio();
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
			deletePortfolio("IDB_portfolio_state", "entity_id");
			deletePortfolio("IDB_portfolio", "id");
		}
		jps.commitTransaction();
	}

	private void deletePortfolio(String table, String column) {
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
		String sql = "Select id FROM IDB_Portfolio" + " WHERE portfolio_id = ?";

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
		testPortfolioSave = new Portfolio();
		testPortfolioSave.setID(id);
		testPortfolioSave.setName("001");
		testPortfolioSave.setCurrency("USD");

		jps.startTransaction();
		jps.getPortfolioDao().save(testPortfolioSave);
		jps.commitTransaction();
	}

	public Portfolio loadFromDatabase() {
		Portfolio testPortfolioLoad = new Portfolio();

		jps.startTransaction();
		testPortfolioLoad = jps.getPortfolioDao().loadById(id, new GregorianCalendar(2016, 5, 5));
		jps.commitTransaction();

		return testPortfolioLoad;
	}

	public List<Portfolio> searchPortfolioByName() {
		List<Portfolio> testPortfolioLoad = new ArrayList<Portfolio>();

		jps.startTransaction();
		testPortfolioLoad = jps.getPortfolioDao().searchByName("001");
		jps.commitTransaction();

		return testPortfolioLoad;
	}

	public void deletePortfolioFromDatabase() {

		jps.startTransaction();
		jps.getPortfolioDao().delete(id);
		jps.commitTransaction();
	}

	@Test
	public void testSaveLoadPortfolio() {

		saveIntoDatabase();
		Portfolio testPortfolio = loadFromDatabase();

		assertNotNull("Portfolio is not loaded from database.", testPortfolio);
		assertEquals("ID is not the same.", testPortfolioSave.getID(), testPortfolio.getID());
		assertEquals("Name is not the same.", testPortfolioSave.getName(), testPortfolio.getName());
		assertEquals("Currency is not the same.", testPortfolioSave.getCurrency(), testPortfolio.getCurrency());

	}

	@Test
	public void testDeletePortfolio() {

		saveIntoDatabase();
		Portfolio testPortfolio = loadFromDatabase();

		assertNotNull("Portfolio is not loaded from database.", testPortfolio);

		deletePortfolioFromDatabase();
	}

	@Test
	public void testSearchByName() {

		List<Portfolio> testListPortfolio = new ArrayList<Portfolio>();

		saveIntoDatabase();
		Portfolio testPortfolio = loadFromDatabase();
		assertNotNull("Portfolio is not loaded from database.", testPortfolio);

		saveIntoDatabase();
		testPortfolio = loadFromDatabase();
		assertNotNull("Portfolio is not loaded from database.", testPortfolio);

		testListPortfolio = searchPortfolioByName();

		assertNotNull("List Portfolio was not loaded from database.", testListPortfolio);
		assertEquals("Records are not the same.", testListPortfolio.get(0).getName(), "001");
		assertTrue("Records are not the same.", testListPortfolio.size() >= 2);

	}

	@Test
	public void testHistorization() {
		testPortfolioSave = new Portfolio();
		testPortfolioSave.setID(id);
		testPortfolioSave.setName("002");
		testPortfolioSave.setCurrency("BGN");

		jps.startTransaction();
		jps.getPortfolioDao().save(testPortfolioSave);
		jps.commitTransaction();
		GregorianCalendar time = new GregorianCalendar();
		saveIntoDatabase();

		Portfolio testPortfolio = new Portfolio();

		jps.startTransaction();
		testPortfolio = jps.getPortfolioDao().loadById(id, time);
		jps.commitTransaction();

		assertNotNull("Portfolio is not loaded from database.", testPortfolio);
		assertEquals("ID is not the same.", testPortfolioSave.getID(), testPortfolio.getID());
		assertEquals("Name is not the same.", "002", testPortfolio.getName());
		assertEquals("Currency is not the same.", "BGN", testPortfolio.getCurrency());

		testPortfolio = loadFromDatabase();

		assertNotNull("Portfolio is not loaded from database.", testPortfolio);
		assertEquals("ID is not the same.", testPortfolioSave.getID(), testPortfolio.getID());
		assertEquals("Name is not the same.", testPortfolioSave.getName(), testPortfolio.getName());
		assertEquals("Currency is not the same.", testPortfolioSave.getCurrency(), testPortfolio.getCurrency());
	}
}
