package com.ers.internship.exampledao.implementations;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.dao.TransactionDao;
import com.ers.internship.exampledao.abstracts.ExampleSearchingDao;
import com.ers.internship.position.Position;
import com.ers.internship.transaction.Transaction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class TransactionExampleSearchingDaoImpl extends ExampleSearchingDao<String, Transaction> implements TransactionDao {

    private static final Logger logger = Logger.getLogger(TransactionExampleSearchingDaoImpl.class.getName());

    private SearchingDao<String, Position> positionDao;

    public TransactionExampleSearchingDaoImpl(SearchingDao<String, Position> positionDao) {
        this.positionDao = positionDao;
    }

    @Override
    protected String getName(Transaction entity) {
        return entity.getName();
    }

    @Override
    public List<Transaction> loadPortfolioTransactions(String portfolioId, Calendar date) {
        List<Transaction> transactions = new ArrayList<>();
        List<Position> positions = getPositionDao().searchByName("*");

        for (Transaction transaction : items) {
            for (Position position : positions) {
                if (position.getID().equals(transaction.getPositionId())) {
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    public SearchingDao<String, Position> getPositionDao() {
        return positionDao;
    }

    public void setPositionDao(SearchingDao<String, Position> positionDao) {
        this.positionDao = positionDao;
    }
}
