
package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.ers.internship.dao.*;
import com.ers.internship.instrumentdbconnector.InstrumentDBConnector;
import com.ers.internship.instruments.*;

/**
 *
 * @author Snejina Yanakieva
 *
 * 
 */
public class InstrumentDaoImpl extends AbstractCrudDao<String, Instrument>implements SearchingDao<String, Instrument> {

	private static final String entityTableName = "IDB_Instrument";
	private static final String stateTableName = "IDB_Instrument_STATE";
	private static final String[] entityTableColumnNames = { "instrument_id" };

	public InstrumentDaoImpl(JdbcPersistentStore store) {
		super(store);
	}

	@Override
	public void delete(String id) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		if (idKey != null) {
			delete(idKey, stateTableName);
		}
	}

	private String[] getColumnNames(Instrument instrument) {
		String[] columnNames = { "instrument_type", "isin", "currency", "market" };
		InstrumentDBConnector res = new InstrumentResolver().getDao(instrument.getClass().getSimpleName());
		String[] instrumentColumnNames = res.getInstrumentColumnNames();
		String[] allColumnNames = new String[instrumentColumnNames.length + columnNames.length];
		System.arraycopy(columnNames, 0, allColumnNames, 0, columnNames.length);
		System.arraycopy(instrumentColumnNames, 0, allColumnNames, columnNames.length, instrumentColumnNames.length);
		return allColumnNames;
	}

	@Override
	public void save(Instrument instrument) {
		String[] columnNames = getColumnNames(instrument);
		save(instrument.getID(), entityTableName, stateTableName, columnNames, instrument, entityTableColumnNames);
	}

	@Override
	public Instrument loadById(String id, Calendar date) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		if (idKey != null) {
			return loadById(idKey, date, stateTableName, entityTableName);
		}
		return null;
	}

	@Override
	public List<Instrument> searchByName(String name) {
		List<Instrument> listEntity = new LinkedList<Instrument>();
		String selectQuery = "SELECT * FROM " + stateTableName + " join " + entityTableName
				+ " on id=entity_id  WHERE isin LIKE ?";
		try (PreparedStatement stmt = this.store.getConnection().prepareStatement(selectQuery)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Instrument tempInstrument = createEntity(rs);
				listEntity.add(tempInstrument);
			}
			stmt.close();
		} catch (SQLException e) {
			this.printSQLExceptionMessage(e);
		}
		return listEntity;
	}

	@Override
	protected Instrument createEntity(ResultSet rs) throws SQLException {
		InstrumentDBConnector res = new InstrumentResolver().getDao(rs.getString("instrument_type"));
		return res.createInstrument(rs);
	}

	@Override
	protected void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex, Instrument entity)
			throws SQLException {
		stmt.setString(columnIndex++, entity.getClass().getSimpleName());
		stmt.setString(columnIndex++, entity.getIsin());
		stmt.setString(columnIndex++, entity.getCurrency());
		stmt.setString(columnIndex++, entity.getMarket());
		InstrumentDBConnector res = new InstrumentResolver().getDao(entity.getClass().getSimpleName());
		res.setInstrumentInStatement(stmt, columnIndex, entity);
	}

	@Override
	protected void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, String id) throws SQLException {
		stmt.setString(columnIndex, id);
	}
}
