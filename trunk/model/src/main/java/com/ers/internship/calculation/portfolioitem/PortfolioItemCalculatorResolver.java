package com.ers.internship.calculation.portfolioitem;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.aggregation.PositionSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class contains a registry of portfolio item calculators mapped to the class of their respective portfolio items
 *
 * @author Snejina Yanakieva
 */
public class PortfolioItemCalculatorResolver {

    private static final Logger logger = Logger.getLogger(PortfolioItemCalculatorResolver.class.getName());

    private static final Map<Class<? extends PortfolioItem>, PortfolioItemCalculator> portfolioItemCalculators = new HashMap<>();

    static {
        portfolioItemCalculators.put(PositionSnapshot.class, new PositionSnapshotCalculatorImpl());
        portfolioItemCalculators.put(PortfolioSnapshot.class, new PortfolioSnapshotCalculatorImpl());
    }

    /**
     * Resolves and returns an instance of the correct portfolio item calculator for the class of the input portfolio item 
     * 
     * @param portfolioItemClass the class of the portfolio item to be calculated
     * @return the correct portfolio item calculator for the input class
     */
    public static PortfolioItemCalculator getPortfolioItemCalculator(Class<? extends PortfolioItem> portfolioItemClass) {
        return portfolioItemCalculators.get(portfolioItemClass);
    }

    private PortfolioItemCalculatorResolver() {
    }
    
}
