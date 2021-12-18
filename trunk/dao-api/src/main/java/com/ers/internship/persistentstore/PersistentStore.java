package com.ers.internship.persistentstore;

import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.dao.FxQuoteDao;
import com.ers.internship.dao.InstrumentPriceQuoteDao;
import com.ers.internship.dao.SearchingDao;
import com.ers.internship.dao.TransactionDao;
import com.ers.internship.dao.YieldCurveDao;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.snapshotloader.SnapshotLoader;;

/**
 * @author Snejina Yanakieva
 *
 * All get methods use lazy instantiation.
 */
public interface PersistentStore extends Cloneable, AutoCloseable {
	
	SearchingDao<String, Portfolio> getPortfolioDao();

	TransactionDao getTransactionDao();

	SearchingDao<String, Position> getPositionDao();

	FxQuoteDao getFxQuoteDao();

	InstrumentPriceQuoteDao getInstrumentPriceQuoteDao();

	SearchingDao<String, Instrument> getInstrumentDao();

	YieldCurveDao getYieldCurveDao();

	SnapshotLoader<PositionSnapshot> getPositionSnapshotLoader();

	/**
	 * Rollback sql transaction
	 */
	void rollbackTransaction();

	/**
	 * Start sql transaction
	 */
	void startTransaction();

	/**
	 * Commit sql transaction
	 */
	void commitTransaction();

	void dropDB();

	void createDB();

}
