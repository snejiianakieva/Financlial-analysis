package com.ers.internship.services.results;

import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.enums.PortfolioItemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A tree of portfolio item results containing basic info about
 * the portfolio item and the results requested from the service
 * 
 * @author Snejina Yanakieva
 */
public class PortfolioItemResult {

    private static final Logger LOGGER = Logger.getLogger(PortfolioItemResult.class.getName());

    private String itemId;
    private String itemName;
    private PortfolioItemType itemType;
    private List<PortfolioItemResult> children;
    private Map<CalculationResult, Object> results;

    public PortfolioItemResult() {

    }

    /**
     * Returns a list of {@link PortfolioItemResult} that are
     * children of this object. If this object has no children this
     * method returns an empty list or null
     * @return
     */
    public List<PortfolioItemResult> getChildren() {
        return children;
    }

    public void addChild(PortfolioItemResult child) {
        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }

    
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public PortfolioItemType getItemType() {
        return itemType;
    }

    public void setItemType(PortfolioItemType itemType) {
        this.itemType = itemType;
    }

    public Map<CalculationResult, Object> getResults() {
        return results;
    }

    public void setResults(Map<CalculationResult, Object> results) {
        this.results = results;
    }

    public void addResult(CalculationResult resultType, Object result) {
    	if (results == null) {
    		results = new HashMap<>();
    	}
    	
    	results.put(resultType, result);
    }
}
