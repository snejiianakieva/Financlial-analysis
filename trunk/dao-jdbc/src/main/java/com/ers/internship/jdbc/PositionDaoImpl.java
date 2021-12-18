
package com.ers.internship.jdbc;

import com.ers.internship.position.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.ers.internship.dao.*;
import com.ers.internship.instruments.Instrument;

/**
 *
 * @author Snejina Yanakieva
 *
 * 
 */
public class PositionDaoImpl extends AbstractCrudDao<String, Position>implements SearchingDao<String, Position> {

	private static final String entityTableName = "IDB_Position";
	private static final String stateTableName = "IDB_Position_STATE";
	private static final String[] entityTableColumnNames = { "position_id" };

	public PositionDaoImpl(JdbcPersistentStore store) {
		super(store);
	}

	@Override
	public void save(Position position) {
		String[] stateTableColumnNames = { "name", "longSide", "shortSide", "portfolioId", "instrument" };
		save(position.getID(), entityTableName, stateTableName, stateTableColumnNames, position, entityTableColumnNames);

	}

	@Override
	public void delete(String id) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		if (idKey != null) {
			delete(idKey, stateTableName);
		}
	}

	@Override
	public Position loadById(String Id, Calendar date) {
		String id = getEntityTablePrimaryKey(Id, entityTableName, entityTableColumnNames);
		if (id != null) {
			return loadById(id, date, stateTableName, entityTableName);
		}
		return null;
	}

	@Override
	public List<Position> searchByName(String name) {
		List<Position> listPositions = new LinkedList<>();
		String query = "SELECT * FROM " + stateTableName + " join " + entityTableName + " ON entity_id=id WHERE name LIKE ?";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(query)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Position tempPosition = createEntity(rs);
				listPositions.add(tempPosition);
			}
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return listPositions;
	}

	@Override
	protected Position createEntity(ResultSet rs) throws SQLException {
		Position positionDB = new Position();
		setPositionProperties(positionDB, rs);
		Instrument instrument = getInstrument(rs.getString("instrument"), positionDB.getValidFrom());
		positionDB.setInstrument(instrument);
		return positionDB;
	}
	void setPositionProperties(Position pos, ResultSet rs) throws SQLException{
		pos.setID(rs.getString("position_id"));
		pos.setValidFrom(rs.getTimestamp("valid_from"));
		pos.setValidTo(rs.getTimestamp("valid_to"));
		pos.setName(rs.getString("name"));
		pos.setLongSide(rs.getString("longSide"));
		pos.setShortSide(rs.getString("shortSide"));
		pos.setPortfolioId(rs.getString("portfolioId"));
	}
	
	private Instrument getInstrument(String id, Timestamp tDate) {
		InstrumentDaoImpl instrumentDao = new InstrumentDaoImpl(store);
		Calendar date = new GregorianCalendar();
		date.setTime(tDate);
		return instrumentDao.loadById(id, date, "IDB_Instrument_STATE", "IDB_Instrument");
	}

	@Override
	protected void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex, Position position)
			throws SQLException {
		stmt.setString(columnIndex++, position.getName());
		stmt.setString(columnIndex++, position.getLongSide());
		stmt.setString(columnIndex++, position.getShortSide());
		stmt.setString(columnIndex++, position.getPortfolioId());
		String idPKey = getInstrumentIdPrimaryKey(position.getInstrument().getID());
		stmt.setString(columnIndex++, idPKey);
	}

	private String getInstrumentIdPrimaryKey(String instrumentId) {
		InstrumentDaoImpl instrumentDao = new InstrumentDaoImpl(store);
		String[] columnName = { "instrument_id" };
		return instrumentDao.getEntityTablePrimaryKey(instrumentId, "IDB_Instrument", columnName);
	}

	@Override
	protected void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, String id) throws SQLException {
		stmt.setString(columnIndex++, id);
	}

}
