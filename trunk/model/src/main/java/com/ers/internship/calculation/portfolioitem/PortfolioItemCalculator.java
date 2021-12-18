package com.ers.internship.calculation.portfolioitem;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.market.Market;
import java.util.Calendar;

/**
 * This interface defines the types of calculations all portfolio item calculators must support
 *
 * @author Snejina Yanakieva
 */
public interface PortfolioItemCalculator {

    public void calculatePresentValue(PortfolioItem portfolioItem, Market market, Calendar evaluationDate);

}
