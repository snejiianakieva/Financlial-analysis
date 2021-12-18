/**
 * 
 */
package com.ers.internship.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.rest.server.RestServer;

/**
 * @author Snejina Yanakieva
 *
 */
public abstract class RestServiceTest {

	private static final String REST_SERVER_URI = "http://localhost:9015";
	private static RestServer server;

	protected static WebClient client;
	protected static PersistentStore persistentStore;
	
	@BeforeClass
	public static void setup() {
		if (persistentStore == null) {
			persistentStore = new ExamplePersistentStore();
		}

		if (server == null) {
			server = new RestServer(REST_SERVER_URI);
		}

		if (client == null) {
			List<Object> providers = new ArrayList<Object>();
			providers.add(new JacksonJsonProvider());
			providers.add(new ObjectMapper());
			

			client = WebClient.create(REST_SERVER_URI, providers)
					.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
		}
	}

	protected abstract Object getService();
	
	@Before
	public void beforeServiceTest() {
		server.registerService(getService());
		server.start();
	}
	
	@After
	public void afterServiceTest() {
		server.stop();
	}
}
