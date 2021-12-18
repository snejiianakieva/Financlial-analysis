package com.ers.internship.calculation;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.market.Market;
import java.util.Calendar;

/**
 * This interface defines the functionality of various portfolio item calculators
 *
 * @author Snejina Yanakieva
 */
public interface Calculator {

    /**
     * The calculation results are added to the portfolio item's result map itself on completion
     * 
     * @param portfolioItem
     * @param market
     * @param evaluationDate 
     */
    public void calculate(PortfolioItem portfolioItem, Market market, Calendar evaluationDate);

}
