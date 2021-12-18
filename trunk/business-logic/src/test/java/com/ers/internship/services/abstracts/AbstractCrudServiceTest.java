package com.ers.internship.services.abstracts;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.exampledao.ExamplePersistentStore;
import com.ers.internship.exampledao.abstracts.ExampleCrudDao;
import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.services.crud.AbstractCrudService;
import com.ers.internship.services.results.LoadResult;
import java.sql.Timestamp;
import java.util.Calendar;
import junit.framework.Assert;
import org.junit.After;

/**
 *
 * @author Snejina Yanakieva
 * @param <IdType>
 * @param <T>
 * @param <Dao>
 * @param <Service>
 */
public abstract class AbstractCrudServiceTest<IdType, T extends HistorizedItem<IdType>, Dao extends CrudDao<IdType, T>, Service extends AbstractCrudService<IdType, T>> {

    protected ExamplePersistentStore persistentStore;
    protected Dao dao;
    protected Service service;

    protected abstract void assertEqual(String message, T first, T second);

    private Calendar getCenter(Timestamp from, Timestamp to) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis((from.getTime() + to.getTime()) / 2);
        return date;
    }

    public void save(T entity) {
        Assert.assertTrue("Error validating object before save", service.create(entity).isEmpty());
        Calendar date = getCenter(entity.getValidFrom(), entity.getValidTo());
        T actual = dao.loadById(entity.getID(), date);
        assertEqual("Saving has failed", entity, actual);
    }

    public T load(IdType id, Calendar date) {
        T expected = dao.loadById(id, date);
        LoadResult<T> result = service.loadById(id, date);
        T actual = result.getEntity();
        assertEqual("Loading has failed", expected, actual);
        return actual;
    }

    public void delete(IdType id) {
        T loaded = dao.loadById(id, Calendar.getInstance());
        if (loaded != null) {
            service.delete(id);
            loaded = dao.loadById(id, Calendar.getInstance());
            Assert.assertNull("Delete has failed", loaded);
        }
    }

    @After
    public void cleanup() {
        ((ExampleCrudDao<IdType,T>) dao).clear();
    }

}
