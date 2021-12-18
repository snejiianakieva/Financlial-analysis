package com.ers.internship.dao;

import java.util.Calendar;

/**
 * 
 * @author Snejina Yanakieva
 *
 * @param <IdType>
 *            the type of entity's ID
 * @param <E>
 *            the type of current entity
 */

public interface CrudDao<IdType, E> {
	/**
	 * Creates or updates an entity in the database. If the item does not exist
	 * in the database it is created, else it is updated.
	 * 
	 * @param object
	 *            The entity to be created or updated
	 */
	void save(E object);

	/**
	 * The database is historized thus the entity is updated and it is not
	 * deleted.
	 * 
	 * @param id
	 *            The entity's ID
	 */
	void delete(IdType id);

	/**
	 * Returns entity in a certain period of validity
	 * 
	 * @param id
	 *            The component entity's ID
	 * @param date
	 *            Date of validity
	 * @return entity
	 */
	E loadById(IdType id, Calendar date);
}
