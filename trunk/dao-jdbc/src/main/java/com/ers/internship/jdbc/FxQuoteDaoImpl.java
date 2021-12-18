package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

//import org.omg.PortableServer.IdAssignmentPolicy;

import com.ers.internship.dao.FxQuoteDao;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.FxQuote;

/**
 * @author Snejina Yanakieva
 */
public class FxQuoteDaoImpl extends AbstractCrudDao<FxQuoteId, FxQuote>implements FxQuoteDao {

	private static final String entityTableName = "IDB_FxQuote";
	private static final String stateTableName = "IDB_FxQuote_STATE";
	private static final String[] entityTableColumnNames = { "from_currency", "to_currency", "at_date" };

	protected FxQuoteDaoImpl(JdbcPersistentStore store) {
		super(store);
	}

	@Override
	public void save(FxQuote fxQuote) {
		String[] columnNames = { "value" };
		save(fxQuote.getID(), entityTableName, stateTableName, columnNames, fxQuote, entityTableColumnNames);
	}

	@Override
	protected void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex, FxQuote entity)
			throws SQLException {
		stmt.setDouble(columnIndex++, entity.getValue());
	}

	@Override
	protected FxQuote createEntity(ResultSet rs) throws SQLException {
		Calendar date = new GregorianCalendar();
		date.setTime(rs.getTimestamp("at_date"));
		FxQuoteId id = new FxQuoteId(rs.getString("from_currency"), rs.getString("to_currency"), date);
		FxQuote fxQuote = new FxQuote();
		fxQuote.setID(id);
		fxQuote.setValidFrom(rs.getTimestamp("valid_from"));
		fxQuote.setValidTo(rs.getTimestamp("valid_to"));
		fxQuote.setValue(rs.getDouble("value"));
		return fxQuote;
	}

	@Override
	public void delete(FxQuoteId id) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		if (idKey != null) {
			delete(idKey, stateTableName);
		}
	}

	@Override
	public FxQuote loadById(FxQuoteId id, Calendar date) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		if (idKey != null) {
			return loadById(idKey, date, stateTableName, entityTableName);
		}
		return null;
	}

	@Override
	public FxQuote loadLatest(String fromCurrency, String toCurrency, Calendar date) {
		FxQuote tempQuote = new FxQuote();
		String selectQuery = "SELECT * FROM " + stateTableName + " JOIN " + entityTableName
				+ " ON entity_id=id WHERE from_currency=? AND to_currency=? AND "
				+ "at_date = ? AND valid_to=?";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery);) {
			stmt.setString(1, fromCurrency);
			stmt.setString(2, toCurrency);
			stmt.setTimestamp(3, new Timestamp(date.getTimeInMillis()));
			stmt.setTimestamp(4, infinityDate);
			ResultSet rs = stmt.executeQuery();
			stmt.close();
			if (rs.next()) {
				tempQuote = createEntity(rs);
			}
		} catch (SQLException e) {
			printSQLExceptionMessage(e);
		}
		return tempQuote;
	}

	@Override
	protected void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, FxQuoteId id)
			throws SQLException {
		stmt.setString(columnIndex++, id.getFromCurrency());
		stmt.setString(columnIndex++, id.getToCurrency());
		stmt.setTimestamp(columnIndex++, new Timestamp(id.getAtDate().getTimeInMillis()));
	}
}
