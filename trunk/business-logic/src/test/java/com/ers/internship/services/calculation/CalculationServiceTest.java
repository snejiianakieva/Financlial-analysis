package com.ers.internship.services.calculation;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.calculation.Calculator;
import com.ers.internship.calculation.PresentValueCalculator;
import com.ers.internship.dao.CrudDao;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.enums.Frequency;
import com.ers.internship.enums.PortfolioItemType;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.exampledao.loaders.ExampleFlatLoader;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.services.loaders.PortfolioLoader;
import com.ers.internship.services.results.PortfolioItemResult;
import com.ers.internship.services.results.PortfolioItemResultStructure;
import com.ers.internship.transaction.Transaction;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Snejina Yanakieva
 */
public class CalculationServiceTest {

    private static final Logger logger = Logger.getLogger(CalculationServiceTest.class.getName());

    private static final Timestamp VALID_FROM = Timestamp.valueOf("2013-01-01 00:00:00");
    private static final Timestamp VALID_TO = Timestamp.valueOf("2016-12-31 23:59:59");

    private static final Timestamp EVALUATION_DATE = new Timestamp(
            (VALID_FROM.getTime() + VALID_TO.getTime()) / 2);

    private static final String LONG_SIDE = "Kolio Bliznaka";
    private static final String SHORT_SIDE = "bliznaka na Kolio Bliznaka";

    private static final String MARKET = "Test Market";

    private PersistentStore persistentStore;
    private CalculationService calculationService;

    private List<Portfolio> portfolios;
    private List<Position> positions;
    private List<Instrument> instruments;
    private List<Transaction> transactions;
    private List<YieldCurve> yieldCurves;
    private List<InstrumentPriceQuote> instrumentPriceQuotes;

    private Portfolio makePortfolio(String id, String currency) {
        Portfolio result = new Portfolio(id);

        result.setValidFrom(VALID_FROM);
        result.setValidTo(VALID_TO);
        result.setName("Test Portfolio");
        result.setCurrency(currency);

        return result;
    }

    private Position makePosition(String id, String portfolioId, Instrument instrument) {
        Position result = new Position(id);

        result.setInstrument(instrument);
        result.setLongSide(LONG_SIDE);
        result.setShortSide(SHORT_SIDE);
        result.setName("Test Position");
        result.setPortfolioId(portfolioId);
        result.setValidFrom(VALID_FROM);
        result.setValidTo(VALID_TO);

        return result;
    }

    private Transaction makeTransaction(String id, String positionId,
            String currency, double volume, boolean longToShort) {
        Transaction result = new Transaction(id);

        result.setName("Test Transaction");
        result.setPositionId(positionId);
        result.setReceiver(longToShort ? SHORT_SIDE : LONG_SIDE);
        result.setSender(longToShort ? LONG_SIDE : SHORT_SIDE);
        result.setValidFrom(VALID_FROM);
        result.setValidTo(VALID_TO);
        result.setCurrency(currency);
        result.setVolume(volume);

        return result;
    }

    private YieldCurve makeYieldCurve(String currency, Timestamp date, Timestamp validFrom, Timestamp validTo,
            double zy3m, double zy6m, double zy1y, double zy2y, double zy5y, double zy10y, double zy30y) {
        YieldCurve yieldCurve = new YieldCurve();
        Calendar yieldCurveDate = Calendar.getInstance();
        yieldCurveDate.setTime(date);
        yieldCurve.setID(new YieldCurveId(currency, yieldCurveDate));
        yieldCurve.setValidFrom(validFrom);
        yieldCurve.setValidTo(validTo);
        yieldCurve.setZeroYieldThreeMonths(zy3m);
        yieldCurve.setZeroYieldSixMonths(zy6m);
        yieldCurve.setZeroYieldOneYear(zy1y);
        yieldCurve.setZeroYieldTwoYears(zy2y);
        yieldCurve.setZeroYieldFiveYears(zy5y);
        yieldCurve.setZeroYieldTenYears(zy10y);
        yieldCurve.setZeroYieldThirtyYears(zy30y);
        return yieldCurve;
    }

    private InstrumentPriceQuote makeInstrumentPriceQuote(String instrumentId, Timestamp quoteTime,
            String currency, double instrumentPrice) {
        Calendar quoteDate = Calendar.getInstance();
        quoteDate.setTimeInMillis(quoteTime.getTime());

        InstrumentPriceQuoteId id = new InstrumentPriceQuoteId(instrumentId, quoteDate);
        InstrumentPriceQuote result = new InstrumentPriceQuote(id);
        result.setCurrency(currency);
        result.setValidFrom(VALID_FROM);
        result.setValidTo(VALID_TO);
        result.setInstrumentPrice(instrumentPrice);

        return result;
    }

