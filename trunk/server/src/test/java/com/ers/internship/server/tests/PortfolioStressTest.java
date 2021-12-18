/**
 * 
 */
package com.ers.internship.server.tests;

import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.WebClient;

import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.utility.EntityFactory;

import junit.framework.Assert;

/**
 * @author Snejina Yanakieva
 *
 */
public class PortfolioStressTest extends AbstractStressTest {

	private static final String PORTFOLIO_ID = "TestPortfolio14";
	
	@Override
	protected String getRelativeRequestURL() {
		return String.format("/portfolio/loadById/%s/%d", PORTFOLIO_ID, System.currentTimeMillis());
	}

	@Override
	protected Future<Response> makeAsyncRequest(AsyncInvoker client) {
		return client.get();
	}

	@Override
	protected void saveEntities(WebClient client) {
		Portfolio portfolio = EntityFactory.makeValidPortfolio(PORTFOLIO_ID);
		client.replacePath("/portfolio/create");
		Response response = client.post(portfolio);
		
		Assert.assertEquals("Portfolio could not be saved!", Status.CREATED.getStatusCode(), 
				response.getStatus());
		
	}

	@Override
	protected void cleanupEntities(WebClient client) {
		client.replacePath("/portfolio/delete/" + PORTFOLIO_ID);
	}

}
