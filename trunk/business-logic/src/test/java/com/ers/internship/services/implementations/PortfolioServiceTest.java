package com.ers.internship.services.implementations;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.services.abstracts.AbstractSearchingServiceTest;
import com.ers.internship.services.crud.searching.AbstractSearchingService;
import com.ers.internship.services.crud.searching.PortfolioService;
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
public class PortfolioServiceTest extends AbstractSearchingServiceTest<String, Portfolio, SearchingDao<String, Portfolio>, AbstractSearchingService<String, Portfolio>> {

    private static final Logger logger = Logger.getLogger(PortfolioServiceTest.class.getName());

    public static Portfolio make(String id, String validFrom, String validTo, String name, String currency) {
        Portfolio portfolio = new Portfolio();
        portfolio.setID(id);
        portfolio.setValidFrom(Timestamp.valueOf(validFrom));
        portfolio.setValidTo(Timestamp.valueOf(validTo));
        portfolio.setName(name);
        portfolio.setCurrency(currency);
        return portfolio;
    }

    public Portfolio makeAndStore(String id, String validFrom, String validTo, String name, String currency) {
        Portfolio portfolio = make(id, validFrom, validTo, name, currency);
        save(portfolio);
        return portfolio;
    }

    @Override
    protected void assertEqual(String message, Portfolio first, Portfolio second) {
        Assert.assertNotNull(message, first);
        Assert.assertNotNull(message, second);
        Assert.assertTrue(message, first.getID().equals(second.getID()));
        Assert.assertTrue(message, first.getCurrency().equals(second.getCurrency()));
        Assert.assertTrue(message, first.getName().equals(second.getName()));
        Assert.assertTrue(message, first.getValidFrom().equals(second.getValidFrom()));
        Assert.assertTrue(message, first.getValidTo().equals(second.getValidTo()));
    }

    @Before
    public void prepare() {
        persistentStore = new ExamplePersistentStore();
        dao = persistentStore.getPortfolioDao();
        service = new PortfolioService(persistentStore);
        cleanup();
    }

    @Test
    public void createTest() {
        Portfolio portfolio = makeAndStore("000001", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MyFirstPortfolio", "BGN");
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
        Portfolio portfolio;
        portfolio = makeAndStore("000001", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MyFirstPortfolio", "BGN");
        portfolio = makeAndStore("000002", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "MySecondPortfolio", "EUR");
        portfolio = makeAndStore("000003", "2007-09-23 00:00:00.0", "2017-09-23 00:00:00.0", "TheirFirstPortfolio", "USD");
        List<Portfolio> result = searchByName("My");
        assertTrue("Search returned an unexpected number of results: " + result.size(), result.size() == 2);
    }

}
