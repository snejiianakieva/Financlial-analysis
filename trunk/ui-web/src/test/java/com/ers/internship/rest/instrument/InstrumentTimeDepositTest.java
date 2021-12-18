package com.ers.internship.rest.instrument;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.utility.EntityFactory;

/**
 * @author Snejina Yanakieva
 *
 */
public class InstrumentTimeDepositTest extends InstrumentRestServiceTest<TimeDeposit> {

    @Override
    protected List<Instrument> generateValidEntities() {
        List<Instrument> timeDepositList = new ArrayList<Instrument>(TEST_ENTITIES_COUNT);

        for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
            timeDepositList.add(EntityFactory.makeValidTimeDeposit(null));
        }

        return timeDepositList;
    }

    @SuppressWarnings("static-access")
    @Override
    protected List<Instrument> generateInvalidEntities() {
        TimeDeposit timeDeposit = new TimeDeposit();

        timeDeposit.setCurrency(null);
        timeDeposit.setID("1");
        timeDeposit.setInterestRate(3.4);
        timeDeposit.setIsin("");
        timeDeposit.setIssue(DayCountConvention.getCalendarInstance(2015, 8, 5));
        timeDeposit.setMarket(null);
        timeDeposit.setTenorMonths(12);
        timeDeposit.setValidFrom(java.sql.Timestamp.valueOf("2015-7-27 23:59:00"));
        timeDeposit.setValidTo(java.sql.Timestamp.valueOf("2017-7-27 23:59:00"));

        List<Instrument> timeDepositList = new ArrayList<Instrument>();
        timeDepositList.add(timeDeposit);

        return timeDepositList;
    }

    protected void assertEqualsTimeDeposit(String message, TimeDeposit expected, TimeDeposit actual) {
        assertEquals(message, expected, actual);
        Assert.assertEquals("Issues are not equals!", expected.getIssue(), actual.getIssue());
        Assert.assertEquals("Tenor months are not equals!", expected.getTenorMonths(), actual.getTenorMonths());
        Assert.assertEquals("Interest rates are not equals!", expected.getInterestRate(), actual.getInterestRate(), 0.1);
    }

    @Override
    protected void updateEntity(Instrument entity) {
        entity.setCurrency("USD");
    }

    @Override
    protected Class<Instrument> getType() {
        return Instrument.class;
    }

    @Override
    protected boolean isEqual(Instrument a, Instrument b) {
        if (!InstrumentRestServiceTest.deepEquals(a, b)) {
            return false;
        }

        if ((a instanceof TimeDeposit) && (b instanceof TimeDeposit)) {
            TimeDeposit depositA = (TimeDeposit) a,
                    depositB = (TimeDeposit) b;

            if (depositA.getInterestRate() == depositB.getInterestRate()
                    && depositA.getTenorMonths() == depositB.getTenorMonths()
                    && checkIfEqual(depositA.getIssue(), depositB.getIssue())) {

                return true;
            }

            return false;
        }

        return false;
    }
}
