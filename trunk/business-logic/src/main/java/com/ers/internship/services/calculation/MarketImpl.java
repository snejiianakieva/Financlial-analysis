package com.ers.internship.services.calculation;

import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.Market;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.persistentstore.PersistentStore;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Provides Market functionality by extracting data from the respective DAO of its persistent store
 *
 * @author Snejina Yanakieva
 */
public class MarketImpl implements Market {

    private static final Logger logger = Logger.getLogger(MarketImpl.class.getName());

    private PersistentStore persistentStore;

    public MarketImpl(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    public PersistentStore getPersistentStore() {
        return persistentStore;
    }

    public void setPersistentStore(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    @Override
    public YieldCurve getYieldCurve(String currency, Calendar date) {
        return getPersistentStore().getYieldCurveDao().loadLatestCurve(currency, date);
    }

    @Override
    public double getFX(String baseCurrency, String targetCurrency, Calendar date) {
        return getPersistentStore().getFxQuoteDao().loadLatest(baseCurrency, targetCurrency, date).getValue();
    }

    @Override
    public InstrumentPriceQuote getPrice(String instrumentId, Calendar date) {
        return getPersistentStore().getInstrumentPriceQuoteDao().loadLatestPrice(instrumentId, date);
    }

}
