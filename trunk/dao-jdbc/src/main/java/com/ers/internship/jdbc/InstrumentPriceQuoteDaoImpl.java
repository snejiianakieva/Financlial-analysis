package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.ers.internship.dao.InstrumentPriceQuoteDao;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.portfolio.Portfolio;

/**
 *
 * @author Snejina Yanakieva
 *
 */
public class InstrumentPriceQuoteDaoImpl extends AbstractCrudDao<InstrumentPriceQuoteId, InstrumentPriceQuote>
		implements InstrumentPriceQuoteDao {

	private static final String entityTableName = "IDB_InstrumentPriceQuote";
	private static final String stateTableName = "IDB_InstrumentPriceQuote_STATE";
	private static final String[] entityTableColumnNames = {"instrument_id", "at_date"};

	public InstrumentPriceQuoteDaoImpl(JdbcPersistentStore store) {
		super(store);
	}

	@Override
	public void save(InstrumentPriceQuote priceQuote) {
		String[] columnNames = {"instrument_price", "currency"};
		save(priceQuote.getID(), entityTableName, stateTableName, columnNames, priceQuote, entityTableColumnNames);

	}

	@Override
	protected void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex,
			InstrumentPriceQuote priceQuote) throws SQLException {
		stmt.setDouble(columnIndex++, priceQuote.getInstrumentPrice());
		stmt.setString(columnIndex++, priceQuote.getCurrency());

	}

	@Override
	protected InstrumentPriceQuote createEntity(ResultSet rs) throws SQLException {
		InstrumentPriceQuote priceQuote = new InstrumentPriceQuote();
		Timestamp atDate = rs.getTimestamp("at_date");
		Calendar date = new GregorianCalendar();
		date.setTimeInMillis(atDate.getTime());
		InstrumentPriceQuoteId id = new InstrumentPriceQuoteId(rs.getString("instrument_id"), date);
		priceQuote.setID(id);
		priceQuote.setValidFrom(rs.getTimestamp("valid_from"));
		priceQuote.setValidTo(rs.getTimestamp("valid_to"));
		priceQuote.setInstrumentPrice(rs.getDouble("instrument_price"));
		priceQuote.setCurrency(rs.getString("currency"));
		return priceQuote;
	}

	@Override
	public void delete(InstrumentPriceQuoteId id) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		delete(idKey, stateTableName);
	}

	@Override
	public InstrumentPriceQuote loadById(InstrumentPriceQuoteId id, Calendar date) {
		String entity_id = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		return loadById(entity_id, date, stateTableName, entityTableName);
	}

	@Override
	public InstrumentPriceQuote loadLatestPrice(String instrumentId, Calendar date) {
		String selectQuery = "SELECT * FROM " + stateTableName + " JOIN " + entityTableName
				+ " ON entity_id=id WHERE instrument_id=? AND at_date=? and valid_to=?";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery)) {
			stmt.setString(1, instrumentId);
			stmt.setTimestamp(2, new Timestamp(date.getTimeInMillis()));
			stmt.setTimestamp(3, infinityDate);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return createEntity(rs);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	protected void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, InstrumentPriceQuoteId id)
			throws SQLException {
		stmt.setString(columnIndex++, id.getInstrumentId());
		stmt.setTimestamp(columnIndex++, new Timestamp(id.getAtDate().getTimeInMillis()));

	}

	@Override
	public List<InstrumentPriceQuote> loadAll() {
		List<InstrumentPriceQuote> listEntity = new LinkedList<>();
		String selectQuery = "SELECT * FROM " + stateTableName + " JOIN " + entityTableName
				+ " ON entity_id=id";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery)) {

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				InstrumentPriceQuote temp = createEntity(rs);
				listEntity.add(temp);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return listEntity;
	}

}
