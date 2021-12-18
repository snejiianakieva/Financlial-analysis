package com.ers.internship.exampledao.abstracts;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.historization.HistorizedItem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Snejina Yanakieva
 * @param <IdType>
 * @param <T>
 */
public abstract class ExampleSearchingDao<IdType, T extends HistorizedItem<IdType>> extends ExampleCrudDao<IdType, T> implements SearchingDao<IdType, T> {

    protected abstract String getName(T entity);

    @Override
    public List<T> searchByName(String name) {
        List<T> results = new ArrayList<>();
        if (name.equals("*")) {
            return items;
        }
        for (T item : items) {
            if (getName(item).contains(name)) {
                results.add(item);
            }
        }
        return results;
    }

}
