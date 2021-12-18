package com.ers.internship.jdbc;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import com.ers.internship.dao.*;
/**
 * 
 * @author Snejina Yanakieva
 *
 *         The database is historized and every entity has two tables in it.The
 *         first table IDB_"ENTITY" refers to the component entity's ID. The
 *         second table IDB_"ENTITY"_STATE is used to store all states of the
 *         entity.
 */
public abstract class AbstractCrudDao<IdType, E> implements CrudDao<IdType, E> {
	protected JdbcPersistentStore store;
	protected final Timestamp infinityDate = Timestamp.valueOf("2100-01-01 00:00:00.0"); // valid
																							// to
																							// date

	protected AbstractCrudDao(JdbcPersistentStore store) {
		this.store = store;
	}

	/**
	 * 
	 * @param id
	 *            The component entity's ID
	 * @param tableName
	 *            This is table IDB_"ENTITY"
	 * @param idTableColumnNames
	 * @return The Primary Key of table IDB_"ENTITY"
	 */
	protected String getEntityTablePrimaryKey(IdType id, String tableName, String[] idTableColumnNames) {
		String selectIdQuery = "SELECT id FROM " + tableName + " WHERE " + idTableColumnNames[0] + "=?";
		for (int i = 1; i < idTableColumnNames.length; i++) {
			selectIdQuery = selectIdQuery + " and " + idTableColumnNames[i] + "=?";
		}
		String check = null;

		try (PreparedStatement selectIdStatement = this.store.getConnection().prepareStatement(selectIdQuery)) {
			setIdPropertiesInStatement(selectIdStatement, 1, id);
			ResultSet selectedRows = selectIdStatement.executeQuery();
			if (selectedRows.next()) {
				check = selectedRows.getString(1);
			}
			selectIdStatement.close();
		} catch (SQLException e) {
			this.printSQLExceptionMessage(e);
		}
		return check;
	}

	/**
	 * 
	 * @param id
	 *            The component entity's ID
	 * @param tableName
	 *            This is table IDB_"ENTITY"
	 * @param idTableColumnNames
	 * @return The generated Primary Key of table IDB_"ENTITY"
	 * @throws SQLException
	 */
	protected String insertIdInIdbEntityTable(IdType id, String tableName, String[] idTableColumnNames)
			throws SQLException {
		String uniqueId = null;
		String insertIdQuery = "INSERT INTO " + tableName + "(id";
		for (int i = 0; i < idTableColumnNames.length; i++) {
			insertIdQuery = insertIdQuery + " , " + idTableColumnNames[i];
		}
		insertIdQuery = insertIdQuery + " ) VALUES ( ?";
		for (int i = 0; i < idTableColumnNames.length; i++) {
			insertIdQuery = insertIdQuery + ",?";
		}
		insertIdQuery = insertIdQuery + " )";
		try (PreparedStatement insertIdStatement = this.store.getConnection().prepareStatement(insertIdQuery)) {
			uniqueId = UUID.randomUUID().toString();
			insertIdStatement.setString(1, uniqueId);
			setIdPropertiesInStatement(insertIdStatement, 2, id);
			insertIdStatement.execute();
			insertIdStatement.close();
		}
		return uniqueId;
	}

	protected void printSQLExceptionMessage(SQLException ex) {
		throw new RuntimeException(" Failed connection with database:  " + ex.getMessage());
	}

