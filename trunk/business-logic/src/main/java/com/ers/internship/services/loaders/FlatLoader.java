package com.ers.internship.services.loaders;

import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.dao.CrudDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class FlatLoader implements PortfolioLoader {

    private static final Logger logger = Logger.getLogger(FlatLoader.class.getName());

    private PersistentStore persistentStore;

    public FlatLoader(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    public PersistentStore getPersistentStore() {
        return persistentStore;
    }

    public void setPersistentStore(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    /**
     * Loads and returns a portfolio snapshot with ID and for date specified by the input parameters.
     * 
     * @param id
     * @param date
     * @return
     */
    @Override
    public PortfolioSnapshot loadSnapshot(String id, Calendar date) {
        Portfolio portfolio = getPersistentStore().getPortfolioDao().loadById(id, date);
        PortfolioSnapshot portfolioSnapshot = new PortfolioSnapshot();
        portfolioSnapshot.setId(id);
        portfolioSnapshot.setName(portfolio.getName());
        portfolioSnapshot.setCurrency(portfolio.getCurrency());
        CrudDao<String, Portfolio> portfolioDao = getPersistentStore().getPortfolioDao();
        List<PositionSnapshot> positionSnapshots = getPersistentStore().getPositionSnapshotLoader().loadSnapshots(id, date);
        for (PositionSnapshot positionSnapshot : positionSnapshots) {
            portfolioSnapshot.addItem(positionSnapshot);
        }
        return portfolioSnapshot;
    }

}
