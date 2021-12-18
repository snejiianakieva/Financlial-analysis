package com.ers.internship.aggregation;

import com.ers.internship.calculation.CalculationResult;
import java.util.HashMap;
import java.util.Map;

/**
 * Manipulates the calculation results of portfolio items
 *
 * @author Snejina Yanakieva
 */
public abstract class AbstractPortfolioItem implements PortfolioItem {
    
    /**
     * A string representing the ID of the portfolio item
     */
    private String id;

    /**
    * A map containing the values of all calculated results for the portfolio item
    */
    private Map<CalculationResult, Object> results;

    /**
     *
     * @return the id of the portfolio item
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of the portfolio to the specified one
     * 
     * @param id the ID of the portfolio item
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return the calculation results associated with the portfolio item
     */
    @Override
    public Map<CalculationResult, Object> getResults() {
        return results;
    }

    /**
     * Sets the map of results for the portfolio item to the specified one
     *
     * @param results the calculation results of the portfolio item
     */
    public void setResults(Map<CalculationResult, Object> results) {
        this.results = results;
    }

    /**
     * Adds a result to the map of calculation results for the portfolio item
     *
     * @param type the type of the calculation result
     * @param value the value of the calculation result
     */
    public void addResult(CalculationResult type, Object value) {
        if (results == null) {
            results = new HashMap<>();
        }
        results.put(type, value);
    }
    
}
