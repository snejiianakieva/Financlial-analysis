package com.ers.internship.services.results;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a list of results from a service method. Contains 
 * the results from the method execution and a list of errors
 * 
 * @param <E> the type of the result returned by the service
 *
 * @author Snejina Yanakieva
 */
public class LoadResults<E> {

    private static final Logger LOGGER = Logger.getLogger(LoadResults.class.getName());

    private List<E> entities;
    private List<String> errors;

    public LoadResults() {
    }

    public LoadResults(List<E> entities, List<String> errors) {
        this.entities = entities;
        this.errors = errors;
    }

    /**
     * Returns the entity results from the service method execution. This
     * result should be considered valid if the error list is empty or null
     * 
     * @return The results from the service method execution
     */
    public List<E> getEntities() {
        return entities;
    }

    public void setEntities(List<E> entities) {
        this.entities = entities;
    }

    public void addEntity(E entity) {
        if (entities == null) {
            entities = new ArrayList<>();
        }
        entities.add(entity);
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
