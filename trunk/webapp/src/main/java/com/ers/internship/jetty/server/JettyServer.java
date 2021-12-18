package com.ers.internship.jetty.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.utils.ResourceUtils;
import org.apache.cxf.transport.http_jetty.JettyHTTPDestination;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngine;
import org.apache.cxf.transport.http_jetty.spring.JettyHTTPServerEngineBeanDefinitionParser;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;


public class JettyServer {
	private JAXRSServerFactoryBean sfb;
	private String url;
	private org.eclipse.jetty.server.Server httpServer;
	private RestApplication application;
	
	public JettyServer(String url) {
		this.url = url;
		
		application = new RestApplication();
	}
	
	public void start() {
		try {
			sfb = ResourceUtils.createApplication(application, false);
			sfb.setAddress(url);
			
			this.stop();
			
			Server server = sfb.create();
			
			server.getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
			server.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
			org.apache.cxf.transport.Destination dest = server.getDestination();
			JettyHTTPDestination jettyDestination = JettyHTTPDestination.class.cast(dest);
			JettyHTTPServerEngine serverEngine = JettyHTTPServerEngine.class.cast(jettyDestination.getEngine());
			httpServer = serverEngine.getServer();
			httpServer.stop();
			httpServer.join();
			Handler[] existingHandlers = httpServer.getHandlers();
			ResourceHandler resourceHandler = new ResourceHandler();
			resourceHandler.setDirectoriesListed(true);
			resourceHandler.setWelcomeFiles(new String[]{"index.html"});
			resourceHandler.setResourceBase("./src/main/webapp");
			HandlerList handlers = new HandlerList();
			handlers.addHandler(resourceHandler);
			if(existingHandlers!=null)
				for(Handler handler : existingHandlers){
					handlers.addHandler(handler);
				}
					
			httpServer.setHandler(handlers);
			httpServer.start();
			httpServer.join();
		} catch (Exception ex) {
			Logger.getLogger(JettyServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void stop() {
		if (httpServer != null) {
			try {
				httpServer.stop();
				httpServer.destroy();
			} catch (Exception ex) {
				Logger.getLogger(JettyServer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	public String getUrl() {
		return url;
	}
	
	public void registerService(Object service) {
		application.registerService(service);
	}
}
