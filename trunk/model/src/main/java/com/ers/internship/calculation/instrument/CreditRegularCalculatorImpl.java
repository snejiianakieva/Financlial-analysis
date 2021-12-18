package com.ers.internship.calculation.instrument;

import com.ers.internship.enums.Frequency;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.Market;
import com.ers.internship.market.YieldCurve;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Provides the functionality to calculate the net present value of a regular credit instrument with regards to its volume
 *
 * @author Snejina Yanakieva
 */
public class CreditRegularCalculatorImpl implements InstrumentCalculator {

    private static final Logger logger = Logger.getLogger(CreditRegularCalculatorImpl.class.getName());
	private final Map<Calendar, Double> principalCashFlow = new HashMap<>();
	private final Map<Calendar, Double> interestCashFlow = new HashMap<>();

	
    /**
     * Calculates and returns the net present value of the regular credit
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
        // Accumulate the net present value of all cash flow elements in netPresentValue
        double netPresentValue = 0;
        for (Map.Entry<Calendar, Double> entry : cashFlow.entrySet()) {
            if (!yieldCurve.getDate().after(entry.getKey())) {
                netPresentValue += entry.getValue() * yieldCurve.calculateDiscountFactor(entry.getKey());
            }
        }
        return netPresentValue;
    }

    private Calendar calculateInitialPaymentDate(Calendar issue, Calendar maturity, int stepType, int stepValue) {
        Calendar initialPayment = DayCountConvention.getCalendarCopy(maturity);
        while (initialPayment.after(issue)) {
            initialPayment.add(stepType, -stepValue);
        }
        initialPayment.add(stepType, stepValue);
        return initialPayment;
    }

	@Override
    public Map<Calendar, Double> buildCashFlow(Instrument instrument, double volume) {
        Map<Calendar, Double> cashFlow = new HashMap<>();

        CreditRegular credit = (CreditRegular) instrument;
        int monthTenor = credit.getTenorMonths();
        double interestRate = credit.getInterestRate();

        Frequency frequency = credit.getFrequency();
        Calendar issue = credit.getIssue();
        Calendar maturity = credit.getMaturity();

        Calendar nextPrincipalPayment = calculateInitialPaymentDate(issue, maturity, Calendar.MONTH, monthTenor);
        Calendar nextInterestPayment = calculateInitialPaymentDate(issue, maturity, frequency.getType(), frequency.getValue());

        int amortizationCount = (int) (DayCountConvention.getDistanceYears(nextPrincipalPayment, maturity) + monthTenor / 12.0) * (12 / monthTenor);
        double amortizationStep = volume / amortizationCount;
		
		Date a = nextInterestPayment.getTime();
		Date b = maturity.getTime();
		
        // Calculate the value of all credit payments (interest and principal) and map it to their date
        while (!nextPrincipalPayment.after(maturity)) {
            while (!nextInterestPayment.after(nextPrincipalPayment)) {
                cashFlow.put(DayCountConvention.getCalendarCopy(nextInterestPayment), volume * ((interestRate / 100.0) * (monthTenor / 12.0)));
                interestCashFlow.put(DayCountConvention.getCalendarCopy(nextInterestPayment), volume * ((interestRate / 100.0) * (monthTenor / 12.0)));
				nextInterestPayment.add(frequency.getType(), frequency.getValue());
            }
            if (cashFlow.containsKey(nextPrincipalPayment)) {
				double nextPayment = cashFlow.get(nextPrincipalPayment);
                cashFlow.put(DayCountConvention.getCalendarCopy(nextPrincipalPayment), amortizationStep + nextPayment);
				principalCashFlow.put(DayCountConvention.getCalendarCopy(nextPrincipalPayment), amortizationStep + nextPayment);
            } else {
                cashFlow.put(DayCountConvention.getCalendarCopy(nextPrincipalPayment), amortizationStep);
				principalCashFlow.put(DayCountConvention.getCalendarCopy(nextPrincipalPayment), amortizationStep);
            }
            volume -= amortizationStep;
            nextPrincipalPayment.add(Calendar.MONTH, monthTenor);
        }

        return cashFlow;
    }

	@Override
	 public Map<Calendar, Double> getPrincipalCashFLow() {
        return principalCashFlow;
    }
	 
	@Override
	  public Map<Calendar, Double> getInterestCashFLow() {
        return interestCashFlow;
    }

}
