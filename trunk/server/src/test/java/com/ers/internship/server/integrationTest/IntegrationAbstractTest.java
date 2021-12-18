package com.ers.internship.server.integrationTest;

import java.util.GregorianCalendar;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.server.ServerApplication;
import com.ers.internship.server.tests.CreateClient;
import com.ers.internship.server.tests.CreateTestDB;

import junit.framework.Assert;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public abstract class IntegrationAbstractTest <IdType> {
	protected static WebClient client;
	private static ServerApplication s;

	@BeforeClass
	public static void setup() {
		new CreateTestDB().createDB();
		client = new CreateClient().getClient();
		s = new ServerApplication();
	}
	

	@AfterClass
	public static void after() {
		client =null;
		s = null;
	}


	@Before
	public void beforeTest(){
		start();
	}
	
	protected void start() {
		s.startServer();
		
	}

	@After
	public void afterTest() {
		stop();
		
	}
	protected void stop() {
		s.stopServer();
		
	}

	protected void saveEntity(HistorizedItem<IdType> entity, String enityType) {
		client.replacePath("/" + enityType + "/create");
		Response response = client.post(entity);
		Assert.assertEquals("Received status code " + response.getStatus() +
				" when trying to create a valid entity!", Status.CREATED.getStatusCode(), response.getStatus());

	}

	private Response load(HistorizedItem<IdType> entity, GregorianCalendar date) {
		client.replacePath( getLoadPath(entity, date));
		Response response = client.get();
		return response;
	}


	private HistorizedItem<IdType> loadValidEntity(HistorizedItem<IdType> entity, GregorianCalendar date) {
		Response response = load(entity, date);
		HistorizedItem<IdType> actualEntity = readResponseEntity(response);
		Assert.assertEquals("Received status code " + response.getStatus() + 
				" when trying to load a valid entity!",	Status.OK.getStatusCode(), response.getStatus());
		return actualEntity;
	}

	private void loadDeletedEntity(HistorizedItem<IdType> entity, GregorianCalendar date) {
		Response response = load(entity, date);
		Assert.assertEquals("Received status code " + response.getStatus() +
				" when trying to load a valid entity!", Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}


	private void updateEntity(HistorizedItem<IdType> entity, String entityType) {
		client.replacePath("/" + entityType + "/update");
		Response response = client.post(entity);
		Assert.assertEquals("Received status code " + response.getStatus() + 
				" when trying to update a valid entity!", Status.OK.getStatusCode(), response.getStatus());
	}

	protected void makeTest(String entityType) {
		HistorizedItem<IdType> entity = createEntity();
		saveEntity(entity, entityType);
		assertEntityEquals(entity, loadValidEntity(entity, new GregorianCalendar()));
		entity = changeEntity();
		updateEntity(entity, entityType);
		assertEntityEquals(entity, loadValidEntity(entity, new GregorianCalendar(2020, 10, 10)));
		deleteEntity(entity, entityType);
		loadDeletedEntity(entity, new GregorianCalendar(2020, 10, 10));

	}

	private void deleteEntity(HistorizedItem<IdType> entity, String entityType) {
		client.replacePath(getDeletePath(entity));
		Response response = client.delete();
		Assert.assertEquals("Received status code " + response.getStatus() +
				" when trying to delete a valid entity!", Status.OK.getStatusCode(), response.getStatus());

	}

	abstract HistorizedItem<IdType> changeEntity();
	protected abstract HistorizedItem<IdType> readResponseEntity(Response response);
	abstract HistorizedItem<IdType> createEntity();
	protected abstract void assertEntityEquals(HistorizedItem<IdType> expected, HistorizedItem<IdType> actual);
	protected abstract String getLoadPath(HistorizedItem<IdType> entity, GregorianCalendar date);
	protected abstract String getDeletePath(HistorizedItem<IdType> entity);
}