    private void makeAndStorePortfolio(String id, String currency) {
        Portfolio portfolio = makePortfolio(id, currency);

        portfolios.add(portfolio);
        persistentStore.getPortfolioDao().save(portfolio);
    }

    private void makeAndStorePosition(String id, String portfolioId, Instrument instrument) {
        Position position = makePosition(id, portfolioId, instrument);

        positions.add(position);
        persistentStore.getPositionDao().save(position);
    }

    private void makeAndStoreTransaction(String id, String positionId,
            String currency, double volume, boolean longToShort) {

        Transaction transaction = makeTransaction(id, positionId, currency, volume, longToShort);

        transactions.add(transaction);
        persistentStore.getTransactionDao().save(transaction);
    }

    private void makeAndStoreYieldCurve(String currency, Timestamp date, Timestamp validFrom, Timestamp validTo,
            double zy3m, double zy6m, double zy1y, double zy2y, double zy5y, double zy10y, double zy30y) {

        YieldCurve curve = makeYieldCurve(currency, date, validFrom, validTo, zy3m,
                zy6m, zy1y, zy2y, zy5y, zy10y, zy30y);

        yieldCurves.add(curve);
        persistentStore.getYieldCurveDao().save(curve);
    }

    private void makeAndStoreInstrumentPriceQuote(String instrumentId, Timestamp quoteTime,
            String currency, double instrumentPrice) {

        InstrumentPriceQuote quote = makeInstrumentPriceQuote(instrumentId, quoteTime, currency,
                instrumentPrice);

        instrumentPriceQuotes.add(quote);
        persistentStore.getInstrumentPriceQuoteDao().save(quote);
    }

    @Before
    public void init() {
        portfolios = new ArrayList<>();
        positions = new ArrayList<>();
        instruments = new ArrayList<>();
        transactions = new ArrayList<>();
        yieldCurves = new ArrayList<>();
        instrumentPriceQuotes = new ArrayList<>();

        persistentStore = new ExamplePersistentStore();
        calculationService = new CalculationService(persistentStore);

        clearStore();
        fillStore();
    }

    @After
    public void cleanup() {
        clearStore();
    }

    @Test
    public void calculateFlatPVTest() {
        PortfolioLoader flatLoader = new ExampleFlatLoader(persistentStore);
        calculationService.setPortfolioLoader(PortfolioItemResultStructure.FLAT, flatLoader);

        Calendar evaluationDate = Calendar.getInstance(),
                portfolioDate = Calendar.getInstance();

        evaluationDate.setTimeInMillis(EVALUATION_DATE.getTime());
        portfolioDate.setTimeInMillis(EVALUATION_DATE.getTime());

        List<CalculationResult> requestedResults = new ArrayList<>();
        requestedResults.add(DoubleResult.PV);

        Calculator pvCalculator = new PresentValueCalculator();

        for (Portfolio portfolio : portfolios) {
            PortfolioCalculationRequest rq = new PortfolioCalculationRequest();
            rq.setEvaluationDate(evaluationDate);
            rq.setPortfolioDate(portfolioDate);
            rq.setPortfolioId(portfolio.getID());
            rq.setRequestedResults(requestedResults);
            rq.setStructure(PortfolioItemResultStructure.FLAT);

            PortfolioItemResult result = calculationService.calculate(rq).getEntity();

            PortfolioSnapshot portfolioSnapshot = flatLoader.loadSnapshot(portfolio.getID(), evaluationDate);
            pvCalculator.calculate(portfolioSnapshot, calculationService.getMarket(), evaluationDate);

            validateResult("Flat PV calculation test failed on portfolio " + portfolioSnapshot.getId(),
                    portfolioSnapshot, result, requestedResults);
        }
    }

    private void validateResult(String message, PortfolioItem expectedItem,
            PortfolioItemResult result, List<CalculationResult> requestedResults) {

        Assert.assertNotNull(message + " a null result was found!", result);

        for (CalculationResult requestedResult : requestedResults) {
            Object expectedResult = expectedItem.getResults().get(requestedResult),
                    actualResult = result.getResults().get(requestedResult);

            Assert.assertEquals(message + " " + requestedResult.getName() + " does not match!",
                    expectedResult, actualResult);
        }

        if (!(expectedItem instanceof PortfolioSnapshot)) {
            Assert.assertEquals(" item result type does not match!",
                    PortfolioItemType.POSITION, result.getItemType());
            return;
        }

        PortfolioSnapshot portfolio = (PortfolioSnapshot) expectedItem;

        for (PortfolioItem item : portfolio.getItems()) {
            for (PortfolioItemResult itemResult : result.getChildren()) {
                if (itemResult.getItemId().equals(item.getId())) {
                    validateResult(message, item, itemResult, requestedResults);
                }
            }
        }
    }

