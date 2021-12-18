package com.ers.internship.rest.instrumenpricequote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.rest.AbstractCrudRestServiceTest;
import com.ers.internship.rest.instrumentpricequote.InstrumentPriceQuoteRestServiceImpl;
import com.ers.internship.utility.EntityFactory;

/**
 * @author Snejina Yanakieva
 *
 */
public class InstrumentPriceQuoteRestServiceTest
        extends AbstractCrudRestServiceTest<InstrumentPriceQuoteId, InstrumentPriceQuote> {

    @Override
    protected Object getService() {
        return new InstrumentPriceQuoteRestServiceImpl(persistentStore);
    }

    @Override
    protected String getRelativeCreateURI() {
        return "/instrumentPriceQuote/create";
    }

    @Override
    protected String getRelativeUpdateURI() {
        return "/instrumentPriceQuote/update";
    }

    @Override
    protected String getRelativeLoadByIdURI(InstrumentPriceQuoteId id, Calendar atDate) {
        return "/instrumentPriceQuote/loadById/" + id.getInstrumentId() + "/" + id.getAtDate().getTimeInMillis() + "/"
                + atDate.getTimeInMillis();
    }

    @Override
    protected String getRelativeDeleteByIdURI(InstrumentPriceQuoteId id) {
        return "/instrumentPriceQuote/delete/" + id.getInstrumentId() + "/" + id.getAtDate().getTimeInMillis();
    }

    @Override
    protected List<InstrumentPriceQuote> generateValidEntities() {
        List<InstrumentPriceQuote> entities = new ArrayList<>(TEST_ENTITIES_COUNT);
        Calendar startDate = DayCountConvention.getCalendarInstance(2014, 8, 13),
                currentDate;
        InstrumentPriceQuoteId id;

        for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
            currentDate = DayCountConvention.getCalendarCopy(startDate);
            DayCountConvention.incrementCalendar(currentDate, Calendar.DAY_OF_MONTH, i * 3);
            id = new InstrumentPriceQuoteId(null, currentDate);
            entities.add(EntityFactory.makeValidInstrumentPriceQuote(id));
        }

        return entities;
    }

    @SuppressWarnings("static-access")
    @Override
    protected List<InstrumentPriceQuote> generateInvalidEntities() {
        InstrumentPriceQuoteId instrumentPriceQuotedID = new InstrumentPriceQuoteId("1",
                DayCountConvention.getCalendarInstance(2115, 8, 5));

        InstrumentPriceQuote instrumentPriceQuote = new InstrumentPriceQuote(instrumentPriceQuotedID);

        instrumentPriceQuote.setCurrency(null);
        instrumentPriceQuote.setDate(DayCountConvention.getCalendarInstance(2015, 8, 5));
        instrumentPriceQuote.setID(instrumentPriceQuotedID);
        instrumentPriceQuote.setInstrumentId("");
        instrumentPriceQuote.setInstrumentPrice(-30);
        instrumentPriceQuote.setValidFrom(null);
        instrumentPriceQuote.setValidTo(null);

        List<InstrumentPriceQuote> instrumentPriceQuoteList = new ArrayList<InstrumentPriceQuote>();
        instrumentPriceQuoteList.add(instrumentPriceQuote);

        return instrumentPriceQuoteList;
    }

    @Override
    protected void assertEquals(String message, InstrumentPriceQuote expected, InstrumentPriceQuote actual) {
        Assert.assertEquals(message + "Instrument prices are not equals!", expected.getInstrumentPrice(), actual.getInstrumentPrice(), 0.1);
        Assert.assertEquals(message + "Currencies are not equals!", expected.getCurrency(), actual.getCurrency());
        Assert.assertEquals(message + "Dates are not equals!", expected.getDate(), actual.getDate());
        Assert.assertEquals(message + "IDs are not equals!", expected.getID(), actual.getID());
        Assert.assertEquals(message + "Instrument IDs are not equals!", expected.getInstrumentId(), actual.getInstrumentId());
        Assert.assertEquals(message + "Valid from are not equals!", expected.getValidFrom(), actual.getValidFrom());
        Assert.assertEquals(message + "Valid to are not equals!", expected.getValidTo(), actual.getValidTo());
    }

    @Override
    protected CrudDao<InstrumentPriceQuoteId, InstrumentPriceQuote> getDao() {
        return persistentStore.getInstrumentPriceQuoteDao();
    }

    @Override
    protected void updateEntity(InstrumentPriceQuote entity) {
        entity.setValidFrom(java.sql.Timestamp.valueOf("2017-3-13 23:59:00"));
        entity.setValidTo(java.sql.Timestamp.valueOf("2019-3-13 23:59:00"));
        entity.setInstrumentPrice(50);
    }

    @Override
    protected Class<InstrumentPriceQuote> getType() {
        return InstrumentPriceQuote.class;
    }
}
