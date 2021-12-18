package com.ers.internship.market;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.identificators.FxQuoteId;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class FxQuote extends HistorizedItem<FxQuoteId> {

    private static final Logger LOG = Logger.getLogger(FxQuote.class.getName());

    private double value;

    public FxQuote() {
        super();
    }

    public FxQuote(FxQuoteId id) {
        super(id);
    }

    public String getBaseCurrency() {
        return getID().getFromCurrency();
    }

    public void setBaseCurrency(String fromCurrency) {
        getID().setFromCurrency(fromCurrency);
    }

    public String getTargetCurrency() {
        return getID().getToCurrency();
    }

    public void setTargetCurrency(String toCurrency) {
        getID().setToCurrency(toCurrency);
    }

    public Calendar getDate() {
        return getID().getAtDate();
    }

    public void setDate(Calendar atDate) {
        getID().setAtDate(atDate);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
