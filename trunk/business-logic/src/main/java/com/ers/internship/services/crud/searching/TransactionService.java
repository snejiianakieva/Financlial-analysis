package com.ers.internship.services.crud.searching;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.transaction.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class TransactionService extends AbstractSearchingService<String, Transaction> {

    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());

    public TransactionService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected SearchingDao<String, Transaction> getDao() {
        return getPersistentStore().getTransactionDao();
    }

    @Override
    protected List<String> validateItem(Transaction transaction) {
        List<String> result = new ArrayList<>();
        validateStringNotEmpty(result, transaction.getID(), "Transaction identificator not found or is empty");
        validateStringNotEmpty(result, transaction.getName(), "Transaction name not found or is empty");
        validateStringNotEmpty(result, transaction.getCurrency(), "Transaction currency not found or is empty");
        validateStringNotEmpty(result, transaction.getSender(), "Transaction sender not found or is empty");
        validateStringNotEmpty(result, transaction.getReceiver(), "Transaction receiver not found or is empty");
        validateNumberPositive(result, transaction.getVolume(), "Transaction volume must be positive");
        validateNumberPositive(result, transaction.getPaidAmount(), "Transaction amount must be positive");
        return result;
    }

}