	protected abstract void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex, E entity)
			throws SQLException;

	protected abstract void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, IdType id)
			throws SQLException;

	/**
	 * 
	 * @param tableName
	 *            The table is IDB_"ENTITY"_STATE
	 * @param columnNames
	 *            The properties of the entity are columns in table
	 * @param entityId
	 *            The Primary Key of IDB_"ENTITY"
	 * @param entity
	 *            The current entity
	 * @throws SQLException
	 */
	protected void insertEntityInSnapshotTable(String tableName, String[] columnNames, String entityId, E entity)
			throws SQLException {

		String insertQuery = "INSERT INTO " + tableName + "(snapshot_id, entity_id, valid_from, valid_to";
		for (int i = 0; i < columnNames.length; i++) {
			insertQuery = insertQuery + ", " + columnNames[i];
		}
		insertQuery = insertQuery + ") VALUES ( ?,?,?,?";
		for (int i = 0; i < columnNames.length; i++) {
			insertQuery = insertQuery + ",?";
		}
		insertQuery = insertQuery + ")";

		try (PreparedStatement insertStatement = this.store.getConnection().prepareStatement(insertQuery)) {

			insertStatement.setString(1, UUID.randomUUID().toString());
			insertStatement.setString(2, entityId);
			insertStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
			insertStatement.setTimestamp(4, this.infinityDate);

			setEntityPropertiesInStatement(insertStatement, 5, entity);
			insertStatement.executeUpdate();
			insertStatement.close();
		}

	}

	/**
	 * Updates an entity and the entity's state expires.
	 * 
	 * @param id
	 *            The Primary Key of IDB_"ENTITY"
	 * @param tableName
	 *            The table is IDB_"ENTITY"_STATE
	 * @throws SQLException
	 */
	protected void expireState(String id, String tableName) throws SQLException {
		String query = "UPDATE " + tableName + " SET valid_to=? WHERE entity_id=? AND valid_to=?";

		try (PreparedStatement stmt = this.store.getConnection().prepareStatement(query)) {
			stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
			stmt.setString(2, id);
			stmt.setTimestamp(3, this.infinityDate);
			stmt.execute();

			stmt.close();
		}
	}

	/**
	 * Creates or updates an entity in the database. If the item does not exist
	 * in the database it is created, else it is updated.
	 * 
	 * @param entityId
	 *            The component entity's ID
	 * @param tableName
	 *            The table is IDB_"ENTITY"
	 * @param stateTableName
	 *            The table is IDB_"ENTITY"_STATE
	 * @param columnNames
	 *            The entity's properties are columns in table
	 *            IDB_"ENTITY"_STATE
	 * @param entity
	 *            The current entity
	 * @param idTableColumnNames
	 *            The components of entity's ID are columns in table
	 *            IDB_"ENTITY"
	 */
	protected void save(IdType entityId, String tableName, String stateTableName, String[] columnNames, E entity,
			String[] idTableColumnNames) {
		try {
			String entityIdKey = this.getEntityTablePrimaryKey(entityId, tableName, idTableColumnNames);
			
			if (entityIdKey == null) {
				entityIdKey = this.insertIdInIdbEntityTable(entityId, tableName, idTableColumnNames);
			} else {
				this.expireState(entityIdKey, stateTableName);
			}
			this.insertEntityInSnapshotTable(stateTableName, columnNames, entityIdKey, entity);
		} catch (SQLException e) {
			printSQLExceptionMessage(e);
		}
	}

	/**
	 * The database is historized thus the entity is updated and it is not
	 * deleted.
	 * 
	 * @param idKey
	 *            The Primary Key of the table IDB_"ENTITY"
	 * @param tableName
	 *            This is table IDB_"ENTITY"_STATE
	 */
	protected void delete(String idKey, String tableName) {
		try {
			this.expireState(idKey, tableName);
		} catch (SQLException e) {
			printSQLExceptionMessage(e);
		}
	}

	abstract E createEntity(ResultSet rs) throws SQLException;

	/**
	 * Returns entity in a certain period of validity.
	 * 
	 * @param id
	 *            The Primary Key of the table IDB_"ENTITY"
	 * @param date
	 * @param stateTableName
	 *            This is table IDB_"ENTITY"_STATE
	 * @param entityTableName
	 *            This is table IDB_"ENTITY"
	 * @return entity
	 */
	protected E loadById(String id, Calendar date, String stateTableName, String entityTableName) {
		E loadedEntity = null;
		String sql = "SELECT * FROM " + stateTableName + " join " + entityTableName
				+ " on id=entity_id WHERE entity_id=? AND (? BETWEEN valid_from AND valid_to)";
		try (PreparedStatement stmt = this.store.getConnection().prepareStatement(sql)) {
			stmt.setString(1, id);
			Timestamp currentDate = new Timestamp(date.getTimeInMillis());
			stmt.setTimestamp(2, currentDate);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				loadedEntity = createEntity(rs);
			}
			stmt.close();
		} catch (SQLException e) {
			this.printSQLExceptionMessage(e);
		}
		return loadedEntity;
	}
}
