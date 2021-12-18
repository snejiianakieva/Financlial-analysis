package com.ers.internship.calculation;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.calculation.portfolioitem.PortfolioItemCalculator;
import com.ers.internship.calculation.portfolioitem.PortfolioItemCalculatorResolver;
import com.ers.internship.market.Market;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Provides the functionality to calculate the net present value of a portfolio item
 *
 * @author Snejina Yanakieva
 */
public class PresentValueCalculator implements Calculator {

    private static final Logger logger = Logger.getLogger(PresentValueCalculator.class.getName());

    @Override
    public void calculate(PortfolioItem portfolioItem, Market market, Calendar evaluationDate) {

        PortfolioItemCalculator calculator = PortfolioItemCalculatorResolver.getPortfolioItemCalculator(portfolioItem.getClass());
        calculator.calculatePresentValue(portfolioItem, market, evaluationDate);
    }
}
