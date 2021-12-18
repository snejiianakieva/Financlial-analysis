package com.ers.internship.services.crud;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.results.LoadResult;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Provides entity validation and access to CRUD functionality through the DAOs in the persistent store
 *
 * @author Snejina Yanakieva
 * @param <IdType>
 */
// TODO handle SQL Exceptions
public abstract class AbstractCrudService<IdType, T> {

    private PersistentStore persistentStore;

    public AbstractCrudService(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    public PersistentStore getPersistentStore() {
        return persistentStore;
    }

    public void setPersistentStore(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
    }

    /**
     * Performs the delete operation through the DAO
     * 
     * @param id
     * @return empty list of errors if operation is successful, list is filled if an error occurred
     */
    public List<String> delete(IdType id) {
        List<String> errors = new ArrayList<>();

        if (id == null) {
            errors.add("ID is null");
            return errors;
        }

        getPersistentStore().startTransaction();
        try {
            getDao().delete(id);
        } catch (Exception e) {
            errors.add(e.getMessage());
            getPersistentStore().rollbackTransaction();
        }
        getPersistentStore().commitTransaction();

        return errors;
    }

    /**
     * Performs the update operation through the DAO
     * 
     * @param item
     * @return  empty list of errors if operation is successful, list is filled if an error occurred
     */
    public List<String> update(T item) {
        List<String> errors = validateItem(item);

        if (errors.size() > 0) {
            return errors;
        }

        getPersistentStore().startTransaction();
        try {
            getDao().save(item);
        } catch (Exception e) {
            errors.add(e.getMessage());
            getPersistentStore().rollbackTransaction();
        }
        getPersistentStore().commitTransaction();

        return errors;
    }

    /**
     * Performs the create operation through the DAO
     * 
     * @param item
     * @return  empty list of errors if operation is successful, list is filled if an error occurred
     */
    public List<String> create(T item) {
        return this.update(item);
    }

    /**
     * Loads an entity from the DAO accordingly to the input ID and date
     * 
     * @param id
     * @param date
     * @return 
     */
    public LoadResult<T> loadById(IdType id, Calendar date) {
        LoadResult<T> result = new LoadResult<>();
        try {
            T resultEntity = getDao().loadById(id, date);
            result.setEntity(resultEntity);
            if (resultEntity == null) {
                result.addError("No entity with this ID for the date given");
            }
        } catch (Exception e) {
            result.addError(e.getMessage());
        }
        return result;
    }

    protected abstract CrudDao<IdType, T> getDao();

    protected boolean validateNumberPositive(List<String> errors, double num, String message) {
        if (num <= 0) {
            errors.add(message);
            return false;
        }
        return true;
    }

    protected boolean validateStringNotEmpty(List<String> errors, String str, String message) {
        if (str == null || str.isEmpty()) {
            errors.add(message);
            return false;
        }
        return true;
    }

    protected boolean validateNotNull(List<String> errors, Object o, String message) {
        if (o == null) {
            errors.add(message);
            return false;
        }
        return true;
    }

    protected abstract List<String> validateItem(T item);
    
}
