package com.ers.internship.rest.server;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.utils.ResourceUtils;


public class RestServer {
	private JAXRSServerFactoryBean sfb;
	private String url;
	private Server server;
	private RestApplication application;
	
	public RestServer(String url) {
		this.url = url;
		
		application = new RestApplication();
	}
	
	public void start() {
		sfb = ResourceUtils.createApplication(application, false);
		sfb.setAddress(url);
		
		this.stop();
		
		server = sfb.create();
		
		server.getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
		server.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
		
		server.start();
	}
	
	public void stop() {
		if (server != null) {
			server.stop();
			server.destroy();
		}
	}
	
	public String getUrl() {
		return url;
	}
	
	public void registerService(Object service) {
		application.registerService(service);
	}
}
