package com.ers.internship.services.implementations;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.services.abstracts.AbstractSearchingServiceTest;
import com.ers.internship.services.crud.searching.AbstractSearchingService;
import com.ers.internship.services.crud.searching.TransactionService;
import com.ers.internship.transaction.Transaction;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;
import junit.framework.Assert;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Snejina Yanakieva
 */
public class TransactionServiceTest extends AbstractSearchingServiceTest<String, Transaction, SearchingDao<String, Transaction>, AbstractSearchingService<String, Transaction>> {

    private static final Logger logger = Logger.getLogger(TransactionServiceTest.class.getName());

    public static Transaction make(String id, String validFrom, String validTo, String name, double volume, double paidAmount, String currency, String sender, String receiver, String positionId) {
        Transaction transaction = new Transaction();
        transaction.setID(id);
        transaction.setValidFrom(Timestamp.valueOf(validFrom));
        transaction.setValidTo(Timestamp.valueOf(validTo));
        transaction.setName(name);
        transaction.setVolume(volume);
        transaction.setPaidAmount(paidAmount);
        transaction.setCurrency(currency);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setPositionId(positionId);
        return transaction;
    }

    public Transaction makeAndStore(String id, String validFrom, String validTo, String name, double volume, double paidAmount, String currency, String sender, String receiver, String positionId) {
        Transaction transaction = make(id, validFrom, validTo, name, volume, paidAmount, currency, sender, receiver, positionId);
        save(transaction);
        return transaction;
    }

    @Override
    protected void assertEqual(String message, Transaction first, Transaction second) {
        Assert.assertNotNull(message, first);
        Assert.assertNotNull(message, second);
        Assert.assertEquals(message, first.getID(), second.getID());
        Assert.assertEquals(message, first.getName(), second.getName());
        Assert.assertEquals(message, first.getVolume(), second.getVolume());
        Assert.assertEquals(message, first.getPaidAmount(), second.getPaidAmount());
        Assert.assertEquals(message, first.getCurrency(), second.getCurrency());
        Assert.assertEquals(message, first.getSender(), second.getSender());
        Assert.assertEquals(message, first.getReceiver(), second.getReceiver());
        Assert.assertEquals(message, first.getPositionId(), second.getPositionId());
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        dao = persistentStore.getTransactionDao();
        service = new TransactionService(persistentStore);
        cleanup();
    }

    @Test
    public void createTest() {
        makeAndStore("000001", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MyFirstTransaction", 1200, 24000, "EUR", "Them", "Me", "54321");
    }

    @Test
    public void updateTest() {
        createTest();
    }

    @Test
    public void deleteTest() {
        createTest();
        delete("000001");
    }

    @Test
    public void loadByIdTest() {
        createTest();
        load("000001", DayCountConvention.getCalendarInstance(2012, 9, 23));
    }

    @Test
    public void searchByNameTest() {
        makeAndStore("000001", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MyFirstTransaction", 1200, 24000, "EUR", "Them", "Me", "54321");
        makeAndStore("000002", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MySecondTransaction", 1200, 24000, "EUR", "Me", "Them", "54321");
        makeAndStore("000003", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "OtherTransaction", 1200, 24000, "EUR", "Him", "Her", "12345");
        List<Transaction> result = searchByName("My");
        assertTrue("Search returned an unexpected number of results: " + result.size(), result.size() == 2);
    }

}
