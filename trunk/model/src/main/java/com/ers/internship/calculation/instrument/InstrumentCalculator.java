package com.ers.internship.calculation.instrument;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.Market;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * This interface defines the types of calculations all instrument calculators
 * must support
 *
 * @author Snejina Yanakieva
 */
public interface InstrumentCalculator {
	
	public double calculatePresentValue(Instrument instrument, double volume, Market market, Calendar evaluationDate);

	public Map<Calendar, Double> buildCashFlow(Instrument instrument, double volume);

	public Map<Calendar, Double> getInterestCashFLow();

	public Map<Calendar, Double> getPrincipalCashFLow();

}
