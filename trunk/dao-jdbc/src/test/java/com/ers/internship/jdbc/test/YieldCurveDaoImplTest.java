
package com.ers.internship.jdbc.test;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.jdbc.YieldCurveDaoImpl;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.identificators.YieldCurveId;
import org.junit.Ignore;
/**
 * @author Snejina Yanakieva
 * 
 */
@Ignore
public class YieldCurveDaoImplTest {
	private static JdbcPersistentStore store;
	private YieldCurveDaoImpl testCurveImpl;
	private YieldCurveId id = new YieldCurveId("BGN", new GregorianCalendar(2011, 5, 5));

	@BeforeClass
	public static void beforeClass() throws Exception {
		store = new JdbcPersistentStore("jdbc:hsqldb:mem:target/HistorizedDB", "username", "password");
		store.startTransaction();
		store.createDB();
		store.commitTransaction();
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
		String loadedId = getIdPrimaryKey("IDB_YIELDCURVE", id);
		deleteEntityInDBTable("IDB_YIELDCURVE_STATE", "entity_id", loadedId);
		deleteEntityInDBTable("IDB_YIELDCURVE", "id", loadedId);
		testCurveImpl = new YieldCurveDaoImpl(store);
	}

	@After
	public void after() {
		String loadedId = getIdPrimaryKey("IDB_YIELDCURVE", id);
		deleteEntityInDBTable("IDB_YIELDCURVE_STATE", "entity_id", loadedId);
		deleteEntityInDBTable("IDB_YIELDCURVE", "id", loadedId);
		testCurveImpl = null;
	}

	@Test
	public final void testSaveYieldCurve() {
		store.startTransaction();
		YieldCurve yieldCurve = createYieldCurve(id);
		testCurveImpl.save(yieldCurve);
		Calendar date = new GregorianCalendar();
		YieldCurve loadedCurve = null;
		loadedCurve = testCurveImpl.loadById(id, date);
		assertNotNull("Should be not null! save method", loadedCurve);
		assertEqualsEntities(yieldCurve, loadedCurve);
		store.commitTransaction();
	}

	@Test
	public final void testDeleteString() {
		store.startTransaction();
		YieldCurve yieldCurve = createYieldCurve(id);
		testCurveImpl.save(yieldCurve);
		Calendar date = new GregorianCalendar();
		YieldCurve loadedCurve = null;
		loadedCurve = testCurveImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedCurve);
		assertEqualsEntities(yieldCurve, loadedCurve);

