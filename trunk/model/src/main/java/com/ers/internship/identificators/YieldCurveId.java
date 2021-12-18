package com.ers.internship.identificators;

import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class YieldCurveId {

    private static final Logger logger = Logger.getLogger(YieldCurveId.class.getName());

    /**
     * The currency of the yield curve
     */
    private String currency;
    
    /**
     * The validity date of the yield curve
     */
    private Calendar atDate;

    public YieldCurveId() {

    }

    /**
     *
     * @param currency the currency of the yield curve
     * @param atDate the validity date of the yield curve
     */
    public YieldCurveId(String currency, Calendar atDate) {
        this.currency = currency;
        this.atDate = atDate;
    }

    /**
     *
     * @return the currency of the yield curve
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the yield curve
     *
     * @param currency the currency of the yield curve
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     *
     * @return the validity date of the yield curve
     */
    public Calendar getAtDate() {
        return atDate;
    }

    /**
     * Sets the validity date of the yield curve
     *
     * @param atDate the validity date of the yield curve
     */
    public void setAtDate(Calendar atDate) {
        this.atDate = atDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.currency);
        hash = 11 * hash + Objects.hashCode(this.atDate);
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
        final YieldCurveId other = (YieldCurveId) obj;
        if (!Objects.equals(this.currency, other.getCurrency())) {
            return false;
        } else if (!Objects.equals(this.atDate, other.getAtDate())) {
            return false;
        }
        return true;
    }

}
