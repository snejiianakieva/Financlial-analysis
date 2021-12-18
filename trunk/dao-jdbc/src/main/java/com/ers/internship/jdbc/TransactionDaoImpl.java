
package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.ers.internship.dao.*;
import com.ers.internship.transaction.*;

/**
 *
 * @author Snejina Yanakieva
 *
 * 
 */
public class TransactionDaoImpl extends AbstractCrudDao<String, Transaction>implements TransactionDao {

	private static final String entityTableName = "IDB_Transaction";
	private static final String stateTableName = "IDB_Transaction_STATE";
	private static final String[] entityTableColumnNames = { "transaction_id" };

	protected TransactionDaoImpl(JdbcPersistentStore store) {
		super(store);
	}

	@Override
	public void save(Transaction transaction) {
		String[] stateTableColumnNames = { "name", "volume", "paidAmount", "currency", "sender", "receiver", "positionId" };
		save(transaction.getID(), entityTableName, stateTableName, stateTableColumnNames, transaction, entityTableColumnNames);
	}

	@Override
	public void delete(String id) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		delete(idKey, stateTableName);
	}

	@Override
	public Transaction loadById(String Id, Calendar date) {
		String idKey = getEntityTablePrimaryKey(Id, entityTableName, entityTableColumnNames);
		return loadById(idKey, date, stateTableName, entityTableName);
	}

	@Override
	Transaction createEntity(ResultSet rs) throws SQLException {
		Transaction transactionDB = new Transaction();
		transactionDB.setID(rs.getString("transaction_id"));
		transactionDB.setValidFrom(rs.getTimestamp("valid_from"));
		transactionDB.setValidTo(rs.getTimestamp("valid_to"));
		transactionDB.setName(rs.getString("name"));
		transactionDB.setVolume(rs.getDouble("volume"));
		transactionDB.setPaidAmount(rs.getDouble("paidAmount"));
		transactionDB.setCurrency(rs.getString("currency"));
		transactionDB.setSender(rs.getString("sender"));
		transactionDB.setReceiver(rs.getString("receiver"));
		transactionDB.setPositionId(rs.getString("positionId"));
		return transactionDB;
	}

	private String joinTables(String table, String fColumn, String sColumn) {
		String query = "";
		query = query + " join " + table + " on " + fColumn + " = " + sColumn;
		return query;
	}
	
	private String getPortfolioTransactionsSelectQuery()
	{
		String query = "SELECT * FROM IDB_transaction_state ts ";
		query = query + joinTables("IDB_transaction t", "t.id", "ts.entity_id");
		query = query + joinTables("IDB_position p", "p.position_id", "ts.positionId ");
		query = query + joinTables("IDB_position_State ps", "p.id", "ps.entity_id");
		query = query + " WHERE ps.portfolioid = ? and (? BETWEEN ts.valid_from AND ts.valid_to) ";
		return query;
	}

	@Override
	public List<Transaction> loadPortfolioTransactions(String portfolioId, Calendar date) {
		List<Transaction> transactions = new LinkedList<Transaction>();
		String query = getPortfolioTransactionsSelectQuery();
		try (PreparedStatement stmt = store.getConnection().prepareStatement(query)) {
			stmt.setString(1, portfolioId);
			Timestamp currentDate = new Timestamp(date.getTimeInMillis());
			stmt.setTimestamp(2, currentDate);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Transaction tempTransaction = createEntity(rs);
				transactions.add(tempTransaction);
			}
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return transactions;
	}

	@Override
	public List<Transaction> searchByName(String name) {
		List<Transaction> transactions = new LinkedList<Transaction>();
		String query = "SELECT * FROM " + stateTableName + " join " + entityTableName + " on id=entity_id WHERE name like ?";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(query)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Transaction tempTransaction = createEntity(rs);
				transactions.add(tempTransaction);
			}
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return transactions;
	}

	@Override
	protected void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex, Transaction entity)
			throws SQLException {
		stmt.setString(columnIndex++, entity.getName());
		stmt.setDouble(columnIndex++, entity.getVolume());
		stmt.setDouble(columnIndex++, entity.getPaidAmount());
		stmt.setString(columnIndex++, entity.getCurrency());
		stmt.setString(columnIndex++, entity.getSender());
		stmt.setString(columnIndex++, entity.getReceiver());
		stmt.setString(columnIndex++, entity.getPositionId());
	}

	@Override
	protected void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, String id) throws SQLException {
		stmt.setString(columnIndex++, id);
	}
}