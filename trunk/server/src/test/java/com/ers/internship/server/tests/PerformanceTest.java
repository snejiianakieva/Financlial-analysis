package com.ers.internship.server.tests;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.rest.calculation.CalculationRestServiceImpl;
import com.ers.internship.rest.fxquote.FxQuoteRestServiceImpl;
import com.ers.internship.rest.instrument.InstrumentRestServiceImpl;
import com.ers.internship.rest.instrumentpricequote.InstrumentPriceQuoteRestServiceImpl;
import com.ers.internship.rest.portfolio.PortfolioRestServiceImpl;
import com.ers.internship.rest.position.PositionRestServiceImpl;
import com.ers.internship.rest.server.RestServer;
import com.ers.internship.rest.transaction.TransactionRestServiceImpl;
import com.ers.internship.rest.yieldcurve.YieldCurveRestServiceImpl;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public abstract class PerformanceTest{
	private static final String SERVER_URI = "http://localhost:9015";
	private static final String DATABASE_URL = "jdbc:hsqldb:mem:db/test_database";
	protected static RestServer server;
	protected static CreateTestDB testDB;
	protected static WebClient client;
	protected static JdbcPersistentStore persistentStore;

	@BeforeClass
	public static void beforeClass() {
		testDB = new CreateTestDB();
		testDB.createDB();
		persistentStore = getPersistentStore();
		server = new RestServer(SERVER_URI);
		List<Object> providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());
		providers.add(new ObjectMapper());
		client = WebClient.create(SERVER_URI, providers).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		registerServices();
		server.start();
	}

	@AfterClass
	public static void afterClass() {
		server.stop();
		testDB.createDB();
		persistentStore = null;
		client = null;
		server = null;
	}

	@Before
	public void beforeTest() {
		System.out.println("beforeTest performance");
		testDB.createDB();
	}

	@After
	public void afterTest() {
		System.out.println("afterTest performance");
		testDB.createDB();
	}
	private static void registerServices() {
		server.registerService(new CalculationRestServiceImpl(persistentStore));
		server.registerService(new FxQuoteRestServiceImpl(persistentStore));
		server.registerService(new InstrumentPriceQuoteRestServiceImpl(persistentStore));
		server.registerService(new InstrumentRestServiceImpl(persistentStore));
		server.registerService(new PortfolioRestServiceImpl(persistentStore));
		server.registerService(new PositionRestServiceImpl(persistentStore));
		server.registerService(new TransactionRestServiceImpl(persistentStore));
		server.registerService(new YieldCurveRestServiceImpl(persistentStore));
	}

	private static JdbcPersistentStore getPersistentStore() {
		return new JdbcPersistentStore(DATABASE_URL, "username", "password");
	}


}
