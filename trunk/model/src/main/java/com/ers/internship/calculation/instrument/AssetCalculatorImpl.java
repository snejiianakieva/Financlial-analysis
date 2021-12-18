package com.ers.internship.calculation.instrument;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.Market;

import java.util.Calendar;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Provides the functionality to calculate various results for an asset
 * instrument with regards to its volume
 *
 * @author Snejina Yanakieva
 */
public class AssetCalculatorImpl implements InstrumentCalculator {

	private static final Logger logger = Logger.getLogger(AssetCalculatorImpl.class.getName());

	/**
	 * Calculates and returns the net present value of the asset
	 *
	 * @param instrument
	 * @param volume
	 * @param market
	 * @param evaluationDate
	 * @return
	 */
	@Override
	public double calculatePresentValue(Instrument instrument, double volume, Market market, Calendar evaluationDate) {
		return volume * market.getPrice(instrument.getID(), evaluationDate).getInstrumentPrice();
	}

	@Override
	public Map<Calendar, Double> buildCashFlow(Instrument instrument, double volume) {
		return null;
	}

	@Override
	public Map<Calendar, Double> getInterestCashFLow() {
		return null;
	}

	@Override
	public Map<Calendar, Double> getPrincipalCashFLow() {
		return null;
	}

}
