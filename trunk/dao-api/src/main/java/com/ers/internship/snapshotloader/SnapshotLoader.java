package com.ers.internship.snapshotloader;

import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author Snejina Yanakieva
 */
public interface SnapshotLoader<E> {
/**
 * Returns the current condition of a portfolio by given portfolio id.
 * 
 * @param portfolioId portfolio id
 * @param date date
 * @return list of entities
 */
	public List<E> loadSnapshots(String portfolioId, Calendar date);
	
	public E loadSnapshot(String positionId, Calendar date);
}
