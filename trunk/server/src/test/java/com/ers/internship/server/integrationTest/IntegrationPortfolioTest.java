package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;

import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.market.FxQuote;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.server.tests.CreateTestDB;
import com.ers.internship.services.calculation.PortfolioCalculationRequest;
import com.ers.internship.services.results.PortfolioItemResultStructure;
import com.ers.internship.transaction.Transaction;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class IntegrationPortfolioTest extends IntegrationAbstractTest<String> {
	private Portfolio portfolioTest = new Portfolio();

	@Override
	protected HistorizedItem<String> createEntity() {
		portfolioTest.setID("testPId");
		portfolioTest.setCurrency("BGN");
		portfolioTest.setName("testPName");
		return portfolioTest;
	}

	@Override
	protected HistorizedItem<String> readResponseEntity(Response response) {
		return response.readEntity(Portfolio.class);
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<String> expected, HistorizedItem<String> actual) {
		assertEquals("ID is not the same", expected.getID(), actual.getID());
		assertEquals("Name is not the same", ((Portfolio) expected).getName(), ((Portfolio) actual).getName());
		assertEquals("Currency is not the same", ((Portfolio) expected).getCurrency(),
				((Portfolio) actual).getCurrency());

	}

	@Override
	protected HistorizedItem<String> changeEntity() {
		portfolioTest.setCurrency("EUR");
		return portfolioTest;
	}

	@Override
	protected String getLoadPath(HistorizedItem<String> entity, GregorianCalendar date) {
		return "/portfolio/loadById/" + entity.getID() + "/" + date.getTimeInMillis();
	}

	@Override
	protected String getDeletePath(HistorizedItem<String> entity) {
		return "/portfolio/delete/" + entity.getID();
	}
	
	private void savePosition(){
		IntegrationPositionTest test = new IntegrationPositionTest();
		Position testPos = (Position) test.createEntity();
		test.saveEntity(testPos, "position");
		testPos = (Position)test.changeEntity();
		test.saveEntity(testPos, "position");
	}
	
	private void saveTransaction(){
		IntegrationTransactionTest test = new IntegrationTransactionTest();
		Transaction testT = (Transaction) test.createEntity();
		test.saveEntity(testT, "transaction");
		testT = (Transaction) test.changeEntity();
		test.saveEntity(testT, "transaction");
	}
	private void saveFxqoute(){
		IntegrationFxQuoteTest test = new IntegrationFxQuoteTest();
		FxQuote testT = (FxQuote) test.createEntity();
		test.saveEntity(testT, "fxQuote");
		testT = (FxQuote) test.changeEntity();
		test.saveEntity(testT, "fxQuote");
	}
	private void saveInstrumentPriceQuote(){
		IntegrationInstrumentPriceQuoteTest test = new IntegrationInstrumentPriceQuoteTest();
		InstrumentPriceQuote testT = (InstrumentPriceQuote) test.createEntity();
		test.saveEntity(testT, "instrumentPriceQuote");
		testT = (InstrumentPriceQuote) test.changeEntity();
		test.saveEntity(testT, "instrumentPriceQuote");
	}
	private void saveYieldCurve(){
		IntegrationYieldCurveTest test = new IntegrationYieldCurveTest();
		YieldCurve testT = (YieldCurve) test.createEntity();
		test.saveEntity(testT, "yieldcurve");
		testT = (YieldCurve) test.changeEntity();
		test.saveEntity(testT, "yieldcurve");
	}
	
	private PortfolioCalculationRequest createRequest(){
		PortfolioCalculationRequest request = new PortfolioCalculationRequest();
		request.setEvaluationDate(new GregorianCalendar(2014,7,14));
		request.setPortfolioDate(new GregorianCalendar());
		request.setPortfolioId(portfolioTest.getID());
		List<CalculationResult> requestResults = new ArrayList<CalculationResult>();
		requestResults.add(DoubleResult.PV);
		request.setRequestedResults(requestResults);
		request.setStructure(PortfolioItemResultStructure.BY_CURRENCY);
		return request;
	}

	@Test
	
		public void test() {
		start();
		makeTest("portfolio");
		client.replacePath("/portfolio/searchByName/"+portfolioTest.getName());
		Response response = client.get();
		Assert.assertEquals("Portfolio calculation service returned response code "
          + response.getStatus() + " expected " + Status.OK.getStatusCode(),
           response.getStatus(), Status.OK.getStatusCode());
		List<Portfolio> actualEntity = (List<Portfolio>)response.readEntity(List.class);
		Assert.assertTrue("Portfolio calculation service returned response code ",actualEntity.size()>0);
		stop();
		new CreateTestDB().createDB();
		start();
		saveEntity(createEntity(), "portfolio");
		savePosition();
		saveTransaction();
		saveFxqoute();
		saveInstrumentPriceQuote();
		saveYieldCurve();
		PortfolioCalculationRequest request = createRequest();
		client.replacePath("/calculation");
		response = client.post(request);
		Assert.assertEquals("Portfolio calculation service returned response code "
          + response.getStatus() + " expected " + Status.OK.getStatusCode(),
           response.getStatus(), Status.OK.getStatusCode());
		stop();
	}
	}


