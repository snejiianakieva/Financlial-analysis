package com.ers.internship.exampledao.abstracts;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.historization.HistorizedItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 * @param <IdType>
 * @param <T>
 */
public abstract class ExampleCrudDao<IdType, T extends HistorizedItem<IdType>> implements CrudDao<IdType, T> {

    private static final Logger logger = Logger.getLogger(ExampleCrudDao.class.getName());

    protected List<T> items = new ArrayList<>();

    protected boolean isValidOnDate(T entity, Calendar date) {
        return !entity.getValidFrom().after(date.getTime()) && !entity.getValidTo().before(date.getTime());
    }

    @Override
    public void save(T historizedItem) {
        items.add(historizedItem);
    }

    @Override
    public void delete(IdType id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getID().equals(id)) {
                items.remove(i);
                i--;
            }
        }
    }

    @Override
    public T loadById(IdType id, Calendar date) {
        T result = null;
        for (T item : items) {
            if (item.getID().equals(id) && isValidOnDate(item, date)) {
                result = item;
                break;
            }
        }
        return result;
    }

    public void clear() {
        items.clear();
    }

}
