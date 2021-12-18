package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import com.ers.internship.dao.*;
import com.ers.internship.portfolio.*;

/**
 * @author Snejina Yanakieva
 */
public class PortfolioDaoImpl extends AbstractCrudDao<String, Portfolio>implements SearchingDao<String, Portfolio> {

	private static final String entityTableName = "IDB_Portfolio";
	private static final String stateTableName = "IDB_Portfolio_STATE";
	private static final String[] entityTableColumnNames = { "portfolio_id" };

	PortfolioDaoImpl(JdbcPersistentStore store) {
		super(store);
	}

	@Override
	public void save(Portfolio portfolio) {
		String[] columnNames = { "name", "currency" };
		save(portfolio.getID(), entityTableName, stateTableName, columnNames, portfolio, entityTableColumnNames);
	}

	@Override
	public void delete(String id) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		if (idKey != null) {
			delete(idKey, stateTableName);
		}
	}

	@Override
	public Portfolio loadById(String id, Calendar date) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		if (idKey != null) {
			return loadById(idKey, date, stateTableName, entityTableName);
		}
		return null;
	}

	@Override
	public List<Portfolio> searchByName(String name) {
		List<Portfolio> listEntity = new LinkedList<Portfolio>();
		String selectQuery = "SELECT * FROM " + stateTableName + " join " + entityTableName
				+ " on id=entity_id  WHERE name like ?";
		try (PreparedStatement stmt = this.store.getConnection().prepareStatement(selectQuery)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Portfolio tempPortfolio = createEntity(rs);
				listEntity.add(tempPortfolio);
			}
			stmt.close();
		} catch (SQLException e) {
			this.printSQLExceptionMessage(e);
		}
		return listEntity;
	}

	@Override
	protected void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex, Portfolio entity)
			throws SQLException {
		stmt.setString(columnIndex++, entity.getName());
		stmt.setString(columnIndex++, entity.getCurrency());
	}

	@Override
	protected Portfolio createEntity(ResultSet rs) throws SQLException {
		Portfolio portfolioDB = new Portfolio();
		portfolioDB.setID(rs.getString("portfolio_id"));
		portfolioDB.setValidFrom(rs.getTimestamp("valid_from"));
		portfolioDB.setValidTo(rs.getTimestamp("valid_to"));
		portfolioDB.setName(rs.getString("name"));
		portfolioDB.setCurrency(rs.getString("currency"));
		return portfolioDB;
	}

	@Override
	protected void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, String id) throws SQLException {
		stmt.setString(columnIndex, id);
	}
}
