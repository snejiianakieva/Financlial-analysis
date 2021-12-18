package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;

import com.ers.internship.enums.Frequency;
import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.portfolio.Portfolio;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class IntegrationCreditRegularTest extends IntegrationAbstractTest<String> {
	private CreditRegular testCreditRegular = new CreditRegular();

	@Override
	public HistorizedItem<String> createEntity() {
		testCreditRegular.setCurrency("BGN");
		testCreditRegular.setFrequency(Frequency.FOUR_MONTHS);
		testCreditRegular.setID("creditId");
		testCreditRegular.setInterestRate(6);
		testCreditRegular.setIsin("ISIN");
		testCreditRegular.setIssue(new GregorianCalendar());
		testCreditRegular.setMarket("market");
		testCreditRegular.setMaturity(new GregorianCalendar(2020, 4, 20));
		testCreditRegular.setTenorMonths(6);
		return testCreditRegular;
	}

	@Override
	protected HistorizedItem<String> readResponseEntity(Response response) {
		return response.readEntity(CreditRegular.class);
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<String> expected, HistorizedItem<String> actual) {
		assertEquals("ID is not the same", expected.getID(), actual.getID());
		assertEquals("Isin is not the same", ((CreditRegular) expected).getIsin(), ((CreditRegular) actual).getIsin());
		assertEquals("Currency is not the same", ((CreditRegular) expected).getCurrency(),
				((CreditRegular) actual).getCurrency());
		assertEquals("Issue is not the same", ((CreditRegular) expected).getIssue(),
				((CreditRegular) actual).getIssue());
		assertEquals("Market is not the same", ((CreditRegular) expected).getMarket(),
				((CreditRegular) actual).getMarket());
		assertEquals("Interest rate is not the same", ((CreditRegular) expected).getInterestRate(),
				((CreditRegular) actual).getInterestRate(), 0.1);

	}

	@Override
	protected HistorizedItem<String> changeEntity() {
		testCreditRegular.setInterestRate(0.04);
		return testCreditRegular;
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
		client.replacePath("/instrument/searchByName/"+testCreditRegular.getIsin());
		Response response = client.get();
		Assert.assertEquals("Instrument calculation service returned response code "
          + response.getStatus() + " expected " + Status.OK.getStatusCode(),
           response.getStatus(), Status.OK.getStatusCode());
		List<CreditRegular> actualEntity = response.readEntity(List.class);
		Assert.assertTrue("Instrument calculation service returned response code ",actualEntity.size()>0);
		stop();
	}

}
