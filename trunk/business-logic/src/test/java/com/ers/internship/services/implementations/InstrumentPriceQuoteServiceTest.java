package com.ers.internship.services.implementations;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.services.abstracts.AbstractCrudServiceTest;
import com.ers.internship.services.crud.AbstractCrudService;
import com.ers.internship.services.crud.InstrumentPriceQuoteService;
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
public class InstrumentPriceQuoteServiceTest extends AbstractCrudServiceTest<InstrumentPriceQuoteId, InstrumentPriceQuote, CrudDao<InstrumentPriceQuoteId, InstrumentPriceQuote>, AbstractCrudService<InstrumentPriceQuoteId, InstrumentPriceQuote>> {

    private static final Logger logger = Logger.getLogger(InstrumentPriceQuoteServiceTest.class.getName());

    public static InstrumentPriceQuote make(String instrumentId, Calendar atDate, String validFrom, String validTo, double price, String currency) {
        InstrumentPriceQuote instrumentPriceQuote = new InstrumentPriceQuote();
        InstrumentPriceQuoteId id = new InstrumentPriceQuoteId(instrumentId, atDate);
        instrumentPriceQuote.setID(id);
        instrumentPriceQuote.setValidFrom(Timestamp.valueOf(validFrom));
        instrumentPriceQuote.setValidTo(Timestamp.valueOf(validTo));
        instrumentPriceQuote.setInstrumentPrice(price);
        instrumentPriceQuote.setCurrency(currency);
        return instrumentPriceQuote;
    }

    public InstrumentPriceQuote makeAndStore(String instrumentId, Calendar atDate, String validFrom, String validTo, double price, String currency) {
        InstrumentPriceQuote instrumentPriceQuote = make(instrumentId, atDate, validFrom, validTo, price, currency);
        save(instrumentPriceQuote);
        return instrumentPriceQuote;
    }

    @Override
    protected void assertEqual(String message, InstrumentPriceQuote first, InstrumentPriceQuote second) {
        Assert.assertNotNull(message, first);
        Assert.assertNotNull(message, second);
        Assert.assertEquals(message, first.getID(), second.getID());
        Assert.assertEquals(message, first.getInstrumentPrice(), second.getInstrumentPrice());
        Assert.assertEquals(message, first.getCurrency(), second.getCurrency());
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        dao = persistentStore.getInstrumentPriceQuoteDao();
        service = new InstrumentPriceQuoteService(persistentStore);
        cleanup();
    }

    @Test
    public void createTest() {
        makeAndStore("000001", DayCountConvention.getCalendarInstance(2007, 9, 23), "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", 1200, "EUR");
    }

    @Test
    public void updateTest() {
        createTest();
    }

    @Test
    public void deleteTest() {
        createTest();
        InstrumentPriceQuoteId id = new InstrumentPriceQuoteId("000001", DayCountConvention.getCalendarInstance(2007, 9, 23));
        delete(id);
    }

    @Test
    public void loadByIdTest() {
        createTest();
        InstrumentPriceQuoteId id = new InstrumentPriceQuoteId("000001", DayCountConvention.getCalendarInstance(2007, 9, 23));
        load(id, DayCountConvention.getCalendarInstance(2012, 9, 23));
    }

}
