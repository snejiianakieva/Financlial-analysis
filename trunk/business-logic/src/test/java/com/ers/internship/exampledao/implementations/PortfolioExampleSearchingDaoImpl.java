package com.ers.internship.exampledao.implementations;

import com.ers.internship.exampledao.abstracts.ExampleSearchingDao;
import com.ers.internship.portfolio.Portfolio;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class PortfolioExampleSearchingDaoImpl extends ExampleSearchingDao<String, Portfolio> {

    private static final Logger logger = Logger.getLogger(PortfolioExampleSearchingDaoImpl.class.getName());

    @Override
    protected String getName(Portfolio entity) {
        return entity.getName();
    }
}
