package com.ers.internship.exampledao.loaders;

import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.dao.CrudDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.services.loaders.PortfolioLoader;
import com.ers.internship.snapshotloader.SnapshotLoader;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Snejina Yanakieva
 *
 */
public class ExampleFlatLoader implements PortfolioLoader {

    private static final Logger logger = Logger.getLogger(ExampleFlatLoader.class.getName());

    private final CrudDao<String, Portfolio> portfolioDao;
    private final SnapshotLoader<PositionSnapshot> positionLoader;

    public ExampleFlatLoader(PersistentStore persistentStore) {
        portfolioDao = persistentStore.getPortfolioDao();
        positionLoader = persistentStore.getPositionSnapshotLoader();
    }

    @Override
    public PortfolioSnapshot loadSnapshot(String portfolioId, Calendar atDate) {
        PortfolioSnapshot result = new PortfolioSnapshot();
        List<PositionSnapshot> positionSnapshots = positionLoader.loadSnapshots(portfolioId, atDate);
        Portfolio portfolio = portfolioDao.loadById(portfolioId, atDate);

        result.setCurrency(portfolio.getCurrency());
        result.setName(portfolio.getName());
        result.setId(portfolio.getID());

        for (PositionSnapshot positionSnapshot : positionSnapshots) {
            result.addItem(positionSnapshot);
        }

        return result;
    }
    
}
