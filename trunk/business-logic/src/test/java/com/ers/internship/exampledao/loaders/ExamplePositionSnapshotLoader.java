package com.ers.internship.exampledao.loaders;

import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.dao.SearchingDao;
import com.ers.internship.dao.TransactionDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.position.Position;
import com.ers.internship.snapshotloader.SnapshotLoader;
import com.ers.internship.transaction.Transaction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Snejina Yanakieva
 *
 */
public class ExamplePositionSnapshotLoader implements SnapshotLoader<PositionSnapshot> {

    private static final Logger logger = Logger.getLogger(ExamplePositionSnapshotLoader.class.getName());

    private final PersistentStore persistentStore;

    public ExamplePositionSnapshotLoader(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    private double getPositionVolume(Position position, List<Transaction> transactions) {
        double positionVolume = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getPositionId().equals(position.getID())) {
                if (transaction.getSender().equals(position.getLongSide())) {
                    positionVolume += transaction.getVolume();
                } else {
                    positionVolume -= transaction.getVolume();
                }
            }
        }

        return positionVolume;
    }

    @Override
    public List<PositionSnapshot> loadSnapshots(String portfolioId, Calendar atDate) {
        TransactionDao transactionDao = persistentStore.getTransactionDao();
        SearchingDao<String, Position> positionDao = persistentStore.getPositionDao();

        List<Transaction> transactions = transactionDao.loadPortfolioTransactions(portfolioId, atDate);
        List<Position> positions = positionDao.searchByName("*");

        List<PositionSnapshot> result = new ArrayList<>();

        for (Position position : positions) {
            PositionSnapshot currentPositionSnapshot = new PositionSnapshot();

            double positionVolume = getPositionVolume(position, transactions);

            if (positionVolume == 0) {
                continue;
            }

            currentPositionSnapshot.setId(position.getID());
            currentPositionSnapshot.setPosition(position);
            currentPositionSnapshot.setVolume(positionVolume);

            result.add(currentPositionSnapshot);
        }

        return result;
    }

	@Override
	public PositionSnapshot loadSnapshot(String positionId, Calendar date) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
