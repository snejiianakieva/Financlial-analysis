package com.ers.internship.portfolio;

import com.ers.internship.historization.HistorizedItem;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class Portfolio extends HistorizedItem<String> {

    private static final Logger logger = Logger.getLogger(Portfolio.class.getName());

    /**
     * The name of the portfolio
     */
    private String name;
    
    /**
     * The currency of the portfolio
     */
    private String currency;

    public Portfolio() {
        super();
    }

    /**
     *
     * @param id the ID of the portfolio
     */
    public Portfolio(String id) {
        super(id);
    }

    /**
     *
     * @return the name of the portfolio
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the portfolio
     *
     * @param name the name of the portfolio
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the currency of the portfolio
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the portfolio
     *
     * @param currency the currency of the portfolio
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
