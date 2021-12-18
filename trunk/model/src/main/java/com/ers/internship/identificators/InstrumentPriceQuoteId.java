package com.ers.internship.identificators;

import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class InstrumentPriceQuoteId {

    private static final Logger logger = Logger.getLogger(InstrumentPriceQuoteId.class.getName());

    /**
     * The ID of the instrument
     */
    private String instrumentId;
    
    /**
     * The validity date of the price quote
     */
    private Calendar atDate;

    public InstrumentPriceQuoteId() {

    }

    /**
     *
     * @param instrumentId the ID of the instrument
     * @param atDate the validity date of the price quote
     */
    public InstrumentPriceQuoteId(String instrumentId, Calendar atDate) {
        this.instrumentId = instrumentId;
        this.atDate = atDate;
    }

    /**
     *
     * @return the ID of the instrument
     */
    public String getInstrumentId() {
        return instrumentId;
    }

    /**
     * Sets the ID of the instrument
     *
     * @param instrumentId the ID of the instrument
     */
    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    /**
     *
     * @return the validity date of the price quote
     */
    public Calendar getAtDate() {
        return atDate;
    }

    /**
     * Sets the validity date of the price quote
     *
     * @param atDate the validity date of the price quote
     */
    public void setAtDate(Calendar atDate) {
        this.atDate = atDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.instrumentId);
        hash = 17 * hash + Objects.hashCode(this.atDate);
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
        final InstrumentPriceQuoteId other = (InstrumentPriceQuoteId) obj;
        if (!Objects.equals(this.instrumentId, other.getInstrumentId())) {
            return false;
        } else if (!Objects.equals(this.atDate, other.getAtDate())) {
            return false;
        }
        return true;
    }

}
