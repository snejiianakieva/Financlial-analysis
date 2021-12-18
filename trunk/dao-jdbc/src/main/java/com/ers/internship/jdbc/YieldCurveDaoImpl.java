package com.ers.internship.jdbc;

import java.util.GregorianCalendar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.ers.internship.dao.YieldCurveDao;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.*;

/**
 *
 * @author Snejina Yanakieva
 *
 *
 */
public class YieldCurveDaoImpl extends AbstractCrudDao<YieldCurveId, YieldCurve> implements YieldCurveDao {

	private static final String stateTableName = "IDB_YIELDCURVE_STATE";
	private static final String entityTableName = "IDB_YIELDCURVE";
	private static final String[] entityTableColumnNames = {"currency", "at_date"};

	public YieldCurveDaoImpl(JdbcPersistentStore store) {
		super(store);
	}

	@Override
	public void save(YieldCurve yieldCurve) {
		String[] stateTableColumnNames = {"three_months", "six_months", "one_year", "two_years", "five_years", "ten_years",
			"thirty_years"};
		save(yieldCurve.getID(), entityTableName, stateTableName, stateTableColumnNames, yieldCurve, entityTableColumnNames);
	}

	@Override
	public void delete(YieldCurveId id) {
		String idKey = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		delete(idKey, stateTableName);
	}

	@Override
	protected void setEntityPropertiesInStatement(PreparedStatement stmt, int columnIndex, YieldCurve yieldCurve)
			throws SQLException {
		stmt.setDouble(columnIndex++, yieldCurve.getZeroYieldThreeMonths());
		stmt.setDouble(columnIndex++, yieldCurve.getZeroYieldSixMonths());
		stmt.setDouble(columnIndex++, yieldCurve.getZeroYieldOneYear());
		stmt.setDouble(columnIndex++, yieldCurve.getZeroYieldTwoYears());
		stmt.setDouble(columnIndex++, yieldCurve.getZeroYieldFiveYears());
		stmt.setDouble(columnIndex++, yieldCurve.getZeroYieldTenYears());
		stmt.setDouble(columnIndex++, yieldCurve.getZeroYieldThirtyYears());
	}

	@Override
	protected YieldCurve createEntity(ResultSet rs) throws SQLException {
		YieldCurve yCurve = new YieldCurve();
		Timestamp atDate = rs.getTimestamp("at_date");
		Calendar date = new GregorianCalendar();
		date.setTimeInMillis(atDate.getTime());
		YieldCurveId id = new YieldCurveId(rs.getString("currency"), date);
		yCurve.setID(id);
		yCurve.setValidFrom(rs.getTimestamp("valid_from"));
		yCurve.setValidTo(rs.getTimestamp("valid_to"));
		yCurve.setZeroYieldThreeMonths(rs.getDouble("three_months"));
		yCurve.setZeroYieldSixMonths(rs.getDouble("six_months"));
		yCurve.setZeroYieldOneYear(rs.getDouble("one_year"));
		yCurve.setZeroYieldTwoYears(rs.getDouble("two_years"));
		yCurve.setZeroYieldFiveYears(rs.getDouble("five_years"));
		yCurve.setZeroYieldTenYears(rs.getDouble("ten_years"));
		yCurve.setZeroYieldThirtyYears(rs.getDouble("thirty_years"));
		return yCurve;
	}

	@Override
	public YieldCurve loadById(YieldCurveId id, Calendar date) {
		String entity_id = getEntityTablePrimaryKey(id, entityTableName, entityTableColumnNames);
		return loadById(entity_id, date, stateTableName, entityTableName);
	}

	@Override
	public YieldCurve loadLatestCurve(String currency, Calendar date) {
		YieldCurve yCurve = null;
		String selectQuery = "SELECT * FROM " + stateTableName + " JOIN " + entityTableName
				+ " ON entity_id=id WHERE currency=? AND valid_to=?";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery)) {
			stmt.setString(1, currency);
			stmt.setTimestamp(2, infinityDate);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				yCurve = createEntity(rs);
			}
			stmt.close();
		} catch (SQLException e) {
			printSQLExceptionMessage(e);
		}
		return yCurve;
	}

	@Override
	protected void setIdPropertiesInStatement(PreparedStatement stmt, int columnIndex, YieldCurveId id)
			throws SQLException {
		stmt.setString(columnIndex++, id.getCurrency());
		stmt.setTimestamp(columnIndex++, new Timestamp(id.getAtDate().getTimeInMillis()));
	}

	@Override
	public List<YieldCurve> loadAll() {
		List<YieldCurve> listEntity = new LinkedList<>();
		String selectQuery = "SELECT * FROM " + stateTableName + " JOIN " + entityTableName
				+ " ON entity_id=id";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery)) {

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				YieldCurve temp = createEntity(rs);
				listEntity.add(temp);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return listEntity;
	}
}
