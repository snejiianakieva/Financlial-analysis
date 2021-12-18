package com.ers.internship.dao;

import java.util.List;

/**
 * 
 * @author Snejina Yanakieva
 *
 * @param <IdType> entity's ID
 * @param <E> entity
 */
public interface SearchingDao<IdType, E> extends CrudDao<IdType, E> {
	/**
	 * method allows all entities that have name (or instrument isin) to be
	 * searched by it
	 * 
	 * @param name name of entity
	 * @return list of entities
	 */
	List<E> searchByName(String name);
}
