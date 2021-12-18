package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.FxQuote;

public class IntegrationFxQuoteTest extends IntegrationAbstractTest<FxQuoteId> {
	private FxQuote testFxQuote = new FxQuote();

	@Override
	protected HistorizedItem<FxQuoteId> changeEntity() {
		testFxQuote.setValue(8);
		return testFxQuote;
	}

	@Override
	protected HistorizedItem<FxQuoteId> readResponseEntity(Response response) {
		return response.readEntity(FxQuote.class);
	}

	@Override
	protected HistorizedItem<FxQuoteId> createEntity() {
		FxQuoteId fxId = new FxQuoteId();
		fxId.setAtDate(new GregorianCalendar(2014,7,14));
		fxId.setFromCurrency("BGN");
		fxId.setToCurrency("USD");
		testFxQuote.setID(fxId);
		testFxQuote.setValue(6);
		return testFxQuote;
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<FxQuoteId> expected, HistorizedItem<FxQuoteId> actual) {
		assertEquals("From currnecy is not the same", expected.getID().getFromCurrency(),
				actual.getID().getFromCurrency());
		assertEquals("To currency is not the same", expected.getID().getToCurrency(), 
				actual.getID().getToCurrency());
		assertEquals("Value is not the same", ((FxQuote) expected).getValue(), ((FxQuote) actual).getValue(), 0.1);
	}

	@Override
	protected String getLoadPath(HistorizedItem<FxQuoteId> entity, GregorianCalendar date) {
		return "/fxQuote/loadById/" + entity.getID().getFromCurrency() + "/" + entity.getID().getToCurrency() + "/"
				+ entity.getID().getAtDate().getTimeInMillis() + "/" + date.getTimeInMillis();
	}

	@Override
	protected String getDeletePath(HistorizedItem<FxQuoteId> entity) {
		return "/fxQuote/delete/" + entity.getID().getFromCurrency() + "/" + entity.getID().getToCurrency() + "/"
				+ entity.getID().getAtDate().getTimeInMillis();
	}

	@Test
	public void test() {
		start();
		makeTest("fxQuote");
		stop();
	}

}
