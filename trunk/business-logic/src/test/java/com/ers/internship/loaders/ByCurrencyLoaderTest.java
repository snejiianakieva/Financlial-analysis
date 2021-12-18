package com.ers.internship.loaders;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.enums.Frequency;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.FxQuote;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.services.calculation.CalculationService;
import com.ers.internship.services.calculation.PortfolioCalculationRequest;
import com.ers.internship.services.loaders.ByCurrencyLoader;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.PortfolioItemResult;
import com.ers.internship.services.results.PortfolioItemResultStructure;
import com.ers.internship.transaction.Transaction;
import com.ers.internship.utility.EntityFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import junit.framework.Assert;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Snejina Yanakieva
 */
public class ByCurrencyLoaderTest {

    private static final Logger logger = Logger.getLogger(ByCurrencyLoaderTest.class.getName());

    private PersistentStore persistentStore;
    private Timestamp validFrom;
    private Timestamp validTo;
    private Calendar issue;
    private Calendar maturity;

    private void setupMarket() {
        YieldCurve yieldCurve;
        yieldCurve = EntityFactory.makeValidYieldCurve("EUR", issue, validFrom, validTo, 4.0, 4.2, 4.8, 6.6, 8.0, 10.0, 12.0);
        persistentStore.getYieldCurveDao().save(yieldCurve);
        yieldCurve = EntityFactory.makeValidYieldCurve("USD", issue, validFrom, validTo, 4.0, 4.2, 4.8, 6.6, 8.0, 10.0, 12.0);
        persistentStore.getYieldCurveDao().save(yieldCurve);

        InstrumentPriceQuote instrumentPriceQuote = EntityFactory.makeValidInstrumentPriceQuote("SH0001", issue, validFrom, validTo, null, "USD");
        instrumentPriceQuote.setDate(issue);
        instrumentPriceQuote.setInstrumentPrice(1000);
        persistentStore.getInstrumentPriceQuoteDao().save(instrumentPriceQuote);

        FxQuote fxQuote = EntityFactory.makeValidFxQuote("USD", "EUR", issue, validFrom, validTo, 0.91302105);
        persistentStore.getFxQuoteDao().save(fxQuote);

    }

    private void saveInstrumentPositionTransaction(Instrument instrument, Position position, Transaction transaction) {
        persistentStore.getInstrumentDao().save(instrument);
        persistentStore.getPositionDao().save(position);
        persistentStore.getTransactionDao().save(transaction);
    }

    private void setupData() {
        Instrument instrument;
        Position position;
        Transaction transaction;
        double paidAmount;

        Portfolio portfolio = EntityFactory.makeValidPortfolio("PF0001", validFrom, validTo, "Test Portfolio", "EUR");
        persistentStore.getPortfolioDao().save(portfolio);

        instrument = EntityFactory.makeValidCreditRegular("CR0001", validFrom, validTo, "EUS0378331005", "EU", "EUR", 3, 8.0, Frequency.THREE_MONTHS, issue, maturity);
        position = EntityFactory.makeValidPosition("PS0001", validFrom, validTo, "Regular Credit Position", instrument, "Bank", "Customer", "PF0001");
        paidAmount = 40000;
        transaction = EntityFactory.makeValidTransaction("TR0001", validFrom, validTo, "Credit Granting", paidAmount, paidAmount, "EUR", "Bank", "Customer", "PS0001");
        saveInstrumentPositionTransaction(instrument, position, transaction);

        instrument = EntityFactory.makeValidTimeDeposit("TD0001", validFrom, validTo, "USS0378331005", "US", "USD", 24, 10.0, issue);
        position = EntityFactory.makeValidPosition("PS0002", validFrom, validTo, "Time Deposit Position", instrument, "Bank", "Customer", "PF0001");
        paidAmount = 50000;
        transaction = EntityFactory.makeValidTransaction("TR0002", validFrom, validTo, "Credit Granting",
                paidAmount * persistentStore.getFxQuoteDao().loadLatest("USD", "EUR", issue).getValue(),
                paidAmount, "USD", "Bank", "Customer", "PS0002");
        saveInstrumentPositionTransaction(instrument, position, transaction);

        instrument = EntityFactory.makeValidShare("SH0001", validFrom, validTo, "USS0378331005", "US", "USD");
        position = EntityFactory.makeValidPosition("PS0003", validFrom, validTo, "Share Position", instrument, "Bank", "Customer", "PF0001");
        paidAmount = 30000;
        transaction = EntityFactory.makeValidTransaction("TR0003", validFrom, validTo, "Credit Granting",
                (paidAmount * persistentStore.getFxQuoteDao().loadLatest("USD", "EUR", issue).getValue())
                / persistentStore.getInstrumentPriceQuoteDao().loadLatestPrice(instrument.getID(), issue).getInstrumentPrice(),
                paidAmount, "USD", "Bank", "Customer", "PS0003");
        saveInstrumentPositionTransaction(instrument, position, transaction);
    }

    private PortfolioCalculationRequest setupRequest() {
        PortfolioCalculationRequest request = new PortfolioCalculationRequest();
        request.setEvaluationDate(issue);
        request.setStructure(PortfolioItemResultStructure.BY_CURRENCY);
        request.setPortfolioId("PF0001");
        request.setPortfolioDate(issue);

        List<CalculationResult> results = new ArrayList<>();
        results.add(DoubleResult.PV);
        request.setRequestedResults(results);
        return request;
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        validFrom = Timestamp.valueOf("2005-01-01 00:00:00.0");
        validTo = Timestamp.valueOf("2015-01-01 00:00:00.0");
        issue = DayCountConvention.getCalendarInstance(2005, 1, 1);
        maturity = DayCountConvention.getCalendarInstance(2007, 1, 1);
    }

    @Test
    public void currencyLoaderTest() {
        setupMarket();
        setupData();
        PortfolioCalculationRequest request = setupRequest();

        ByCurrencyLoader loader = new ByCurrencyLoader(persistentStore);
        PortfolioItem portfolioItem = loader.loadSnapshot("PF0001", issue);
        Assert.assertNotNull("Portfolio item is null", portfolioItem);
        CalculationService calculationService = new CalculationService(persistentStore);

        LoadResult<PortfolioItemResult> result = calculationService.calculate(request);
        Assert.assertNull("Calculation result contains errors", result.getErrors());
        PortfolioItemResult entity = result.getEntity();
        Assert.assertNotNull("Entity is null", entity);
        assertEquals("Unexpected PV result value", 110128.45272818071, entity.getResults().get(DoubleResult.PV));
    }

}
