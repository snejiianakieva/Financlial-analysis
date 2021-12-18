package com.ers.internship.exampledao;

import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.dao.FxQuoteDao;
import com.ers.internship.dao.InstrumentPriceQuoteDao;
import com.ers.internship.dao.SearchingDao;
import com.ers.internship.dao.TransactionDao;
import com.ers.internship.dao.YieldCurveDao;
import com.ers.internship.exampledao.implementations.FxQuoteExampleCrudDaoImpl;
import com.ers.internship.exampledao.implementations.InstrumentExampleSearchingDaoImpl;
import com.ers.internship.exampledao.implementations.InstrumentPriceQuoteExampleCrudDaoImpl;
import com.ers.internship.exampledao.implementations.PortfolioExampleSearchingDaoImpl;
import com.ers.internship.exampledao.implementations.PositionExampleSearchingDaoImpl;
import com.ers.internship.exampledao.implementations.TransactionExampleSearchingDaoImpl;
import com.ers.internship.exampledao.implementations.YieldCurveExampleCrudDaoImpl;
import com.ers.internship.exampledao.loaders.ExamplePositionSnapshotLoader;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.snapshotloader.SnapshotLoader;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class ExamplePersistentStore implements PersistentStore {

    private static final Logger logger = Logger.getLogger(ExamplePersistentStore.class.getName());

    private final SnapshotLoader<PositionSnapshot> positionSnapshotLoader;
    private final PortfolioExampleSearchingDaoImpl portfolioExampleSearchingDaoImpl;
    private final PositionExampleSearchingDaoImpl positionExampleSearchingDaoImpl;
    private final TransactionExampleSearchingDaoImpl transactionExampleSearchingDaoImpl;
    private final InstrumentExampleSearchingDaoImpl instrumentExampleSearchingDaoImpl;
    private final YieldCurveExampleCrudDaoImpl yieldCurveExampleCrudDaoImpl;
    private final FxQuoteExampleCrudDaoImpl fxQuoteExampleCrudDaoImpl;
    private final InstrumentPriceQuoteExampleCrudDaoImpl instrumentPriceQuoteExampleCrudDaoImpl;

    public ExamplePersistentStore() {
        positionSnapshotLoader = new ExamplePositionSnapshotLoader(this);
        portfolioExampleSearchingDaoImpl = new PortfolioExampleSearchingDaoImpl();
        positionExampleSearchingDaoImpl = new PositionExampleSearchingDaoImpl();
        transactionExampleSearchingDaoImpl = new TransactionExampleSearchingDaoImpl(positionExampleSearchingDaoImpl);
        instrumentExampleSearchingDaoImpl = new InstrumentExampleSearchingDaoImpl();
        yieldCurveExampleCrudDaoImpl = new YieldCurveExampleCrudDaoImpl();
        fxQuoteExampleCrudDaoImpl = new FxQuoteExampleCrudDaoImpl();
        instrumentPriceQuoteExampleCrudDaoImpl = new InstrumentPriceQuoteExampleCrudDaoImpl();
    }

    @Override
    public SearchingDao<String, Portfolio> getPortfolioDao() {
        return portfolioExampleSearchingDaoImpl;
    }

    @Override
    public TransactionDao getTransactionDao() {
        return transactionExampleSearchingDaoImpl;
    }

    @Override
    public SearchingDao<String, Position> getPositionDao() {
        return positionExampleSearchingDaoImpl;
    }

    @Override
    public FxQuoteDao getFxQuoteDao() {
        return fxQuoteExampleCrudDaoImpl;
    }

    @Override
    public InstrumentPriceQuoteDao getInstrumentPriceQuoteDao() {
        return instrumentPriceQuoteExampleCrudDaoImpl;
    }

    @Override
    public SearchingDao<String, Instrument> getInstrumentDao() {
        return instrumentExampleSearchingDaoImpl;
    }

    @Override
    public YieldCurveDao getYieldCurveDao() {
        return yieldCurveExampleCrudDaoImpl;
    }

    @Override
    public void rollbackTransaction() {
        //System.out.println("Rolled back transaction");
    }

    @Override
    public void startTransaction() {
        //System.out.println("Started transaction");
    }

    @Override
    public void commitTransaction() {
        //System.out.println("Committed transaction");
    }

    @Override
    public void dropDB() {
        instrumentExampleSearchingDaoImpl.clear();
        portfolioExampleSearchingDaoImpl.clear();
        positionExampleSearchingDaoImpl.clear();
        transactionExampleSearchingDaoImpl.clear();
        fxQuoteExampleCrudDaoImpl.clear();
        yieldCurveExampleCrudDaoImpl.clear();
        instrumentPriceQuoteExampleCrudDaoImpl.clear();
    }

    @Override
    public void createDB() {
        // Maps are already initialized so this method
        // should do nothing
    }

    @Override
    public void close() {
        // Garbage collector should
        // take care of that for us
    }

    @Override
    public SnapshotLoader<PositionSnapshot> getPositionSnapshotLoader() {
        return positionSnapshotLoader;
    }

}
