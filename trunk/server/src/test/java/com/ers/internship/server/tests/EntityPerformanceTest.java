package com.ers.internship.server.tests;

import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public abstract class EntityPerformanceTest<IdType, EntityType extends HistorizedItem<IdType>> extends PerformanceTest {
	private static final Logger LOGGER = Logger.getLogger(EntityPerformanceTest.class.getName());
	protected static String CREATE_URL = null;
	protected static String UPDATE_URL = null;
	protected static String DELETE_URL = null;// + parameters
	protected static String LOAD_URL = null; // + parameters

	protected static String CREATE_MSG = null;
	protected static String UPDATE_MSG = null;
	protected static String DELETE_MSG = null;
	protected static String LOAD_MSG = null;

	protected static String ENTITY_TABLE = null;
	protected static String ENTITY_STATE_TABLE = null;

	private long currentTiming = 0;
	private long allTimingCreate = 0;
	private long minTimingCreate = Long.MAX_VALUE;
	private long maxTimingCreate = 0;
	private long allTimingUpdate = 0;
	private long minTimingUpdate = Long.MAX_VALUE;
	private long maxTimingUpdate = 0;
	private long allTimingLoad = 0;
	private long minTimingLoad = Long.MAX_VALUE;
	private long maxTimingLoad = 0;
	private long allTimingDelete = 0;
	private long minTimingDelete = Long.MAX_VALUE;
	private long maxTimingDelete = 0;
	private int countRequests = 50;

	private long timingPost(String relativeURL) {
		long startTimer = System.currentTimeMillis();
		EntityType entity = createEntity();
		client.replacePath(relativeURL);
		client.post(entity);
		return System.currentTimeMillis() - startTimer;
	}

	private long timingDelete(String relativeURL) {
		long startTimer = System.currentTimeMillis();
		client.replacePath(relativeURL);
		client.delete();
		return System.currentTimeMillis() - startTimer;
	}

	private long timingGet(String relativeURL) {
		long startTimer = System.currentTimeMillis();
		client.replacePath(relativeURL);
		client.get();
		return System.currentTimeMillis() - startTimer;
	}

	private void printAllResults() {
		System.out.println("\n Results in milliseconds:");
		printResults(CREATE_MSG, minTimingCreate, maxTimingCreate, allTimingCreate);
		printResults(UPDATE_MSG, minTimingUpdate, maxTimingUpdate, allTimingUpdate);
		printResults(LOAD_MSG, minTimingLoad, maxTimingLoad, allTimingLoad);
		printResults(DELETE_MSG, minTimingDelete, maxTimingDelete, allTimingDelete);
	}

	private void printResults(String message, long min, long max, long all) {
		StringBuilder results = new StringBuilder(200);
		results.append("\n " + message + ":\n Min: " + min + " Max: " + max + " Average: " + (all / countRequests));
		System.out.println(results + "\n");
	}

	protected void deleteEntityInDB() {
		persistentStore.startTransaction();
		String id = getPrimaryKey();
		
		LOGGER.info("\n deleteEntityInDB:\n id=" + id);
		if (id != null) {
			deleteEntity(ENTITY_STATE_TABLE, "entity_id", id);
			deleteEntity(ENTITY_TABLE, "id", id);
			LOGGER.info("\n deleteEntityInDB:\n database tables' names: " + ENTITY_STATE_TABLE + " " + ENTITY_TABLE
					+ "\n id(PK in second table)=" + id);
		}
		persistentStore.commitTransaction();
	}

	@Test
	public void TestEntityPerformance() {
		LOGGER.info("\n TestEntityPerformance count of requests " + countRequests);
		for (int i = 0; i < countRequests; i++) {
			timingIteration();
		}
		printAllResults();
	}

	private void timingIteration() {
		currentTiming = timingPost(CREATE_URL);
		allTimingCreate += currentTiming;
		maxTimingCreate = (currentTiming > maxTimingCreate) ? currentTiming : maxTimingCreate;
		minTimingCreate = (currentTiming < minTimingCreate) ? currentTiming : minTimingCreate;
		currentTiming = timingPost(UPDATE_URL);
		allTimingUpdate += currentTiming;
		maxTimingUpdate = (currentTiming > maxTimingUpdate) ? currentTiming : maxTimingUpdate;
		minTimingUpdate = (currentTiming < minTimingUpdate) ? currentTiming : minTimingUpdate;
		currentTiming = timingGet(LOAD_URL + "/" + new GregorianCalendar().getTimeInMillis());
		allTimingLoad += currentTiming;
		maxTimingLoad = (currentTiming > maxTimingLoad) ? currentTiming : maxTimingLoad;
		minTimingLoad = (currentTiming < minTimingLoad) ? currentTiming : minTimingLoad;
		currentTiming = timingDelete(DELETE_URL);
		allTimingDelete += currentTiming;
		maxTimingDelete = (currentTiming > maxTimingDelete) ? currentTiming : maxTimingDelete;
		minTimingDelete = (currentTiming < minTimingDelete) ? currentTiming : minTimingDelete;
	}

	private void deleteEntity(String table, String columnName, String id) {
		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM " + table + "  WHERE " + columnName + "=? ");
		try (PreparedStatement stmt = persistentStore.getConnection().prepareStatement(query.toString())) {
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			fail("Fail delete" + e.getMessage());
		}

	}

	abstract protected EntityType createEntity();

	abstract protected String getPrimaryKey();

}
