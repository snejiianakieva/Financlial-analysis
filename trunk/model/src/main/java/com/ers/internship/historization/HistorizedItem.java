package com.ers.internship.historization;

import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 * Provides the base data required to achieve historization in the database
 *
 * @author Snejina Yanakieva
 * @param <IdType> the type to be used as ID
 */
public class HistorizedItem<IdType> {

    private static final Logger logger = Logger.getLogger(HistorizedItem.class.getName());

    /**
     * The ID of the historized item
     */
    private IdType id;
    
    /**
     * The start of the historized item's validity
     */
    private Timestamp validFrom;
    
    /**
     * The end of the historized item's validity
     */
    private Timestamp validTo;

    public HistorizedItem() {
    }

    /**
     *
     * @param id the ID of the historized item
     */
    public HistorizedItem(IdType id) {
        this.id = id;
    }

    /**
     *
     * @return the ID of the historized item
     */
    public IdType getID() {
        return id;
    }

    /**
     * Sets the ID of the historized item
     *
     * @param id the ID of the historized item
     */
    public void setID(IdType id) {
        this.id = id;
    }

    /**
     *
     * @return the start of the validity period of the historized item
     */
    public Timestamp getValidFrom() {
        return validFrom;
    }

    /**
     * Sets the start of the validity period of the historized item
     *
     * @param validFrom the start of the validity period of the historized item
     */
    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    /**
     *
     * @return the end of the validity period of the historized item
     */
    public Timestamp getValidTo() {
        return validTo;
    }

    /**
     * Sets the end of the validity period of the historized item
     *
     * @param validTo the end of the validity period of the historized item
     */
    public void setValidTo(Timestamp validTo) {
        this.validTo = validTo;
    }

}
