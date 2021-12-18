package com.ers.internship.services.implementations;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.enums.Frequency;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.services.abstracts.AbstractSearchingServiceTest;
import com.ers.internship.services.crud.searching.AbstractSearchingService;
import com.ers.internship.services.crud.searching.InstrumentService;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import junit.framework.Assert;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Snejina Yanakieva
 */
public class InstrumentServiceTest extends AbstractSearchingServiceTest<String, Instrument, SearchingDao<String, Instrument>, AbstractSearchingService<String, Instrument>> {

    private static final Logger logger = Logger.getLogger(InstrumentServiceTest.class.getName());

    private static void setupInstrument(Instrument instrument, String id, String validFrom,
            String validTo, String isin, String market, String currency) {
        instrument.setID(id);
        instrument.setValidFrom(Timestamp.valueOf(validFrom));
        instrument.setValidTo(Timestamp.valueOf(validTo));
        instrument.setIsin(isin);
        instrument.setMarket(market);
        instrument.setCurrency(currency);
    }

    public static Instrument makeCreditRegular(String id, String validFrom,
            String validTo, String isin, String market, String currency, int tenorMonths,
            double interestRate, Frequency frequency, Calendar issue, Calendar maturity) {
        CreditRegular instrument = new CreditRegular();
        setupInstrument(instrument, id, validFrom, validTo, isin, market, currency);
        instrument.setTenorMonths(tenorMonths);
        instrument.setInterestRate(interestRate);
        instrument.setFrequency(frequency);
        instrument.setIssue(issue);
        instrument.setMaturity(maturity);
        return instrument;
    }

    public static Instrument makeTimeDeposit(String id, String validFrom,
            String validTo, String isin, String market, String currency,
            int tenorMonths, double interestRate, Calendar issue) {
        TimeDeposit instrument = new TimeDeposit();
        setupInstrument(instrument, id, validFrom, validTo, isin, market, currency);
        instrument.setTenorMonths(tenorMonths);
        instrument.setInterestRate(interestRate);
        instrument.setIssue(issue);
        return instrument;
    }

    public static Instrument makeShare(String id, String validFrom,
            String validTo, String isin, String market, String currency) {
        Share instrument = new Share();
        setupInstrument(instrument, id, validFrom, validTo, isin, market, currency);
        return instrument;
    }

    public Instrument makeAndStoreCreditRegular(String id, String validFrom, String validTo,
            String isin, String market, String currency, int tenorMonths, double interestRate,
            Frequency frequency, Calendar issue, Calendar maturity) {
        Instrument instrument = makeCreditRegular(id, validFrom, validTo, isin, market, currency,
                tenorMonths, interestRate, frequency, issue, maturity);
        save(instrument);
        return instrument;
    }

    public Instrument makeAndStoreTimeDeposit(String id, String validFrom,
            String validTo, String isin, String market, String currency,
            int tenorMonths, double interestRate, Calendar issue) {
        Instrument instrument = makeTimeDeposit(id, validFrom, validTo, isin,
                market, currency, tenorMonths, interestRate, issue);
        save(instrument);
        return instrument;
    }

    public Instrument makeAndStoreShare(String id, String validFrom, String validTo,
            String isin, String market, String currency) {
        Instrument instrument = makeShare(id, validFrom, validTo, isin, market, currency);
        save(instrument);
        return instrument;
    }

    @Override
    protected void assertEqual(String message, Instrument first, Instrument second) {
        Assert.assertNotNull(message, first);
        Assert.assertNotNull(message, second);
        Assert.assertEquals(message, first.getID(), second.getID());
        Assert.assertEquals(message, first.getIsin(), second.getIsin());
        Assert.assertEquals(message, first.getCurrency(), second.getCurrency());
        Assert.assertEquals(message, first.getMarket(), second.getMarket());
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        dao = persistentStore.getInstrumentDao();
        service = new InstrumentService(persistentStore);
        cleanup();
    }

    @Test
    public void createTest() {
        String id = "000001";
        String validFrom = "2007-09-23 00:00:00.0";
        String validTo = "2017-09-23 00:00:00.0";
        String isin = "7H1$1$AN1$1N";
        String market = "EUROPE";
        String currency = "EUR";
        int tenorMonth = 3;
        double interestRate = 8.0;
        Frequency frequency = Frequency.THREE_MONTHS;
        Calendar issue = DayCountConvention.getCalendarInstance(2007, 9, 23);
        Calendar maturity = DayCountConvention.getCalendarInstance(2017, 9, 23);
        Instrument instrument = makeAndStoreCreditRegular(id, validFrom, validTo, isin,
                market, currency, tenorMonth, interestRate, frequency, issue, maturity);
    }

    @Test
    public void updateTest() {
        createTest();
    }

    @Test
    public void deleteTest() {
        createTest();
        delete("000001");
    }

    @Test
    public void loadByIdTest() {
        createTest();
        load("000001", DayCountConvention.getCalendarInstance(2012, 9, 23));
    }

    @Test
    public void searchByNameTest() {
        String id = "000001";
        String validFrom = "2007-09-23 00:00:00.0";
        String validTo = "2017-09-23 00:00:00.0";
        String isin = "7H1$1$AN1$1N";
        String market = "EUROPE";
        String currency = "EUR";
        int tenorMonth = 3;
        double interestRate = 8.0;
        Frequency frequency = Frequency.THREE_MONTHS;
        Calendar issue = DayCountConvention.getCalendarInstance(2007, 9, 23);
        Calendar maturity = DayCountConvention.getCalendarInstance(2017, 9, 23);

        Instrument instrument;
        instrument = makeAndStoreCreditRegular(id, validFrom, validTo, isin,
                market, currency, tenorMonth, interestRate, frequency, issue, maturity);
        id = "000002";
        isin = "ISIN12345";
        instrument = makeAndStoreCreditRegular(id, validFrom, validTo, isin,
                market, currency, tenorMonth, interestRate, frequency, issue, maturity);
        id = "000003";
        isin = "ISIN54321";
        instrument = makeAndStoreCreditRegular(id, validFrom, validTo, isin,
                market, currency, tenorMonth, interestRate, frequency, issue, maturity);

        List<Instrument> result = searchByName("ISIN");
        assertTrue("Search returned an unexpected number of results: " + result.size(), result.size() == 2);
    }

}
