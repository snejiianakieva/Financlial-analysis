package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.market.InstrumentPriceQuote;

public class IntegrationInstrumentPriceQuoteTest extends IntegrationAbstractTest<InstrumentPriceQuoteId> {
	InstrumentPriceQuote testInsPC = new InstrumentPriceQuote();

	@Override
	protected HistorizedItem<InstrumentPriceQuoteId> changeEntity() {
		testInsPC.setCurrency("USD");
		return testInsPC;
	}

	@Override
	protected HistorizedItem<InstrumentPriceQuoteId> readResponseEntity(Response response) {
		return response.readEntity(InstrumentPriceQuote.class);
	}

	@Override
	protected HistorizedItem<InstrumentPriceQuoteId> createEntity() {
		InstrumentPriceQuoteId pcId = new InstrumentPriceQuoteId();
		pcId.setAtDate(new GregorianCalendar(2014,7,14));
		pcId.setInstrumentId("shareId");
		testInsPC.setCurrency("BGN");
		testInsPC.setID(pcId);
		testInsPC.setInstrumentPrice(4);
		return testInsPC;
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<InstrumentPriceQuoteId> expected,
			HistorizedItem<InstrumentPriceQuoteId> actual) {
		assertEquals("ID is not the same", expected.getID().getInstrumentId(), actual.getID().getInstrumentId());
		assertEquals("Date is not the same", ((InstrumentPriceQuote) expected).getDate(),
				((InstrumentPriceQuote) actual).getDate());
		assertEquals("Currency is not the same", ((InstrumentPriceQuote) expected).getCurrency(),
				((InstrumentPriceQuote) actual).getCurrency());
		assertEquals("Price is not the same", ((InstrumentPriceQuote) expected).getInstrumentPrice(),
				((InstrumentPriceQuote) actual).getInstrumentPrice(), 0.1);

	}

	@Override
	protected String getLoadPath(HistorizedItem<InstrumentPriceQuoteId> entity, GregorianCalendar date) {
		return "/instrumentPriceQuote/loadById/" + entity.getID().getInstrumentId() + "/" + 
					entity.getID().getAtDate().getTimeInMillis() + "/" + date.getTimeInMillis();
	}

	@Override
	protected String getDeletePath(HistorizedItem<InstrumentPriceQuoteId> entity) {
		return "/instrumentPriceQuote/delete/" + entity.getID().getInstrumentId() + "/" + 
				entity.getID().getAtDate().getTimeInMillis();
	}

	@Test
	public void test() {
		start();
		makeTest("instrumentPriceQuote");
		stop();
	}
}
