package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.instruments.Share;
import com.ers.internship.portfolio.Portfolio;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class IntegrationShareTest extends IntegrationAbstractTest<String> {
	private Share testShare = new Share();

	@Override
	public HistorizedItem<String> createEntity() {
		testShare.setCurrency("BGN");
		testShare.setID("shareId");
		testShare.setIsin("ISIN");
		testShare.setMarket("market");
		return testShare;
	}

	@Override
	protected HistorizedItem<String> readResponseEntity(Response response) {
		return response.readEntity(Share.class);
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<String> expected, HistorizedItem<String> actual) {
		assertEquals("ID is not the same", expected.getID(), actual.getID());
		assertEquals("Isin is not the same", ((Share) expected).getIsin(), ((Share) actual).getIsin());
		assertEquals("Currency is not the same", ((Share) expected).getCurrency(), ((Share) actual).getCurrency());
		assertEquals("Market is not the same", ((Share) expected).getMarket(), ((Share) actual).getMarket());

	}

	@Override
	protected HistorizedItem<String> changeEntity() {
		testShare.setMarket("newMarket");
		return testShare;
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
		client.replacePath("/instrument/searchByName/"+testShare.getIsin());
		Response response = client.get();
		Assert.assertEquals("instrument calculation service returned response code "
          + response.getStatus() + " expected " + Status.OK.getStatusCode(),
           response.getStatus(), Status.OK.getStatusCode());
		List<Share> actualEntity = response.readEntity(List.class);
		Assert.assertTrue("Instrument calculation service returned response code ",actualEntity.size()>0);
		stop();

	}

}
