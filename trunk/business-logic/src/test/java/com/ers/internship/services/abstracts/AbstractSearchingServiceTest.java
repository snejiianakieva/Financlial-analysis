package com.ers.internship.services.abstracts;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.services.crud.searching.AbstractSearchingService;
import java.util.List;
import junit.framework.Assert;

/**
 *
 * @author Snejina Yanakieva
 * @param <IdType>
 * @param <T>
 * @param <Dao>
 * @param <Service>
 */
public abstract class AbstractSearchingServiceTest<IdType, T extends HistorizedItem<IdType>, Dao extends SearchingDao<IdType, T>, Service extends AbstractSearchingService<IdType, T>>
        extends AbstractCrudServiceTest<IdType, T, Dao, Service> {

    public List<T> searchByName(String name) {
        List<T> expected = dao.searchByName(name);
        List<T> actual = service.searchByName(name).getEntities();
        Assert.assertTrue("Result list sizes do not match", actual.size() == expected.size());
        return actual;
    }
}
