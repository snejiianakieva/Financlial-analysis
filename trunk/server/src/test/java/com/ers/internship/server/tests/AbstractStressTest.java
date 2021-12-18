/**
 * 
 */
package com.ers.internship.server.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ers.internship.server.ServerApplication;

/**
 * @author Snejina Yanakieva
 *
 */
public abstract class AbstractStressTest {
	private static final Logger LOGGER = Logger.getLogger(AbstractStressTest.class.getName());
	
	private static final String REST_SERVER_URI = "http://localhost:9015";
	private static final ServerApplication SERVER_APP = new ServerApplication();
	private static WebClient[] clients;
	
	private static WebClient setupClient;
	
	public static final int[] TEST_REQUESTS_COUNT = new int[] { 1 };
	
	@BeforeClass
	public static void beforeClass() {
		SERVER_APP.startServer();

		List<Object> providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());
		providers.add(new ObjectMapper());
		
		setupClient = WebClient.create(REST_SERVER_URI, providers)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
		
		Arrays.sort(TEST_REQUESTS_COUNT);
		int maxClientsCount = TEST_REQUESTS_COUNT[TEST_REQUESTS_COUNT.length - 1];
		
		clients = new WebClient[maxClientsCount];

		for (int i = 0; i < clients.length; i++) {
			clients[i] = WebClient.create(REST_SERVER_URI, providers)
					.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
		}
	}
	
	@AfterClass
	public static void afterClass() {
		SERVER_APP.stopServer();
	}
	
	private void testSimultaneousRequests(int requestsCount, long timeoutInMillis) {
		List<Future<Response>> futureResponses = new ArrayList<>(requestsCount);

		long startTime = System.currentTimeMillis(),
				timeLeft;
		
		for (int i = 0; i < requestsCount; i++) {
			futureResponses.add(makeAsyncRequest(clients[i].async()));
		}
		
		
		Response currentResponse; 
		
		for (Future<Response> futureResponse : futureResponses) {
			timeLeft = System.currentTimeMillis() - startTime;
			try {
				currentResponse = futureResponse.get(timeLeft, TimeUnit.MILLISECONDS);
				Assert.assertEquals("Wrong error code!", Status.OK.getStatusCode(), currentResponse.getStatus());
				
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				Assert.assertTrue("Request timed out!", true);
			}
			
		}
		
		long totalTime = System.currentTimeMillis() - startTime;
		
		LOGGER.info(String.format("%d requests completed for %f seconds!", requestsCount, 
				((double) totalTime) / 1000));
	}
	
	@Test
	public void test() {
		for (int i : TEST_REQUESTS_COUNT) {
			LOGGER.info("Testing with " + i + " simultaneous requests!");
			testSimultaneousRequests(i, Long.MAX_VALUE);
		}
	}
	
	@Before
	public void before() {
		saveEntities(setupClient);
		
		String relativeRequestURL = getRelativeRequestURL();
		
		for (WebClient webClient : clients) {
			webClient.replacePath(relativeRequestURL);
		}
	}
	
	@After
	public void after() {
		cleanupEntities(setupClient);
	}
	
	protected abstract String getRelativeRequestURL();
	
	protected abstract Future<Response> makeAsyncRequest(AsyncInvoker client);
	
	protected abstract void saveEntities(WebClient client);
	protected abstract void cleanupEntities(WebClient client);
}
