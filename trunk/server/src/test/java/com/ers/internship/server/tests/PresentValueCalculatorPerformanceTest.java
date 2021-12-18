package com.ers.internship.server.tests;

import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.calculation.PresentValueCalculator;
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
import com.ers.internship.services.calculation.CachingMarket;
import com.ers.internship.services.calculation.MarketImpl;
import com.ers.internship.services.implementations.InstrumentPriceQuoteServiceTest;
import com.ers.internship.utility.EntityFactory;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Snejina Yanakieva
 */
public class PresentValueCalculatorPerformanceTest {

    private static final Logger logger = Logger.getLogger(PresentValueCalculatorPerformanceTest.class.getName());

    private static final Random rand = new Random();
    private static final String[] currencies = {"EUR", "USD", "BGN", "GBP", "CHF"};
    private static final Integer[] tenors = {3, 6, 12};
    private static final Frequency[] frequencies = {Frequency.TWO_MONTHS, Frequency.THREE_MONTHS, Frequency.SIX_MONTHS};

    public static int randomInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    private static double getRandomInterestRate() {
        return randomDouble(8.0, 16.0);
    }

    private static double getRandomInterestRate(double min, double max) {
        return randomDouble(min, max);
    }

    private static String getRandomCurrency() {
        return currencies[randomInt(0, currencies.length - 1)];
    }

    private static int getRandomTenor() {
        return tenors[randomInt(0, tenors.length - 1)];
    }

    private static Frequency getRandomFrequency() {
        return frequencies[randomInt(0, frequencies.length - 1)];
    }

    private PersistentStore persistentStore;
    private Timestamp validFrom;
    private Timestamp validTo;
    private Calendar issue;
    private Calendar maturity;
    private CachingMarket cachingMarket;
    private PresentValueCalculator presentValueCalculator;

    private void generateYieldCurve(String currency) {
        YieldCurve yieldCurve = EntityFactory.makeValidYieldCurve(currency, issue, validFrom, validTo, getRandomInterestRate(4.0, 5.0),
                getRandomInterestRate(5.0, 6.0), getRandomInterestRate(6.0, 7.5), getRandomInterestRate(7.5, 9.0),
                getRandomInterestRate(9.0, 11.0), getRandomInterestRate(11.0, 13.0), getRandomInterestRate(13.0, 16.0));
        persistentStore.getYieldCurveDao().save(yieldCurve);
    }

    private void submitFxQuote(String baseCurrency, String targetCurrency, double value) {
        FxQuote fxQuote = EntityFactory.makeValidFxQuote(baseCurrency, targetCurrency, issue, validFrom, validTo, value);
        persistentStore.getFxQuoteDao().save(fxQuote);
        fxQuote = EntityFactory.makeValidFxQuote(targetCurrency, baseCurrency, issue, validFrom, validTo, 1.0 / value);
        persistentStore.getFxQuoteDao().save(fxQuote);
    }

    private void setupMarket() {
        for (int i = 0; i < currencies.length; i++) {
            generateYieldCurve(currencies[i]);
        }

        submitFxQuote("EUR", "USD", 1.11317);
        submitFxQuote("EUR", "BGN", 1.95639642);
        submitFxQuote("EUR", "GBP", 0.715014292);
        submitFxQuote("EUR", "CHF", 1.08690494);
        submitFxQuote("USD", "BGN", 1.75750013);
        submitFxQuote("USD", "GBP", 0.642322639);
        submitFxQuote("USD", "CHF", 0.976405169);
        submitFxQuote("BGN", "GBP", 0.365475158);
        submitFxQuote("BGN", "CHF", 0.555564777);
        submitFxQuote("GBP", "CHF", 1.52011639);
    }

    private Instrument generateInstrument() {
        Instrument instrument;
        switch (randomInt(0, 2)) {
            case 0:
                instrument = EntityFactory.makeValidCreditRegular(null, validFrom, validTo, null, null, getRandomCurrency(),
                        getRandomTenor(), getRandomInterestRate(), getRandomFrequency(), issue, maturity);
                break;
            case 1:
                instrument = EntityFactory.makeValidTimeDeposit(null, validFrom, validTo, null, null, getRandomCurrency(),
                        getRandomTenor(), getRandomInterestRate(), issue);
                break;
            default:
                instrument = EntityFactory.makeValidShare(null, validFrom, validTo, null, null, getRandomCurrency());
                InstrumentPriceQuote instrumentPriceQuote = InstrumentPriceQuoteServiceTest.make(instrument.getID(), issue, validFrom.toString(), validTo.toString(), randomDouble(10, 100), instrument.getCurrency());
                persistentStore.getInstrumentPriceQuoteDao().save(instrumentPriceQuote);

        }
        return instrument;
    }

    private PortfolioSnapshot generatePortfolioSnapshot(int positionCount) {
        Instrument instrument;
        Position position;
        PositionSnapshot positionSnapshot;

        Portfolio portfolio = EntityFactory.makeValidPortfolio(null, validFrom, validTo, null, getRandomCurrency());
        PortfolioSnapshot portfolioSnapshot = new PortfolioSnapshot();
        portfolioSnapshot.setId("TPS");
        portfolioSnapshot.setCurrency(portfolio.getCurrency());
        portfolioSnapshot.setName("TestPortfolioSnapshot");

        for (int i = 0; i < positionCount; i++) {
            instrument = generateInstrument();
            position = EntityFactory.makeValidPosition(null, validFrom, validTo, null, instrument, null, null, portfolio.getID());

            positionSnapshot = new PositionSnapshot();
            positionSnapshot.setId("PositionSnapshot" + (i + 1));
            positionSnapshot.setPosition(position);
            positionSnapshot.setVolume(randomDouble(20000, 100000));

            portfolioSnapshot.addItem(positionSnapshot);
        }

        return portfolioSnapshot;
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        validFrom = Timestamp.valueOf("2005-01-01 00:00:00.0");
        validTo = Timestamp.valueOf("2015-01-01 00:00:00.0");
        issue = DayCountConvention.getCalendarInstance(2005, 1, 1);
        maturity = DayCountConvention.getCalendarInstance(2010, 1, 1);
        cachingMarket = new CachingMarket(new MarketImpl(persistentStore));
        presentValueCalculator = new PresentValueCalculator();
        setupMarket();
    }

    private void executeCalculation(int calculationCount, int positionCount) {
        PortfolioSnapshot portfolioSnapshot = generatePortfolioSnapshot(positionCount);
        for (int i = 0; i < calculationCount; i++) {
            presentValueCalculator.calculate(portfolioSnapshot, cachingMarket, issue);
            Assert.assertNotNull("Portfolio has no results calculated", portfolioSnapshot.getResults());
            Assert.assertNotNull("Calculation result contains errors", portfolioSnapshot.getResults().get(DoubleResult.PV));
        }
    }

    @Test
    public void testCycles10Positions10() {
        executeCalculation(10, 10);
    }

    @Test
    public void testCycles100Positions100() {
        executeCalculation(100, 100);
    }

    @Test
    public void testCycles500Positions500() {
        executeCalculation(500, 500);
    }

    @Test
    public void testCycles1Positions1000() {
        executeCalculation(1, 1000);
    }

    //@Test
    public void testCycles1000Positions1000() {
        executeCalculation(1000, 1000);
    }

}