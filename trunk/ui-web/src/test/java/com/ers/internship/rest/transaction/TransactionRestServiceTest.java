package com.ers.internship.rest.transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.rest.AbstractCrudRestServiceTest;
import com.ers.internship.rest.transaction.TransactionRestServiceImpl;
import com.ers.internship.transaction.Transaction;
import com.ers.internship.utility.EntityFactory;

/**
 * @author Snejina Yanakieva
 *
 */

public class TransactionRestServiceTest extends AbstractCrudRestServiceTest<String, Transaction> {
	
	@Override
	protected Object getService() {
		return new TransactionRestServiceImpl(persistentStore);
	}
	
	@Override
	protected String getRelativeCreateURI() {
		return "/transaction/create";
	}

	@Override
	protected String getRelativeUpdateURI() {
		return "/transaction/update";
	}

	@Override
	protected String getRelativeLoadByIdURI(String id, Calendar atDate) {
		return "/transaction/loadById/" + id + "/" + atDate.getTimeInMillis();
	}

	@Override
	protected String getRelativeDeleteByIdURI(String id) {
		return "/transaction/delete/" + id;
	}

	@Override
	protected List<Transaction> generateValidEntities() {
		List<Transaction> transactionList = new ArrayList<Transaction>(TEST_ENTITIES_COUNT);
		
		for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
			transactionList.add(EntityFactory.makeValidTransaction(null));
		}
		
		return transactionList;
	}

	@Override
	protected List<Transaction> generateInvalidEntities() {
		Transaction transaction = new Transaction();
		
		transaction.setCurrency("BGN");
		transaction.setID(null);
		transaction.setName("transaction1");
		transaction.setPaidAmount(-50);
		transaction.setReceiver("a");
		transaction.setSender("b");
		transaction.setValidFrom(java.sql.Timestamp.valueOf("2015-7-27 23:59:00"));
		transaction.setValidTo(java.sql.Timestamp.valueOf("2015-7-27 23:59:00"));
		transaction.setVolume(-5.2);

		List<Transaction> transactionList = new ArrayList<Transaction>();
		transactionList.add(transaction);
		
		return transactionList;
	}

	@Override
	protected void assertEquals(String message, Transaction expected, Transaction actual) {
		Assert.assertEquals(message + "Currencies aren't equals!", expected.getPaidAmount(), actual.getPaidAmount(), 0.1);
		Assert.assertEquals(message + "Volumes aren't equals!", expected.getVolume(), actual.getVolume(), 0.1);
		Assert.assertEquals(message + "Currencies aren't equals!", expected.getCurrency(), actual.getCurrency());
		Assert.assertEquals(message + "IDs aren't equals!", expected.getID(), actual.getID());
		Assert.assertEquals(message + "Names aren't equals!", expected.getName(), actual.getName());
		Assert.assertEquals(message + "Receivers aren't equals!", expected.getReceiver(), actual.getReceiver());
		Assert.assertEquals(message + "Senders aren't equals!", expected.getSender(), actual.getSender());
		Assert.assertEquals(message + "Valid from isn't equals!", expected.getValidFrom(), actual.getValidFrom());
		Assert.assertEquals(message + "Valid to isn't equals!", expected.getValidTo(), actual.getValidTo());
	}

	@Override
	protected CrudDao<String, Transaction> getDao() {
		return persistentStore.getTransactionDao();
	}

	@Override
	protected void updateEntity(Transaction entity) {
		entity.setValidFrom(java.sql.Timestamp.valueOf("2016-3-13 23:59:00"));
		entity.setValidTo(java.sql.Timestamp.valueOf("2019-3-13 23:59:00"));
		entity.setCurrency("USD");
	}

	@Override
	protected Class<Transaction> getType() {
		return Transaction.class;
	}
}