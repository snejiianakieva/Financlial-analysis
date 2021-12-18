package com.ers.internship.identificators;

import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class FxQuoteId {

    private static final Logger logger = Logger.getLogger(FxQuoteId.class.getName());

    /**
     * The base currency of the FX quote
     */
    private String fromCurrency;
    
    /**
     * The target currency of the FX quote
     */
    private String toCurrency;
    
    /**
     * The validity date of the FX quote
     */
    private Calendar atDate;

    public FxQuoteId() {

    }

    /**
     *
     * @param fromCurrency the base currency of the FX quote
     * @param toCurrency the target currency of the FX quote
     * @param atDate the validity date of the FX quote
     */
    public FxQuoteId(String fromCurrency, String toCurrency, Calendar atDate) {
        super();
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.atDate = atDate;
    }

    /**
     *
     * @return the base currency of the FX quote
     */
    public String getFromCurrency() {
        return fromCurrency;
    }

    /**
     * Sets the base currency of the FX quote
     *
     * @param fromCurrency the base currency of the FX quote
     */
    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    /**
     *
     * @return the target currency of the FX quote
     */
    public String getToCurrency() {
        return toCurrency;
    }

    /**
     * Sets the target currency of the FX quote
     *
     * @param toCurrency the target currency of the FX quote
     */
    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    /**
     *
     * @return the validity date of the FX quote
     */
    public Calendar getAtDate() {
        return atDate;
    }

    /**
     * Sets the validity date of the FX quote
     *
     * @param atDate the validity date of the FX quote
     */
    public void setAtDate(Calendar atDate) {
        this.atDate = atDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.fromCurrency);
        hash = 53 * hash + Objects.hashCode(this.toCurrency);
        hash = 53 * hash + Objects.hashCode(this.atDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FxQuoteId other = (FxQuoteId) obj;
        if (!Objects.equals(this.fromCurrency, other.getFromCurrency())) {
            return false;
        } else if (!Objects.equals(this.toCurrency, other.getToCurrency())) {
            return false;
        } else if (!Objects.equals(this.atDate, other.getAtDate())) {
            return false;
        }
        return true;
    }

}
