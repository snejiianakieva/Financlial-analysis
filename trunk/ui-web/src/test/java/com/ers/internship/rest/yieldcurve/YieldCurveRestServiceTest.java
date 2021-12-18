package com.ers.internship.rest.yieldcurve;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.rest.AbstractCrudRestServiceTest;
import com.ers.internship.utility.EntityFactory;


/**
 * @author Snejina Yanakieva
 *
 */
public class YieldCurveRestServiceTest extends AbstractCrudRestServiceTest<YieldCurveId, YieldCurve> {
	
	public static void assertDeepEquals(String message,
			YieldCurve expected, YieldCurve actual) {
		double delta = 0.001;
        Assert.assertEquals(message + " currency failed", expected.getCurrency(),
                actual.getCurrency());
        Assert.assertEquals(message + " date failed", expected.getDate(), actual.getDate());
        Assert.assertEquals(message + " id failed", expected.getID(), actual.getID());
        Assert.assertEquals(message + " validFrom failed", expected.getValidFrom().getTime(),
                actual.getValidFrom().getTime());
        Assert.assertEquals(message + " validTo failed", expected.getValidTo().getTime(),
                actual.getValidTo().getTime());
        Assert.assertEquals(message + " 3m yield failed", expected.getZeroYieldThreeMonths(),
                actual.getZeroYieldThreeMonths(), delta);
        Assert.assertEquals(message + " 6m yield failed", expected.getZeroYieldSixMonths(),
                actual.getZeroYieldSixMonths(), delta);
        Assert.assertEquals(message + " 1y yield failed", expected.getZeroYieldOneYear(),
                actual.getZeroYieldOneYear(), delta);
        Assert.assertEquals(message + " 2y yield failed", expected.getZeroYieldTwoYears(),
                actual.getZeroYieldTwoYears(), delta);
        Assert.assertEquals(message + " 5y yield failed", expected.getZeroYieldFiveYears(),
                actual.getZeroYieldFiveYears(), delta);
        Assert.assertEquals(message + " 10y yield failed", expected.getZeroYieldTenYears(),
                actual.getZeroYieldTenYears(), delta);
        Assert.assertEquals(message + " 30y yield failed", expected.getZeroYieldThirtyYears(),
                actual.getZeroYieldThirtyYears(), delta);
	}
			
	@Override
	protected Object getService() {
		return new YieldCurveRestServiceImpl(persistentStore);
	}
	
	@Override
	protected String getRelativeCreateURI() {
		return "/yieldcurve/create";
	}

	@Override
	protected String getRelativeUpdateURI() {
		return "/yieldcurve/update";
	}

	@Override
	protected String getRelativeLoadByIdURI(YieldCurveId id, Calendar atDate) {
		return "/yieldcurve/loadById/" + id.getCurrency() + "/"
				+ id.getAtDate().getTimeInMillis() + "/"
				+ atDate.getTimeInMillis();
	}

	@Override
	protected String getRelativeDeleteByIdURI(YieldCurveId id) {
		return "/yieldcurve/delete/" + id.getCurrency() + "/"
				+ id.getAtDate().getTimeInMillis();
	}

	@Override
	protected Class<YieldCurve> getType() {
		return YieldCurve.class;
	}
	
	@Override
	protected List<YieldCurve> generateValidEntities() {
		List<YieldCurve> validEntities = new ArrayList<>(TEST_ENTITIES_COUNT);
		Calendar startDate = DayCountConvention.getCalendarInstance(2014, 8, 13),
				currentDate;
		
		YieldCurveId id;
		for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
			currentDate = DayCountConvention.getCalendarCopy(startDate);
			DayCountConvention.incrementCalendar(currentDate, Calendar.DAY_OF_MONTH, i * 3);
			id = new YieldCurveId(null,
					currentDate);
			validEntities.add(EntityFactory.makeValidYieldCurve(id));
		}
		
		return validEntities;
	}

	@Override
	protected List<YieldCurve> generateInvalidEntities() {
		List<YieldCurve> invalidEntities = new ArrayList<>();
		
		YieldCurve invalidEntity = new YieldCurve();
		Calendar atDate = DayCountConvention.getCalendarInstance(2000, 1, 1);
		
		invalidEntity.setID(new YieldCurveId("HOI", atDate));
		invalidEntity.setValidFrom(Timestamp.valueOf("1990-01-01 00:00:00"));
		invalidEntity.setValidTo(Timestamp.valueOf("1999-01-01 00:00:00"));
		invalidEntity.setZeroYieldFiveYears(1000);
		invalidEntity.setZeroYieldOneYear(1000);
		invalidEntity.setZeroYieldSixMonths(1000);
		invalidEntity.setZeroYieldTenYears(1000);
		invalidEntity.setZeroYieldThirtyYears(1000);
		invalidEntity.setZeroYieldThreeMonths(1000);
		
		invalidEntities.add(invalidEntity);
		
		return invalidEntities;
	}


	@Override
	protected void assertEquals(String message, YieldCurve expected, YieldCurve actual) {
		assertDeepEquals(message, expected, actual);
	}

	@Override
	protected CrudDao<YieldCurveId, YieldCurve> getDao() {
		return persistentStore.getYieldCurveDao();
	}

	@Override
	protected void updateEntity(YieldCurve entity) {
		
		double zeroYield = entity.getZeroYieldFiveYears();
		if (zeroYield + 1 < 20) {
			zeroYield += 1;
		}
		else {
			zeroYield -= 1;
		}
		
		entity.setZeroYieldFiveYears(zeroYield);
	}	
}