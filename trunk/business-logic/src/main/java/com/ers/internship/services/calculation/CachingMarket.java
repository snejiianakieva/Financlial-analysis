package com.ers.internship.services.calculation;

import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.Market;
import com.ers.internship.market.YieldCurve;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class holds a cache of market entities and returns the cached entity if it exists.
 * Otherwise, it relies on the non-caching market implementation to extract it from its respective DAO.
 *
 * @author Snejina Yanakieva
 */
public class CachingMarket implements Market {

    private static final Logger logger = Logger.getLogger(CachingMarket.class.getName());
    
    private final Market market;
    private final Map<YieldCurveId, YieldCurve> yieldCurveCache;
    private final Map<FxQuoteId, Double> fxQuoteCache;
    private final Map<InstrumentPriceQuoteId, InstrumentPriceQuote> instrumentPriceQuoteCache;

    /**
     * Initializes empty cache maps and sets the market member to the input one
     * 
     * @param market 
     */
    public CachingMarket(Market market) {
        this.market = market;
        yieldCurveCache = new HashMap<>();
        fxQuoteCache = new HashMap<>();
        instrumentPriceQuoteCache = new HashMap<>();
    }

    /**
     * 
     * @param currency
     * @param date
     * @return cached yield curve if any or extracts it from the non-caching market
     */
    @Override
    public YieldCurve getYieldCurve(String currency, Calendar date) {
        YieldCurveId id = new YieldCurveId(currency, date);
        if (yieldCurveCache.containsKey(id)) {
            return yieldCurveCache.get(id);
        } else {
            YieldCurve loaded = market.getYieldCurve(currency, date);
            yieldCurveCache.put(id, loaded);
            return loaded;
        }
    }

    /**
     * 
     * @param baseCurrency
     * @param targetCurrency
     * @param date
     * @return cached FX quote if any or extracts it from the non-caching market
     */
    @Override
    public double getFX(String baseCurrency, String targetCurrency, Calendar date) {
        FxQuoteId id = new FxQuoteId(baseCurrency, targetCurrency, date);
        if (fxQuoteCache.containsKey(id)) {
            return fxQuoteCache.get(id);
        } else {
            Double loaded = market.getFX(baseCurrency, targetCurrency, date);
            fxQuoteCache.put(id, loaded);
            return loaded;
        }
    }

    /**
     * 
     * @param instrumentId
     * @param date
     * @return cached instrument price quote if any or extracts it from the non-caching market
     */
    @Override
    public InstrumentPriceQuote getPrice(String instrumentId, Calendar date) {
        InstrumentPriceQuoteId id = new InstrumentPriceQuoteId(instrumentId, date);
        if (instrumentPriceQuoteCache.containsKey(id)) {
            return instrumentPriceQuoteCache.get(id);
        } else {
            InstrumentPriceQuote loaded = market.getPrice(instrumentId, date);
            instrumentPriceQuoteCache.put(id, loaded);
            return loaded;
        }
    }

}
