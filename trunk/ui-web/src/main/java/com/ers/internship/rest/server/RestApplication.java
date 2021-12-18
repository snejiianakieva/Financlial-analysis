package com.ers.internship.rest.server;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

@ApplicationPath("/")
public class RestApplication extends Application {
	private Set<Object> beans;
	
	public RestApplication() {
		beans = new LinkedHashSet<>();
		
		beans.add(new JacksonJsonProvider());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return beans;
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		for (Object obj : beans) {
			classes.add(obj.getClass());
		}
		return classes;
	}
	
	public void registerService(Object service) {
		boolean serviceAlreadyRegistered = false;
		for (Object registeredService : beans) {
			if (service.getClass().equals(registeredService.getClass())) {
				serviceAlreadyRegistered = true;
				break;
			}
		}
		
		if (!serviceAlreadyRegistered) {
			beans.add(service);
		}
	}

}
