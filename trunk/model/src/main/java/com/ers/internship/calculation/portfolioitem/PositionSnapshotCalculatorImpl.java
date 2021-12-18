package com.ers.internship.calculation.portfolioitem;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.calculation.instrument.InstrumentCalculator;
import com.ers.internship.calculation.instrument.InstrumentCalculatorResolver;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.Market;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Provides the functionality to calculate various results for a position snapshot
 *
 * @author Snejina Yanakieva
 */
public class PositionSnapshotCalculatorImpl implements PortfolioItemCalculator {

    private static final Logger logger = Logger.getLogger(PositionSnapshotCalculatorImpl.class.getName());

    /**
     * Calculates the present value of the position snapshot and adds it to its inner result map
     * 
     * @param portfolioItem
     * @param market
     * @param evaluationDate 
     */
    @Override
    public void calculatePresentValue(PortfolioItem portfolioItem, Market market, Calendar evaluationDate) {
        PositionSnapshot snapshot = (PositionSnapshot) portfolioItem;
        Instrument instrument = snapshot.getPosition().getInstrument();
        InstrumentCalculator instrumentCalculator = InstrumentCalculatorResolver.getInstrumentCalculator(instrument.getClass()); // Fetch the correct calculator for the input snapshot
        snapshot.addResult(DoubleResult.PV, instrumentCalculator.calculatePresentValue(instrument, snapshot.getVolume(), market, evaluationDate)); // Calculate and add the result to the snapshot
    }

}