		date = new GregorianCalendar();
		testCurveImpl.delete(id);
		YieldCurve loadedCurveAfterUpdate = null;
		loadedCurveAfterUpdate = testCurveImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedCurveAfterUpdate);
		assertEqualsEntities(yieldCurve, loadedCurveAfterUpdate);
		store.commitTransaction();

	}

	@Test
	public void testLoadByIdStringCalendar() {
		store.startTransaction();
		YieldCurve yieldCurve = createYieldCurve(id);
		testCurveImpl.save(yieldCurve);
		Calendar date = new GregorianCalendar();
		YieldCurve loadedCurve = null;
		loadedCurve = testCurveImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedCurve);
		assertEqualsEntities(yieldCurve, loadedCurve);
		store.commitTransaction();

	}

	@Test
	public void loadLatestCurveYieldCurve() {
		store.startTransaction();
		YieldCurve yieldCurve = createYieldCurve(id);
		testCurveImpl.save(yieldCurve);
		Calendar date = new GregorianCalendar();
		YieldCurve loadedCurve = null;
		loadedCurve = testCurveImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedCurve);
		assertEqualsEntities(yieldCurve, loadedCurve);

		loadedCurve = testCurveImpl.loadLatestCurve("BGN", new GregorianCalendar(2011,5,5));
		assertNotNull("Should be not null", loadedCurve);
		assertEqualsEntities(yieldCurve, loadedCurve);
		store.commitTransaction();
	}

	@Test
	public void testHistorization() {
		store.startTransaction();
		// first curve
		YieldCurve yieldCurve1 = createYieldCurve(id);
		testCurveImpl.save(yieldCurve1);
		Calendar date = new GregorianCalendar();
		// second curve
		YieldCurve yieldCurve2 = createYieldCurve(id);
		testCurveImpl.save(yieldCurve2);
		Calendar date2 = new GregorianCalendar(2050, 5, 5);
		//
		YieldCurve loadedCurve = testCurveImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedCurve);
		assertEqualsEntities(yieldCurve1, loadedCurve);
		assertEqualsEntities(yieldCurve2, loadedCurve);
		YieldCurve loadedCurve2 = testCurveImpl.loadById(id, date2);
		assertNotNull("Should be not null", loadedCurve2);
		assertEqualsEntities(yieldCurve1, loadedCurve2);
		assertEqualsEntities(loadedCurve, loadedCurve2);
		store.commitTransaction();

	}

	protected YieldCurve createYieldCurve(YieldCurveId id) {
		YieldCurve yieldCurve = new YieldCurve(id);
		yieldCurve.setZeroYieldThreeMonths(1.3);
		yieldCurve.setZeroYieldSixMonths(1.33);
		yieldCurve.setZeroYieldOneYear(1.35);
		yieldCurve.setZeroYieldTwoYears(1.22);
		yieldCurve.setZeroYieldFiveYears(1.2);
		yieldCurve.setZeroYieldTenYears(1.25);
		yieldCurve.setZeroYieldThirtyYears(1.29);
		return yieldCurve;
	}

	protected void deleteEntityInDBTable(String nameTable, String columnName, String id) {
		String deleteQuery = "DELETE FROM " + nameTable + " WHERE " + columnName + "=?";
		try(PreparedStatement stmt = store.getConnection().prepareStatement(deleteQuery)){
			stmt.setString(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			fail("Fail delete");
		}
	}

	protected String getIdPrimaryKey(String nameTable, YieldCurveId id) {
		String idPKey = null;
		String selectQuery = "SELECT id FROM " + nameTable + " WHERE currency=? AND at_date=?";
		try(PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery)) {
			stmt.setString(1, id.getCurrency());
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

	protected void assertEqualsEntities(YieldCurve yCurve1, YieldCurve yCurve2) {
		assertEquals("Not equals by id the entities!", yCurve1.getID(), yCurve2.getID());
		assertEquals("Not equals by currency the entities!", yCurve1.getCurrency(), yCurve2.getCurrency());
		assertEquals("Not equals by date the entities!", yCurve1.getDate(), yCurve2.getDate());
		assertEquals("Not equals by zeroYieldThreeMonths the entities!", yCurve1.getZeroYieldThreeMonths(),
				yCurve2.getZeroYieldThreeMonths(), 0.1);
		assertEquals("Not equals by zeroYieldSixMonths the entities!", yCurve1.getZeroYieldSixMonths(),
				yCurve2.getZeroYieldSixMonths(), 0.1);
		assertEquals("Not equals by zeroYieldOneYear the entities!", yCurve1.getZeroYieldOneYear(),
				yCurve2.getZeroYieldOneYear(), 0.1);
		assertEquals("Not equals by zeroYieldTwoYears the entities!", yCurve1.getZeroYieldTwoYears(),
				yCurve2.getZeroYieldTwoYears(), 0.1);
		assertEquals("Not equals by zeroYieldFiveYears the entities!", yCurve1.getZeroYieldFiveYears(),
				yCurve2.getZeroYieldFiveYears(), 0.1);
		assertEquals("Not equals by zeroYieldTenYears the entities!", yCurve1.getZeroYieldTenYears(),
				yCurve2.getZeroYieldTenYears(), 0.1);
		assertEquals("Not equals by zeroYieldThirtyYears the entities!", yCurve1.getZeroYieldThirtyYears(),
				yCurve2.getZeroYieldThirtyYears(), 0.1);

	}
}
