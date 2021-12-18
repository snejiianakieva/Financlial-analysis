package com.ers.internship.market;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class InstrumentPriceQuote extends HistorizedItem<InstrumentPriceQuoteId> {

    private static final Logger logger = Logger.getLogger(InstrumentPriceQuote.class.getName());

    private double instrumentPrice;
    private String currency;

    public InstrumentPriceQuote() {
        super();
    }

    public InstrumentPriceQuote(InstrumentPriceQuoteId id) {
        super(id);
    }

    public double getInstrumentPrice() {
        return instrumentPrice;
    }

    public void setInstrumentPrice(double instrumentPrice) {
        this.instrumentPrice = instrumentPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getInstrumentId() {
        return getID().getInstrumentId();
    }

    public void setInstrumentId(String instrumentId) {
        getID().setInstrumentId(instrumentId);
    }

    public Calendar getDate() {
        return getID().getAtDate();
    }

    public void setDate(Calendar atDate) {
        getID().setAtDate(atDate);
    }

}
