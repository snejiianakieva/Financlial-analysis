package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Share;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;

public class IntegrationPositionTest extends IntegrationAbstractTest<String> {
	private Position testPosition = new Position();

	@Override
	protected HistorizedItem<String> changeEntity() {
		testPosition.setLongSide("newLongSide");
		return testPosition;
	}

	@Override
	protected HistorizedItem<String> readResponseEntity(Response response) {
		return response.readEntity(Position.class);
	}

	@Override
	protected HistorizedItem<String> createEntity() {
		CreditRegular s = (CreditRegular) new IntegrationCreditRegularTest().createEntity();
		saveEntity(s, "instrument");
		testPosition.setInstrument(s);
		testPosition.setID("IdPos");
		testPosition.setLongSide("longSide");
		testPosition.setName("PosName");
		testPosition.setPortfolioId("testPId");
		testPosition.setShortSide("shortSide");
		return testPosition;
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<String> expected, HistorizedItem<String> actual) {
		assertEquals("ID is not the same", expected.getID(), actual.getID());
		assertEquals("Instrument is not the same", ((Position) expected).getInstrument().getID(),
				((Position) actual).getInstrument().getID());
		assertEquals("Long side is not the same", ((Position) expected).getLongSide(),
				((Position) actual).getLongSide());
		assertEquals("Name is not the same", ((Position) expected).getName(), ((Position) actual).getName());
		assertEquals("Portfolio is not the same", ((Position) expected).getPortfolioId(),
				((Position) actual).getPortfolioId());
		assertEquals("Short side rate is not the same", ((Position) expected).getShortSide(),
				((Position) actual).getShortSide());
	}
	@Override
	protected String getLoadPath(HistorizedItem<String> entity, GregorianCalendar date) {
		return "/position/loadById/" + entity.getID() + "/" + date.getTimeInMillis();
	}
	@Override
	protected String getDeletePath(HistorizedItem<String> entity) {
		return "/position/delete/" + entity.getID();
	}
	@Test
	public void test() {
		start();
		makeTest("position");
		client.replacePath("/position/searchByName/"+testPosition.getName());
		Response response = client.get();
		Assert.assertEquals("Position calculation service returned response code "
          + response.getStatus() + " expected " + Status.OK.getStatusCode(),
           response.getStatus(), Status.OK.getStatusCode());
		List<Position> actualEntity = response.readEntity(List.class);
		Assert.assertTrue("Position calculation service returned response code ",actualEntity.size()>0);
		stop();
	}
}
