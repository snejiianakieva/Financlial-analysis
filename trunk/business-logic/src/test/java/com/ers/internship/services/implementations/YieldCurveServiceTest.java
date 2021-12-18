package com.ers.internship.services.implementations;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.services.abstracts.AbstractCrudServiceTest;
import com.ers.internship.services.crud.AbstractCrudService;
import com.ers.internship.services.crud.YieldCurveService;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Snejina Yanakieva
 */
public class YieldCurveServiceTest extends AbstractCrudServiceTest<YieldCurveId, YieldCurve, CrudDao<YieldCurveId, YieldCurve>, AbstractCrudService<YieldCurveId, YieldCurve>> {

    private static final Logger logger = Logger.getLogger(YieldCurveServiceTest.class.getName());

    public static YieldCurve make(String currency, String date, String validFrom, String validTo,
            double zy3m, double zy6m, double zy1y, double zy2y, double zy5y, double zy10y, double zy30y) {
        YieldCurve yieldCurve = new YieldCurve();
        Calendar yieldCurveDate = Calendar.getInstance();
        yieldCurveDate.setTime(Timestamp.valueOf(date));
        yieldCurve.setID(new YieldCurveId(currency, yieldCurveDate));
        yieldCurve.setValidFrom(Timestamp.valueOf(validFrom));
        yieldCurve.setValidTo(Timestamp.valueOf(validTo));
        yieldCurve.setZeroYieldThreeMonths(zy3m);
        yieldCurve.setZeroYieldSixMonths(zy6m);
        yieldCurve.setZeroYieldOneYear(zy1y);
        yieldCurve.setZeroYieldTwoYears(zy2y);
        yieldCurve.setZeroYieldFiveYears(zy5y);
        yieldCurve.setZeroYieldTenYears(zy10y);
        yieldCurve.setZeroYieldThirtyYears(zy30y);
        return yieldCurve;
    }

    public YieldCurve makeAndStore(String currency, String date, String validFrom, String validTo,
            double zy3m, double zy6m, double zy1y, double zy2y, double zy5y, double zy10y, double zy30y) {
        YieldCurve yieldCurve = make(currency, date, validFrom, validTo, zy3m, zy6m, zy1y, zy2y, zy5y, zy10y, zy30y);
        save(yieldCurve);
        return yieldCurve;
    }

    @Override
    protected void assertEqual(String message, YieldCurve first, YieldCurve second) {
        Assert.assertNotNull(message, first);
        Assert.assertNotNull(message, second);
        Assert.assertEquals(message, first.getID(), second.getID());
        Assert.assertEquals(message, first.getZeroYieldThreeMonths(), second.getZeroYieldThreeMonths());
        Assert.assertEquals(message, first.getZeroYieldSixMonths(), second.getZeroYieldSixMonths());
        Assert.assertEquals(message, first.getZeroYieldOneYear(), second.getZeroYieldOneYear());
        Assert.assertEquals(message, first.getZeroYieldTwoYears(), second.getZeroYieldTwoYears());
        Assert.assertEquals(message, first.getZeroYieldFiveYears(), second.getZeroYieldFiveYears());
        Assert.assertEquals(message, first.getZeroYieldTenYears(), second.getZeroYieldTenYears());
        Assert.assertEquals(message, first.getZeroYieldThirtyYears(), second.getZeroYieldThirtyYears());
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        dao = persistentStore.getYieldCurveDao();
        service = new YieldCurveService(persistentStore);
        cleanup();
    }

    @Test
    public void createTest() {
        makeAndStore("EUR", "2007-09-23 00:00:00.0", "2007-09-23 00:00:00.0", "2012-09-23 00:00:00.0", 4.0, 4.2, 4.8, 6.6, 8, 10, 12);
    }

    @Test
    public void updateTest() {
        createTest();
    }

    @Test
    public void deleteTest() {
        createTest();
        YieldCurveId id = new YieldCurveId("EUR", DayCountConvention.getCalendarInstance(2007, 9, 23));
        delete(id);
    }

    @Test
    public void loadByIdTest() {
        createTest();
        YieldCurveId id = new YieldCurveId("EUR", DayCountConvention.getCalendarInstance(2007, 9, 23));
        load(id, DayCountConvention.getCalendarInstance(2012, 9, 23));
    }

}
