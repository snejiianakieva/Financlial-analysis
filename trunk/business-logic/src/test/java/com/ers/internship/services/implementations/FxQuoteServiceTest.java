package com.ers.internship.services.implementations;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.FxQuote;
import com.ers.internship.services.abstracts.AbstractCrudServiceTest;
import com.ers.internship.services.crud.AbstractCrudService;
import com.ers.internship.services.crud.FxQuoteService;
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
public class FxQuoteServiceTest extends AbstractCrudServiceTest<FxQuoteId, FxQuote, CrudDao<FxQuoteId, FxQuote>, AbstractCrudService<FxQuoteId, FxQuote>> {

    private static final Logger logger = Logger.getLogger(FxQuoteServiceTest.class.getName());

    public static FxQuote make(String baseCurrency, String targetCurrency, Calendar atDate, String validFrom, String validTo, double value) {
        FxQuote fxQuote = new FxQuote();
        FxQuoteId id = new FxQuoteId(baseCurrency, targetCurrency, atDate);
        fxQuote.setID(id);
        fxQuote.setValidFrom(Timestamp.valueOf(validFrom));
        fxQuote.setValidTo(Timestamp.valueOf(validTo));
        fxQuote.setValue(value);
        return fxQuote;
    }

    public FxQuote makeAndStore(String baseCurrency, String targetCurrency, Calendar atDate, String validFrom, String validTo, double value) {
        FxQuote fxQuote = make(baseCurrency, targetCurrency, atDate, validFrom, validTo, value);
        save(fxQuote);
        return fxQuote;
    }

    @Override
    protected void assertEqual(String message, FxQuote first, FxQuote second) {
        Assert.assertNotNull(message, first);
        Assert.assertNotNull(message, second);
        Assert.assertEquals(message, first.getID(), second.getID());
        Assert.assertEquals(message, first.getValue(), second.getValue());
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        dao = persistentStore.getFxQuoteDao();
        service = new FxQuoteService(persistentStore);
        cleanup();
    }

    @Test
    public void createTest() {
        makeAndStore("EUR", "USD", DayCountConvention.getCalendarInstance(2007, 9, 23), "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", 1.23);
    }

    @Test
    public void updateTest() {
        createTest();
    }

    @Test
    public void deleteTest() {
        createTest();
        FxQuoteId id = new FxQuoteId("EUR", "USD", DayCountConvention.getCalendarInstance(2007, 9, 23));
        delete(id);
    }

    @Test
    public void loadByIdTest() {
        createTest();
        FxQuoteId id = new FxQuoteId("EUR", "USD", DayCountConvention.getCalendarInstance(2007, 9, 23));
        load(id, DayCountConvention.getCalendarInstance(2012, 9, 23));
    }

}
