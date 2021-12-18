package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.portfolio.Portfolio;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class IntegrationTimeDepositTest extends IntegrationAbstractTest<String> {
	private TimeDeposit timeDepositTest = new TimeDeposit();

	@Override
	public HistorizedItem<String> createEntity() {
		timeDepositTest.setCurrency("USD");
		timeDepositTest.setID("tdId");
		timeDepositTest.setInterestRate(5);
		timeDepositTest.setIsin("ISIN");
		timeDepositTest.setIssue(new GregorianCalendar());
		timeDepositTest.setMarket("tdMarket");
		timeDepositTest.setTenorMonths(6);
		return timeDepositTest;
	}

	@Override
	protected HistorizedItem<String> readResponseEntity(Response response) {
		return response.readEntity(TimeDeposit.class);
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<String> expected, HistorizedItem<String> actual) {
		assertEquals("ID is not the same", expected.getID(), actual.getID());
		assertEquals("Isin is not the same", ((TimeDeposit) expected).getIsin(), ((TimeDeposit) actual).getIsin());
		assertEquals("Currency is not the same", ((TimeDeposit) expected).getCurrency(),
				((TimeDeposit) actual).getCurrency());
		assertEquals("Issue is not the same", ((TimeDeposit) expected).getIssue(), ((TimeDeposit) actual).getIssue());
		assertEquals("Market is not the same", ((TimeDeposit) expected).getMarket(),
				((TimeDeposit) actual).getMarket());
		assertEquals("Interest rate is not the same", ((TimeDeposit) expected).getInterestRate(),
				((TimeDeposit) actual).getInterestRate(), 0.1);

	}

	@Override
	protected HistorizedItem<String> changeEntity() {
		timeDepositTest.setInterestRate(4);
		return timeDepositTest;
	}

	@Override
	protected String getLoadPath(HistorizedItem<String> entity, GregorianCalendar date) {
		return "/instrument/loadById/" + entity.getID() + "/" + date.getTimeInMillis();
	}
	
	@Override
	protected String getDeletePath(HistorizedItem<String> entity) {
		return "/instrument/delete/" + entity.getID();
	}
	@Test
	public void test() {
		start();
		makeTest("instrument");
		client.replacePath("/instrument/searchByName/"+timeDepositTest.getIsin());
		Response response = client.get();
		Assert.assertEquals("Instrument calculation service returned response code "
          + response.getStatus() + " expected " + Status.OK.getStatusCode(),
           response.getStatus(), Status.OK.getStatusCode());
		List<TimeDeposit> actualEntity = response.readEntity(List.class);
		Assert.assertTrue("Instrument calculation service returned response code ",actualEntity.size()>0);
		stop();
	}

}
