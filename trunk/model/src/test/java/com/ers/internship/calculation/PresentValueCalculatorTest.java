package com.ers.internship.calculation;

import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.enums.Frequency;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Share;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.Market;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.position.Position;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Snejina Yanakieva
 */
public class PresentValueCalculatorTest {

    private static final Logger logger = Logger.getLogger(PresentValueCalculatorTest.class.getName());

    // Example market implementation
    private final Market market = new Market() {
        @Override
        public YieldCurve getYieldCurve(String currency, Calendar date) {
            YieldCurveId yieldCurveId = new YieldCurveId("EUR", date);
            YieldCurve result = new YieldCurve(yieldCurveId);
            result.setZeroYieldThreeMonths(4.0);
            result.setZeroYieldSixMonths(4.2);
            result.setZeroYieldOneYear(4.8);
            result.setZeroYieldTwoYears(6.6);
            result.setZeroYieldFiveYears(8);
            result.setZeroYieldTenYears(10);
            result.setZeroYieldThirtyYears(12);
            return result;
        }

        @Override
        public double getFX(String baseCurrency, String targetCurrency, Calendar date) {
            return 1;
        }

        @Override
        public InstrumentPriceQuote getPrice(String instrumentId, Calendar date) {
            InstrumentPriceQuote instrumentPriceQuote = new InstrumentPriceQuote();
            instrumentPriceQuote.setInstrumentPrice(1.1);
            return instrumentPriceQuote;
        }
    };

    @Test
    public void testCredit() {
        // Test the present value calculation of a regular credit
        PresentValueCalculator pvc = new PresentValueCalculator();

        // Create and setup the instrument
        CreditRegular credit = new CreditRegular();
        credit.setTenorMonths(3);
        credit.setFrequency(Frequency.THREE_MONTHS);
        credit.setInterestRate(8.0);
        credit.setIssue(DayCountConvention.getCalendarInstance(2013, 7, 20));
        credit.setMaturity(DayCountConvention.getCalendarInstance(2015, 7, 20));

        // Create the position on the instrument
        Position position = new Position();
        position.setInstrument(credit);

        // Create a snapshot from the position
        PositionSnapshot positionSnapshot = new PositionSnapshot();
        positionSnapshot.setPosition(position);
        positionSnapshot.setVolume(40000);

        // Calculate the present value of the snapshot and output the result
        pvc.calculate(positionSnapshot, market, DayCountConvention.getCalendarInstance(2013, 7, 20));
        Map<CalculationResult, Object> results = positionSnapshot.getResults();

        Assert.assertNotNull("No results found", results);
        Assert.assertNotNull("Present value result not found", results.get(DoubleResult.PV));
        Assert.assertEquals("Present value not expected", results.get(DoubleResult.PV), 41105.46110380756);
    }

    @Test
    public void testDeposit() {
        // Test the present value calculation of a time deposit
        PresentValueCalculator pvc = new PresentValueCalculator();

        // Create and setup the instrument
        TimeDeposit deposit = new TimeDeposit();
        deposit.setInterestRate(10.0);
        deposit.setTenorMonths(24);
        deposit.setIssue(DayCountConvention.getCalendarInstance(2013, 7, 20));

        // Create the position on the instrument
        Position position = new Position();
        position.setInstrument(deposit);

        // Create a snapshot from the position
        PositionSnapshot positionSnapshot = new PositionSnapshot();
        positionSnapshot.setPosition(position);
        positionSnapshot.setVolume(30000);

        // Calculate the present value of the snapshot and output the result
        pvc.calculate(positionSnapshot, market, DayCountConvention.getCalendarInstance(2013, 7, 20));
        Map<CalculationResult, Object> results = positionSnapshot.getResults();

        Assert.assertNotNull("No results found", results);
        Assert.assertNotNull("Present value result not found", results.get(DoubleResult.PV));
        Assert.assertEquals("Present value not expected", 31680.212891030624, results.get(DoubleResult.PV));
    }

    @Test
    public void testShare() {
        // Test the present value calculation of a share
        PresentValueCalculator pvc = new PresentValueCalculator();

        // Create and setup the instrument
        Share share = new Share();

        // Create the position on the instrument
        Position position = new Position();
        position.setInstrument(share);

        // Create a snapshot from the position
        PositionSnapshot positionSnapshot = new PositionSnapshot();
        positionSnapshot.setPosition(position);
        positionSnapshot.setVolume(20000);

        // Calculate the present value of the snapshot and output the result
        pvc.calculate(positionSnapshot, market, DayCountConvention.getCalendarInstance(2013, 7, 20));
        Map<CalculationResult, Object> results = positionSnapshot.getResults();

        Assert.assertNotNull("No results found", results);
        Assert.assertNotNull("Present value result not found", results.get(DoubleResult.PV));
        Assert.assertEquals("Present value not expected", 22000.0, results.get(DoubleResult.PV));
    }

    @Test
    public void testPortfolio() {
        // Test the present value calculation of a portfolio consisting of the share, time deposit and regular credit of the previous tests
        PresentValueCalculator pvc = new PresentValueCalculator();

        // Create the portfolio snapshot
        PortfolioSnapshot portfolio = new PortfolioSnapshot();
        portfolio.setCurrency("EUR");
        PositionSnapshot positionSnapshot;
        Position position;

        // Create, setup and add the credit snapshot to the portfolio snapshot
        CreditRegular credit = new CreditRegular();
        credit.setCurrency("EUR");
        credit.setTenorMonths(3);
        credit.setFrequency(Frequency.THREE_MONTHS);
        credit.setInterestRate(8.0);
        credit.setIssue(DayCountConvention.getCalendarInstance(2013, 7, 20));
        credit.setMaturity(DayCountConvention.getCalendarInstance(2015, 7, 20));
        position = new Position();
        position.setInstrument(credit);
        positionSnapshot = new PositionSnapshot();
        positionSnapshot.setPosition(position);
        positionSnapshot.setVolume(40000);
        portfolio.addItem(positionSnapshot);

        // Create, setup and add the deposit snapshot to the portfolio snapshot
        TimeDeposit deposit = new TimeDeposit();
        deposit.setCurrency("EUR");
        deposit.setInterestRate(10.0);
        deposit.setTenorMonths(24);
        deposit.setIssue(DayCountConvention.getCalendarInstance(2013, 7, 20));
        position = new Position();
        position.setInstrument(deposit);
        positionSnapshot = new PositionSnapshot();
        positionSnapshot.setPosition(position);
        positionSnapshot.setVolume(30000);
        portfolio.addItem(positionSnapshot);

        // Create, setup and add the share snapshot to the portfolio snapshot
        Share share = new Share();
        share.setCurrency("EUR");
        position = new Position();
        position.setInstrument(share);
        positionSnapshot = new PositionSnapshot();
        positionSnapshot.setPosition(position);
        positionSnapshot.setVolume(20000);
        portfolio.addItem(positionSnapshot);

        // Calculate the present value of the snapshot and output the result
        pvc.calculate(portfolio, market, DayCountConvention.getCalendarInstance(2013, 7, 20));
        Map<CalculationResult, Object> results = portfolio.getResults();

        Assert.assertNotNull("No results found", results);
        Assert.assertNotNull("Present value result not found", results.get(DoubleResult.PV));
        Assert.assertEquals("Present value not expected", 94785.67399483819, results.get(DoubleResult.PV));
    }
}
