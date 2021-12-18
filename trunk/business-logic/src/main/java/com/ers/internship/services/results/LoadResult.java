package com.ers.internship.services.results;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a result from a service method. Contains the
 * result from the method execution and a list of errors
 * 
 * @param <E> the type of the result returned by the service
 *
 * @author Snejina Yanakieva
 */
public class LoadResult<E> {

    private static final Logger LOGGER = Logger.getLogger(LoadResult.class.getName());

    private E entity;
    private List<String> errors;

    public LoadResult() {
    }

    public LoadResult(E entity, List<String> errors) {
        this.entity = entity;
        this.errors = errors;
    }

    /**
     * Returns the entity result from the service method execution. This
     * result should be considered valid if the error list is empty or null
     * 
     * @return The result from the service method execution
     */
    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    /**
     * Returns the errors that occured during the execution
     * of the service method. If no errors occured this method
     * returns either an empty list or null
     * 
     * @return A list of errors that occured
     */
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

}
