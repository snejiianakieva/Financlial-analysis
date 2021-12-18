/**
 *
 */
package com.ers.internship.rest.calculation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.dao.CrudDao;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.FxQuote;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.rest.RestServiceTest;
import com.ers.internship.services.calculation.PortfolioCalculationRequest;
import com.ers.internship.services.results.PortfolioItemResultStructure;
import com.ers.internship.transaction.Transaction;
import com.ers.internship.utility.EntityFactory;

/**
 * @author Snejina Yanakieva
 *
 */
public class CalculationRestServiceTest extends RestServiceTest {
	
    private static final int PORTFOLIOS_COUNT = 5;
    private static final int POSITIONS_COUNT = 15;

    private static final String CALCULATION_PATH = "/calculation";

    private static final Timestamp VALID_FROM = Timestamp.valueOf("1980-01-01 00:00:00");
    private static final Timestamp VALID_TO = Timestamp.valueOf("2020-01-01 00:00:00");
    private static final Calendar EVALUATION_DATE = DayCountConvention.getCalendarInstance(2000, 1, 1);
    private static final Calendar PORTFOLIO_DATE = DayCountConvention.getCalendarInstance(1993, 1, 1);
    private List<String> portfolioIds;

    @Override
    protected Object getService() {
        return new CalculationRestServiceImpl(persistentStore);
    }

    public void cleanup() {
        persistentStore.startTransaction();
        persistentStore.dropDB();
        persistentStore.createDB();
        persistentStore.commitTransaction();
    }

    @Test
    public void test() {
        client.replacePath(CALCULATION_PATH);

        PortfolioCalculationRequest rq;
        List<CalculationResult> requestedResults = new ArrayList<>();
        requestedResults.add(DoubleResult.PV);

        for (String portfolioId : portfolioIds) {
            rq = new PortfolioCalculationRequest();

            rq.setEvaluationDate(EVALUATION_DATE);
            rq.setPortfolioDate(PORTFOLIO_DATE);
            rq.setPortfolioId(portfolioId);
            rq.setRequestedResults(requestedResults);
            rq.setStructure(PortfolioItemResultStructure.FLAT);

            Response response = client.post(rq);
            Assert.assertEquals("Portfolio calculation service returned response code "
                    + response.getStatus() + " expected " + Status.OK.getStatusCode(),
                    response.getStatus(), Status.OK.getStatusCode());
        }
    }

    @Before
    public void before() {
        portfolioIds = new ArrayList<>(PORTFOLIOS_COUNT);
        cleanup();
        generateData();
    }

    @After
    public void after() {
        cleanup();
    }

    public void generateData() {
    	CrudDao<String, Portfolio> portfolioDao = persistentStore.getPortfolioDao();
    	CrudDao<String, Position> positionDao = persistentStore.getPositionDao();
    	CrudDao<String, Transaction> transactionDao = persistentStore.getTransactionDao();
    	CrudDao<String, Instrument> instrumentDao = persistentStore.getInstrumentDao();
    	CrudDao<FxQuoteId, FxQuote> fxQuoteDao = persistentStore.getFxQuoteDao();
    	CrudDao<YieldCurveId, YieldCurve> yieldCurveDao = persistentStore.getYieldCurveDao();
    	CrudDao<InstrumentPriceQuoteId, InstrumentPriceQuote> instrumentPriceQuoteDao;
    	instrumentPriceQuoteDao = persistentStore.getInstrumentPriceQuoteDao();
    	
		Portfolio portfolio = EntityFactory.makeValidPortfolio(null, VALID_FROM, VALID_TO, null, null);
		portfolioDao.save(portfolio);
		portfolioIds.add(portfolio.getID());
		
		for (int i = 0; i < POSITIONS_COUNT; i++) {
			Position position = EntityFactory.makeValidPosition(null, VALID_FROM, VALID_TO,
					null, null, null, null, portfolio.getID());
			positionDao.save(position);
			
			Instrument instrument = position.getInstrument();
			instrumentDao.save(instrument);
			
			Transaction transaction = EntityFactory.makeValidTransaction(null, VALID_FROM,
					VALID_TO, null, null, null, instrument.getCurrency(),
					position.getLongSide(), position.getShortSide(), position.getID());
			transactionDao.save(transaction);
			
			FxQuote fxQuote = EntityFactory.makeValidFxQuote(portfolio.getCurrency(), 
					instrument.getCurrency(), EVALUATION_DATE, VALID_FROM, VALID_TO, null);
			fxQuoteDao.save(fxQuote);
			fxQuote = EntityFactory.makeValidFxQuote(instrument.getCurrency(), 
					portfolio.getCurrency(), EVALUATION_DATE, VALID_FROM, VALID_TO, null);
			fxQuoteDao.save(fxQuote);
			
			YieldCurve curve = EntityFactory.makeValidYieldCurve(portfolio.getCurrency(),
					EVALUATION_DATE, VALID_FROM, VALID_TO, null, null, null, null, null,
					null, null);
			yieldCurveDao.save(curve);
			curve = EntityFactory.makeValidYieldCurve(instrument.getCurrency(),
					EVALUATION_DATE, VALID_FROM, VALID_TO, null, null, null, null, null,
					null, null);
			yieldCurveDao.save(curve);
			
			InstrumentPriceQuote priceQuote = EntityFactory.makeValidInstrumentPriceQuote(
					instrument.getID(), EVALUATION_DATE, VALID_FROM, VALID_TO, null, instrument.getCurrency());
			instrumentPriceQuoteDao.save(priceQuote);
		}
    }
}
