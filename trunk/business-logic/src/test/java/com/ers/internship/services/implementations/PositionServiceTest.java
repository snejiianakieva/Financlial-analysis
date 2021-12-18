package com.ers.internship.services.implementations;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.position.Position;
import com.ers.internship.services.abstracts.AbstractSearchingServiceTest;
import com.ers.internship.services.crud.searching.AbstractSearchingService;
import com.ers.internship.services.crud.searching.PositionService;
import java.sql.Timestamp;
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
public class PositionServiceTest extends AbstractSearchingServiceTest<String, Position, SearchingDao<String, Position>, AbstractSearchingService<String, Position>> {

    private static final Logger logger = Logger.getLogger(PositionServiceTest.class.getName());

    public static Position make(String id, String validFrom, String validTo, String name, String longSide, String shortSide, String portfolioId, Instrument instrument) {
        Position position = new Position();
        position.setID(id);
        position.setValidFrom(Timestamp.valueOf(validFrom));
        position.setValidTo(Timestamp.valueOf(validTo));
        position.setName(name);
        position.setLongSide(longSide);
        position.setShortSide(shortSide);
        position.setPortfolioId(portfolioId);
        position.setInstrument(instrument);
        return position;
    }

    public Position makeAndStore(String id, String validFrom, String validTo, String name, String longSide, String shortSide, String portfolioId, Instrument instrument) {
        Position position = make(id, validFrom, validTo, name, longSide, shortSide, portfolioId, instrument);
        save(position);
        return position;
    }

    @Override
    protected void assertEqual(String message, Position first, Position second) {
        Assert.assertNotNull(message, first);
        Assert.assertNotNull(message, second);
        Assert.assertEquals(message, first.getID(), second.getID());
        Assert.assertEquals(message, first.getName(), second.getName());
        Assert.assertEquals(message, first.getShortSide(), second.getShortSide());
        Assert.assertEquals(message, first.getLongSide(), second.getLongSide());
        Assert.assertEquals(message, first.getPortfolioId(), second.getPortfolioId());
        Assert.assertEquals(message, first.getInstrument().getID(), second.getInstrument().getID());
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        dao = persistentStore.getPositionDao();
        service = new PositionService(persistentStore);
        cleanup();
    }

    @Test
    public void createTest() {
        Instrument instrument = InstrumentServiceTest.makeShare("123456", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "7H1$1$AN1$1N", "EUROPE", "EUR");
        Position position = makeAndStore("000001", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MyFirstPosition", "Me", "Them", "MyPortfolio", instrument);
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
        Instrument instrument = InstrumentServiceTest.makeShare("123456", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "7H1$1$AN1$1N", "EUROPE", "EUR");
        Position position;
        position = makeAndStore("000001", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MyFirstPosition", "Me", "Them", "MyPortfolio", instrument);
        position = makeAndStore("000002", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MySecondPosition", "Me", "Them", "MyPortfolio", instrument);
        position = makeAndStore("000003", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "TheirFirstPosition", "Him", "Her", "TheirPortfolio", instrument);
        List<Position> result = searchByName("My");
        assertTrue("Search returned an unexpected number of results: " + result.size(), result.size() == 2);
    }

}
