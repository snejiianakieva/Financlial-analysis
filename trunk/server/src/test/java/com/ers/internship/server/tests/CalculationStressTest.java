/**
 * 
 */
package com.ers.internship.server.tests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.WebClient;

import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.FxQuote;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.services.calculation.PortfolioCalculationRequest;
import com.ers.internship.services.results.PortfolioItemResultStructure;
import com.ers.internship.transaction.Transaction;
import com.ers.internship.utility.EntityFactory;

import junit.framework.Assert;

/**
 * @author Snejina Yanakieva
 *
 */
public class CalculationStressTest extends AbstractStressTest {

	private static int POSITIONS_COUNT = 15;
	private static int TRANSACTIONS_PER_POSITION = 1;
	
	private Portfolio portfolio;
	private List<Position> positions;
	private List<Transaction> transactions;
	private List<FxQuote> fxQuotes;
	private List<YieldCurve> yieldCurves;
	private List<InstrumentPriceQuote> priceQuotes;
	private List<Instrument> instruments;
	
	private static final Calendar EVALUATION_DATE = DayCountConvention.getCalendarInstance(2020, 2, 2);
	
	private void storeEntities(WebClient client, String createURL, List<?> entities) {
		client.replacePath(createURL);
		Response response;
		
		for (Object entity : entities) {
			response = client.post(entity);
			Assert.assertEquals("Entity could not be saved!", Status.CREATED.getStatusCode(), 
					response.getStatus());
		}
	}
	
	private void deleteEntity(WebClient client, String deleteURL) {
		client.replacePath(deleteURL);
		Response response = client.delete();
		Assert.assertEquals("Entity could not be deleted! URL : " + deleteURL, Status.OK.getStatusCode(), 
				response.getStatus());
	}
	
	@Override
	protected String getRelativeRequestURL() {
		return "/calculation";
	}

	@Override
	protected Future<Response> makeAsyncRequest(AsyncInvoker client) {
		PortfolioCalculationRequest rq = new PortfolioCalculationRequest();
		List<CalculationResult> requestedResults = new ArrayList<>();
		requestedResults.add(DoubleResult.PV);
		
		long now = System.currentTimeMillis();
		Calendar portfolioDate = Calendar.getInstance();
		portfolioDate.setTimeInMillis(now);
		
		rq.setStructure(PortfolioItemResultStructure.FLAT);
		rq.setEvaluationDate(EVALUATION_DATE);
		rq.setPortfolioDate(portfolioDate);
		rq.setRequestedResults(requestedResults);
		rq.setPortfolioId(portfolio.getID());

		return client.post(Entity.entity(rq, MediaType.APPLICATION_JSON_TYPE));
	}
	
	@Override
	protected void saveEntities(WebClient client) {
		positions = new ArrayList<>(POSITIONS_COUNT);
		instruments = new ArrayList<>(POSITIONS_COUNT);
		transactions = new ArrayList<>(TRANSACTIONS_PER_POSITION * POSITIONS_COUNT);
		fxQuotes = new ArrayList<>();
		yieldCurves = new ArrayList<>();
		priceQuotes = new ArrayList<>();
		
		
		portfolio = EntityFactory.makeValidPortfolio(null);
		
		client.replacePath("/portfolio/create");
		Response response = client.post(portfolio);
		Assert.assertEquals("Portfolio could not be saved!", Status.CREATED.getStatusCode(), 
				response.getStatus());
		
		for (int i = 0; i < POSITIONS_COUNT; i++) {
			Position position = EntityFactory.makeValidPosition(null, null, null,
					null, null, null, null, portfolio.getID());
			positions.add(position);
			
			Instrument instrument = position.getInstrument();
			instruments.add(instrument);
			
			Transaction transaction = EntityFactory.makeValidTransaction(null, null,
					null, null, null, null, instrument.getCurrency(),
					position.getLongSide(), position.getShortSide(), position.getID());
			transactions.add(transaction);
			
			FxQuote fxQuote = EntityFactory.makeValidFxQuote(new FxQuoteId(
					portfolio.getCurrency(), instrument.getCurrency(), EVALUATION_DATE));
			fxQuotes.add(fxQuote);
			fxQuote = EntityFactory.makeValidFxQuote(new FxQuoteId(
					instrument.getCurrency(), portfolio.getCurrency(), EVALUATION_DATE));
			fxQuotes.add(fxQuote);
			
			YieldCurve curve = EntityFactory.makeValidYieldCurve(new YieldCurveId(portfolio.getCurrency(), 
					EVALUATION_DATE));
			yieldCurves.add(curve);
			curve = EntityFactory.makeValidYieldCurve(new YieldCurveId(instrument.getCurrency(), 
					EVALUATION_DATE));
			yieldCurves.add(curve);
			
			InstrumentPriceQuote priceQuote = EntityFactory.makeValidInstrumentPriceQuote(
					instrument.getID(), EVALUATION_DATE, null, null, null, instrument.getCurrency());
			priceQuotes.add(priceQuote);
		}

		storeEntities(client, "/instrument/create", instruments);
		storeEntities(client, "/position/create", positions);
		storeEntities(client, "/transaction/create", transactions);
		storeEntities(client, "/instrumentPriceQuote/create", priceQuotes);
		storeEntities(client, "/yieldcurve/create", yieldCurves);
		storeEntities(client, "/fxQuote/create", fxQuotes);
	}

	@Override
	protected void cleanupEntities(WebClient client) {
		deleteEntity(client, "/portfolio/delete/" + portfolio.getID());
		
		for (Position position : positions) {
			deleteEntity(client, "/instrument/delete/" + position.getInstrument().getID());
			deleteEntity(client, "/position/delete/" + position.getID());
		}
		
		for (Transaction transaction : transactions) {
			deleteEntity(client, "/transaction/delete/" + transaction.getID());
		}
		
		for (YieldCurve curve : yieldCurves) {
			deleteEntity(client, String.format("/yieldcurve/delete/%s/%d",
					curve.getCurrency(), curve.getDate().getTimeInMillis()));
		}
		
		for (FxQuote fxQuote : fxQuotes) {
			deleteEntity(client, String.format("/fxQuote/delete/%s/%s/%d",
					fxQuote.getBaseCurrency(), 
					fxQuote.getTargetCurrency(), 
					fxQuote.getDate().getTimeInMillis()
			));
		}
		
		for (InstrumentPriceQuote priceQuote : priceQuotes) {
			deleteEntity(client, String.format("/instrumentPriceQuote/delete/%s/%d",
					priceQuote.getInstrumentId(), 
					priceQuote.getDate().getTimeInMillis()
			));
		}
	}
	
}