    private void fillStore() {
        // Set up instruments

        CreditRegular credit = new CreditRegular("TestCreditRegular1");

        credit.setCurrency("BGN");
        credit.setTenorMonths(3);
        credit.setFrequency(Frequency.THREE_MONTHS);
        credit.setInterestRate(8.0);
        credit.setIssue(DayCountConvention.getCalendarInstance(2013, 7, 20));
        credit.setMaturity(DayCountConvention.getCalendarInstance(2015, 7, 20));
        credit.setIsin("Regular Credit ISIN");
        credit.setValidFrom(VALID_FROM);
        credit.setValidTo(VALID_TO);
        credit.setMarket(MARKET);

        TimeDeposit deposit = new TimeDeposit("TestDeposit1");

        deposit.setCurrency("BGN");
        deposit.setInterestRate(10.0);
        deposit.setTenorMonths(24);
        deposit.setIssue(DayCountConvention.getCalendarInstance(2013, 7, 20));
        deposit.setIsin("Time Deposit ISIN");
        deposit.setValidFrom(VALID_FROM);
        deposit.setValidTo(VALID_TO);
        deposit.setMarket(MARKET);

        Share share = new Share("TestShare1");

        share.setIsin("Share ISIN");
        share.setMarket(MARKET);
        share.setCurrency("BGN");
        share.setValidFrom(VALID_FROM);
        share.setValidTo(VALID_TO);

        // Add them to the instruments list, so they can be cleaned up
        // later from the dao
        instruments.add(credit);
        instruments.add(deposit);
        instruments.add(share);

        makeAndStoreInstrumentPriceQuote(credit.getID(), EVALUATION_DATE, "BGN", 70000);
        makeAndStoreInstrumentPriceQuote(deposit.getID(), EVALUATION_DATE, "BGN", 100000);
        makeAndStoreInstrumentPriceQuote(share.getID(), EVALUATION_DATE, "BGN", 1000);

        // Add instruments to the dao
        CrudDao<String, Instrument> instrumentDao = persistentStore.getInstrumentDao();

        for (Instrument instrument : instruments) {
            instrumentDao.save(instrument);
        }

        makeAndStorePortfolio("TestPortfolio1", "BGN");

        makeAndStorePosition("TestCreditPosition1", "TestPortfolio1", credit);
        makeAndStorePosition("TestSharePosition1", "TestPortfolio1", share);
        makeAndStorePosition("TestTimeDepositPosition1", "TestPortfolio1", deposit);

        makeAndStoreTransaction("Transaction1", "TestCreditPosition1", "BGN", 1, true);
        makeAndStoreTransaction("Transaction2", "TestSharePosition1", "BGN", 150, false);
        makeAndStoreTransaction("Transaction3", "TestTimeDepositPosition1", "BGN", 1, false);

        makeAndStoreYieldCurve("BGN", EVALUATION_DATE, VALID_FROM, VALID_TO, 4.0, 4.2, 4.8,
                6.6, 8, 10, 12);
    }

    private void clearStore() {
        CrudDao<String, Portfolio> portfolioDao = persistentStore.getPortfolioDao();
        CrudDao<String, Position> positionDao = persistentStore.getPositionDao();
        CrudDao<String, Instrument> instrumentDao = persistentStore.getInstrumentDao();
        CrudDao<String, Transaction> transactionDao = persistentStore.getTransactionDao();
        CrudDao<YieldCurveId, YieldCurve> yieldCurveDao = persistentStore.getYieldCurveDao();
        CrudDao<InstrumentPriceQuoteId, InstrumentPriceQuote> instrumentPriceQuoteDao;
        instrumentPriceQuoteDao = persistentStore.getInstrumentPriceQuoteDao();

        for (Portfolio portfolio : portfolios) {
            portfolioDao.delete(portfolio.getID());
        }

        for (Position position : positions) {
            positionDao.delete(position.getID());
        }

        for (Instrument instrument : instruments) {
            instrumentDao.delete(instrument.getID());
        }

        for (Transaction transaction : transactions) {
            transactionDao.delete(transaction.getID());
        }

        for (YieldCurve curve : yieldCurves) {
            yieldCurveDao.delete(curve.getID());
        }

        for (InstrumentPriceQuote quote : instrumentPriceQuotes) {
            instrumentPriceQuoteDao.delete(quote.getID());
        }
    }

}
