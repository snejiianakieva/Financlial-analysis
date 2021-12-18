package com.ers.internship.services.loaders;

import com.ers.internship.aggregation.PortfolioSnapshot;
import java.util.Calendar;

/**
 *
 * @author Snejina Yanakieva
 */
public interface PortfolioLoader {

    public PortfolioSnapshot loadSnapshot(String id, Calendar date);

}
