package com.ers.internship.services.crud.searching;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class PortfolioService extends AbstractSearchingService<String, Portfolio> {

    private static final Logger logger = Logger.getLogger(PortfolioService.class.getName());

    public PortfolioService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected SearchingDao<String, Portfolio> getDao() {
        return getPersistentStore().getPortfolioDao();
    }

    @Override
    protected List<String> validateItem(Portfolio portfolio) {
        List<String> result = new ArrayList<>();
        validateStringNotEmpty(result, portfolio.getID(), "Portfolio identificator not found or empty");
        validateStringNotEmpty(result, portfolio.getName(), "Portfolio name not found or empty");
        validateStringNotEmpty(result, portfolio.getCurrency(), "Portfolio currency not found or empty");
        return result;
    }

}
