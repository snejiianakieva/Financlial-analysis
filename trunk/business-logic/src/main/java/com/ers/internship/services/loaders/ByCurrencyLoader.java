package com.ers.internship.services.loaders;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class ByCurrencyLoader implements PortfolioLoader {

    private static final Logger logger = Logger.getLogger(ByCurrencyLoader.class.getName());

    private PersistentStore persistentStore;

    public ByCurrencyLoader(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    public PersistentStore getPersistentStore() {
        return persistentStore;
    }

    public void setPersistentStore(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    private Map<String, List<PositionSnapshot>> mapSnapshotsByCurrency(List<PositionSnapshot> positionSnapshots) {
        Map<String, List<PositionSnapshot>> map = new HashMap<>();
        String currency;
        for (PositionSnapshot positionSnapshot : positionSnapshots) {
            currency = positionSnapshot.getPosition().getInstrument().getCurrency();
            if (!map.containsKey(currency)) {
                map.put(currency, new ArrayList<PositionSnapshot>());
            }
            map.get(currency).add(positionSnapshot);
        }
        return map;
    }

    private void fillSnapshot(PortfolioSnapshot rootPortfolioSnapshot, Map<String, List<PositionSnapshot>> map) {
        String name = rootPortfolioSnapshot.getName();
        String currency;
        List<PositionSnapshot> positions;
        for (Map.Entry<String, List<PositionSnapshot>> entry : map.entrySet()) {
            currency = entry.getKey();
            positions = entry.getValue();
            PortfolioSnapshot currentPortfolioSnapshot = new PortfolioSnapshot();
            currentPortfolioSnapshot.setCurrency(currency);
            currentPortfolioSnapshot.setName(name + "(" + currency + ")");
            for (PortfolioItem item : positions) {
                currentPortfolioSnapshot.addItem(item);
            }
            rootPortfolioSnapshot.addItem(currentPortfolioSnapshot);
        }
    }

    /**
     * Loads a portfolio snapshot with ID and for date specified by the input parameters.
     * Creates sub-portfolios for each currency in the portfolio positions and distributes
     * the positions to their respective sub-portfolio. This structure is then returned.
     * 
     * @param id
     * @param date
     * @return portfolio snapshot whose positions are divided in sub-portfolios according to their currency
     */
    @Override
    public PortfolioSnapshot loadSnapshot(String id, Calendar date) {
        Portfolio portfolio = getPersistentStore().getPortfolioDao().loadById(id, date);
        List<PositionSnapshot> positionSnapshots = getPersistentStore().getPositionSnapshotLoader().loadSnapshots(id, date);
        Map<String, List<PositionSnapshot>> map = mapSnapshotsByCurrency(positionSnapshots);
        PortfolioSnapshot rootPortfolioSnapshot = new PortfolioSnapshot();
        rootPortfolioSnapshot.setId(id);
        rootPortfolioSnapshot.setName(portfolio.getName());
        rootPortfolioSnapshot.setCurrency(portfolio.getCurrency());
        fillSnapshot(rootPortfolioSnapshot, map);
        return rootPortfolioSnapshot;
    }

}
