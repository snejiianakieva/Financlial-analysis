package com.ers.internship.aggregation;

import com.ers.internship.calculation.CalculationResult;
import java.util.Map;

/**
 * Defines the common functionality of portfolio items
 *
 * @author Snejina Yanakieva
 */
public interface PortfolioItem {

    /**
     *
     * @return the ID of the portfolio item
     */
    public String getId();

    /**
     *
     * @return the map of calculation results of the portfolio item
     */
    public Map<CalculationResult, Object> getResults();

    /**
     *
     * @return the currency of the portfolio item
     */
    public String getCurrency();

    /**
     *
     * @return the name of the portfolio item
     */
    public String getName();

}
