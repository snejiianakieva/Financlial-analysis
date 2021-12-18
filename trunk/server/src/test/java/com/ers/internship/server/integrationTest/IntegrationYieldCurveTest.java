package com.ers.internship.server.integrationTest;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.YieldCurve;

public class IntegrationYieldCurveTest extends IntegrationAbstractTest<YieldCurveId> {
	private YieldCurve testYieldCurve = new YieldCurve();

	@Override
	protected HistorizedItem<YieldCurveId> changeEntity() {
		testYieldCurve.setCurrency("BGN");
		return testYieldCurve;
	}

	@Override
	protected HistorizedItem<YieldCurveId> readResponseEntity(Response response) {
		return response.readEntity(YieldCurve.class);
	}

	@Override
	protected HistorizedItem<YieldCurveId> createEntity() {
		YieldCurveId Yid = new YieldCurveId();
		Yid.setAtDate(new GregorianCalendar(2014,7,14));
		Yid.setCurrency("EUR");
		testYieldCurve.setID(Yid);
		testYieldCurve.setCurrency("EUR");
		testYieldCurve.setDate(new GregorianCalendar());
		testYieldCurve.setZeroYieldFiveYears(3);
		testYieldCurve.setZeroYieldOneYear(3.5);
		testYieldCurve.setZeroYieldSixMonths(3);
		testYieldCurve.setZeroYieldTenYears(4);
		testYieldCurve.setZeroYieldThirtyYears(5);
		testYieldCurve.setZeroYieldThreeMonths(3);
		testYieldCurve.setZeroYieldTwoYears(3.3);
		return testYieldCurve;
	}

	@Override
	protected void assertEntityEquals(HistorizedItem<YieldCurveId> expected, HistorizedItem<YieldCurveId> actual) {
		assertEquals("ID is not the same", expected.getID().getCurrency(), actual.getID().getCurrency());
		assertEquals("Date is not the same", ((YieldCurve) expected).getDate(), ((YieldCurve) actual).getDate());
		assertEquals("Currency is not the same", ((YieldCurve) expected).getCurrency(),
				((YieldCurve) actual).getCurrency());
		assertEquals("Five years yield is not the same", ((YieldCurve) expected).getZeroYieldFiveYears(),
				((YieldCurve) actual).getZeroYieldFiveYears(), 0.1);
		assertEquals("One year yield is not the same", ((YieldCurve) expected).getZeroYieldOneYear(),
				((YieldCurve) actual).getZeroYieldOneYear(), 0.1);
		assertEquals("Six months yield rate is not the same", ((YieldCurve) expected).getZeroYieldSixMonths(),
				((YieldCurve) actual).getZeroYieldSixMonths(), 0.1);
	}

	@Override
	protected String getLoadPath(HistorizedItem<YieldCurveId> entity, GregorianCalendar date) {
		return "/yieldcurve/loadById/" + entity.getID().getCurrency() + "/"
				+ entity.getID().getAtDate().getTimeInMillis() + "/" + date.getTimeInMillis();
	}
	@Override
	protected String getDeletePath(HistorizedItem<YieldCurveId> entity) {
		return "/yieldcurve/delete/" + entity.getID().getCurrency() + "/"
				+ entity.getID().getAtDate().getTimeInMillis();
	}

	@Test
	public void test() {
		start();
		makeTest("yieldcurve");
		stop();
	}

}
