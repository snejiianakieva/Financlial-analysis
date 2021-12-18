package com.ers.internship.instrumentdbconnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ers.internship.instruments.Instrument;

/**
 * @author Snejina Yanakieva
 * 
 *         The interface makes a connection and records an instrument into
 *         database.
 */
public interface InstrumentDBConnector {
	/**
	 * Returns the database column names for the needed instrument type.
	 * 
	 * @return array of string
	 */
	public String[] getInstrumentColumnNames();

	/**
	 * Makes a statement that saves instrument in database.
	 * 
	 * @param stmt prepared statement
	 * @param columnIndex index of a table column
	 * @param entity entity
	 */
	public void setInstrumentInStatement(PreparedStatement stmt, int columnIndex, Instrument entity);

	public Instrument createInstrument(ResultSet rs);
}
