package com.ers.internship.server.tests;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

public class CreateClient {
	private static final String REST_SERVER_URI = "http://localhost:9015";
	protected static WebClient client;

	public CreateClient() {

		if (client == null) {
			List<Object> providers = new ArrayList<Object>();
			providers.add(new JacksonJsonProvider());
			providers.add(new ObjectMapper());

			client = WebClient.create(REST_SERVER_URI, providers).accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON);
		}
	}
	
	public WebClient getClient(){
		return client;
	}
}
