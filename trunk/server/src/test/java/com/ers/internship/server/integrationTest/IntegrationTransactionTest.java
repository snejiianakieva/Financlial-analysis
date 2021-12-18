package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.transaction.Transaction;

public class IntegrationTransactionTest extends IntegrationAbstractTest<String> {
	private Transaction testTransaction = new Transaction();

	@Override
	protected HistorizedItem<String> changeEntity() {
		testTransaction.setID("newTrId");
		testTransaction.setReceiver("shortSide");
		testTransaction.setSender("newLongSide");
		testTransaction.setVolume(100);
		return testTransaction;
	}

	@Override
	protected HistorizedItem<String> readResponseEntity(Response response) {
		return response.readEntity(Transaction.class);
	}

	@Override
	protected HistorizedItem<String> createEntity() {
		testTransaction.setCurrency("BGN");
		testTransaction.setID("TrId");
		testTransaction.setName("TransactionTest");
		testTransaction.setPaidAmount(500);
		testTransaction.setPositionId("IdPos");
		testTransaction.setReceiver("newLongSide");
		testTransaction.setSender("shortSide");
		testTransaction.setVolume(200);
		return testTransaction;
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<String> expected, HistorizedItem<String> actual) {
		assertEquals("ID is not the same", expected.getID(), actual.getID());
		assertEquals("Currency is not the same", ((Transaction) expected).getCurrency(),
				((Transaction) actual).getCurrency());
		assertEquals("Paid amount side is not the same", ((Transaction) expected).getPaidAmount(),
				((Transaction) actual).getPaidAmount(), 0.1);
		assertEquals("Name is not the same", ((Transaction) expected).getName(), ((Transaction) actual).getName());
		assertEquals("Position is not the same", ((Transaction) expected).getPositionId(),
				((Transaction) actual).getPositionId());
		assertEquals("Receiver side rate is not the same", ((Transaction) expected).getReceiver(),
				((Transaction) actual).getReceiver());
	}

	@Override
	protected String getLoadPath(HistorizedItem<String> entity, GregorianCalendar date) {
		return "/transaction/loadById/" + entity.getID() + "/" + date.getTimeInMillis();
	}
	
	@Override
	protected String getDeletePath(HistorizedItem<String> entity) {
		return "/transaction/delete/" + entity.getID();
	}
	@Test
	public void test() {
		start();
		makeTest("transaction");
		client.replacePath("/transaction/searchByName/"+testTransaction.getName());
		Response response = client.get();
		Assert.assertEquals("Transaction calculation service returned response code "
          + response.getStatus() + " expected " + Status.OK.getStatusCode(),
           response.getStatus(), Status.OK.getStatusCode());
		List<Transaction> actualEntity = response.readEntity(List.class);
		Assert.assertTrue("Transaction calculation service returned response code ",actualEntity.size()>0);
		stop();
	}
}
