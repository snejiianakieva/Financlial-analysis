package com.ers.internship.calculation.instrument;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.Market;
import com.ers.internship.market.YieldCurve;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Provides the functionality to calculate various results for a time deposit instrument with regards to its volume
 *
 * @author Snejina Yanakieva
 */
public class TimeDepositCalculatorImpl implements InstrumentCalculator {

    private static final Logger logger = Logger.getLogger(TimeDepositCalculatorImpl.class.getName());
	private final Map<Calendar, Double> principalCashFlow = new HashMap<>();
	private final Map<Calendar, Double> interestCashFlow = new HashMap<>();


    /**
     * Calculates and returns the net present value of the time deposit
     * 
     * @param instrument
     * @param volume
     * @param market
     * @param evaluationDate
     * @return 
     */
    @Override
    public double calculatePresentValue(Instrument instrument, double volume, Market market, Calendar evaluationDate) {
        YieldCurve yieldCurve = market.getYieldCurve(instrument.getCurrency(), evaluationDate);
        Map<Calendar, Double> cashFlow = buildCashFlow(instrument, volume);

        double netPresentValue = 0;
        for (Map.Entry<Calendar, Double> entry : cashFlow.entrySet()) {
            if (!yieldCurve.getDate().after(entry.getKey())) {
                netPresentValue += entry.getValue() * yieldCurve.calculateDiscountFactor(entry.getKey());
            }
        }
        return netPresentValue;
    }

	@Override
    public Map<Calendar, Double> buildCashFlow(Instrument instrument, double volume) {
        Map<Calendar, Double> cashFlow = new HashMap<>();

        TimeDeposit deposit = (TimeDeposit) instrument;
        int monthTenor = deposit.getTenorMonths();
        Calendar maturity = DayCountConvention.getCalendarCopy(deposit.getIssue());
        maturity.add(Calendar.MONTH, monthTenor);

        double amortization = volume + volume * ((deposit.getInterestRate() / 100.0) * (monthTenor / 12.0));
        cashFlow.put(maturity, amortization);

        return cashFlow;
    }

	@Override
	public Map<Calendar, Double> getInterestCashFLow() {
		return interestCashFlow;
	}

	@Override
	public Map<Calendar, Double> getPrincipalCashFLow() {
		return principalCashFlow;
	}

}
