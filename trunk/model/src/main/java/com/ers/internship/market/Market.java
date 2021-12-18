package com.ers.internship.market;

import java.util.Calendar;

/**
 * This interface provides functionality to extract yield curves, foreign exchange quotes and instrument price quotes needed for calculations
 *
 * @author Snejina Yanakieva
 */
public interface Market {

    public YieldCurve getYieldCurve(String currency, Calendar date);

    public double getFX(String baseCurrency, String targetCurrency, Calendar date);

    public InstrumentPriceQuote getPrice(String instrumentId, Calendar date);

}
