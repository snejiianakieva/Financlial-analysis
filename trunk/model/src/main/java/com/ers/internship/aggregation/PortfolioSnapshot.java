package com.ers.internship.aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class PortfolioSnapshot extends AbstractPortfolioItem {

    private static final Logger logger = Logger.getLogger(PortfolioSnapshot.class.getName());

    /**
     * The name of the portfolio snapshot
     */
    private String name;
    
    /**
     * The currency of the portfolio snapshot
     */
    private String currency;
    
    /**
     * The list of portfolio items contained in the snapshot 
     */
    private List<PortfolioItem> items;

    /**
     * Sets the name of the portfolio snapshot to the specified one
     *
     * @param name the name of the portfolio snapshot
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the currency of the portfolio snapshot to the specified one
     *
     * @param currency the currency of the portfolio snapshot
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @return the list of portfolio items contained in the snapshot
     */
    public List<PortfolioItem> getItems() {
        return items;
    }

    /**
     * Sets the list of portfolio items contained in the snapshot to the specified one
     *
     * @param items the list of portfolio items contained in the snapshot
     */
    public void setItems(List<PortfolioItem> items) {
        this.items = items;
    }

    /**
     * Adds a portfolio item to the list of items contained in the snapshot
     *
     * @param item the portfolio item to be added to the list of items contained in the snapshot
     */
    public void addItem(PortfolioItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
    }
    
}
