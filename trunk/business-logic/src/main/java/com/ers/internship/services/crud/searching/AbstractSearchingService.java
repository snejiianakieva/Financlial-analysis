package com.ers.internship.services.crud.searching;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.crud.AbstractCrudService;
import com.ers.internship.services.results.LoadResults;
import java.util.List;

/**
 * Extends the functionality of the AbstractCrudService by adding a searching method where possible
 *
 * @author Snejina Yanakieva
 * @param <IdType>
 */
public abstract class AbstractSearchingService<IdType, E> extends AbstractCrudService<IdType, E> {

    public AbstractSearchingService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected abstract SearchingDao<IdType, E> getDao();

    /**
     * Searches the DAO for entities whose name contains the input string
     * 
     * @param name
     * @return 
     */
    public LoadResults<E> searchByName(String name) {
        LoadResults<E> results = new LoadResults<>();
        try {
            List<E> resultList = getDao().searchByName(name);
            if (resultList == null) {
                results.addError("No entity name matches the search criteria");
            } else {
                results.setEntities(resultList);
            }
        } catch (Exception e) {
            results.addError(e.getMessage());
        }
        return results;
    }

}
