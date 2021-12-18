package com.ers.internship.calculation.portfolioitem;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.market.Market;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provides the functionality to calculate various results for a portfolio snapshot
 *
 * @author Snejina Yanakieva
 */
public class PortfolioSnapshotCalculatorImpl implements PortfolioItemCalculator {

    private static final Logger logger = Logger.getLogger(PortfolioSnapshotCalculatorImpl.class.getName());

    /**
     * Calculates the present value of the portfolio snapshot and adds it to its inner result map
     * 
     * @param portfolioItem
     * @param market
     * @param evaluationDate 
     */
    @Override
    public void calculatePresentValue(PortfolioItem portfolioItem, Market market, Calendar evaluationDate) {
        PortfolioSnapshot portfolioSnapshot = (PortfolioSnapshot) portfolioItem;
        List<PortfolioItem> items = portfolioSnapshot.getItems();

        double netPresentValue = 0;
        for (int i = 0; i < items.size(); i++) {
            PortfolioItem item = items.get(i);
            PortfolioItemCalculator calculator = PortfolioItemCalculatorResolver.getPortfolioItemCalculator(item.getClass());
            calculator.calculatePresentValue(item, market, evaluationDate);

            if (portfolioSnapshot.getCurrency().equals(item.getCurrency())) {
                netPresentValue += (double) item.getResults().get(DoubleResult.PV);
            } else {
                netPresentValue += (double) item.getResults().get(DoubleResult.PV) * market.getFX(item.getCurrency(), portfolioSnapshot.getCurrency(), evaluationDate);
            }
        }
        portfolioSnapshot.addResult(DoubleResult.PV, netPresentValue);
    }
}
