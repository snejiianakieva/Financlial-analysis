package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.position.Position;
import com.ers.internship.snapshotloader.SnapshotLoader;

public class PositionSnapshotLoader implements SnapshotLoader<PositionSnapshot> {
	private JdbcPersistentStore store;

	public PositionSnapshotLoader(JdbcPersistentStore store) {
		this.store = store;
	}

	@Override
	public List<PositionSnapshot> loadSnapshots(String portfolioId, Calendar date) {
		List<PositionSnapshot> positionSnapshots = new LinkedList<>();
		String joinQuery = getQuery();
		try (PreparedStatement stmt = store.getConnection().prepareStatement(joinQuery)) {
			stmt.setString(1, portfolioId);
			stmt.setTimestamp(2, new Timestamp(date.getTimeInMillis()));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				PositionSnapshot ps = new PositionSnapshot();
				Double volume = rs.getDouble("snapVolume");
				Position tPos = createPosition(rs);
				ps.setVolume(volume);
				ps.setPosition(tPos);
				positionSnapshots.add(ps);
			}
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return positionSnapshots;

	}

	@Override
		public PositionSnapshot loadSnapshot(String positionId, Calendar date) {
		PositionSnapshot positionSnapshot = new PositionSnapshot();
		String joinQuery = getPosSnapshotQuery();
		try (PreparedStatement stmt = store.getConnection().prepareStatement(joinQuery)) {
			stmt.setString(1, positionId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Double volume = rs.getDouble("snapVolume");
				Position tPos = createPosition(rs);
				positionSnapshot.setVolume(volume);
				positionSnapshot.setPosition(tPos);
			}
			stmt.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return positionSnapshot;

	}
		
		private String getPosSnapshotQuery() {
		String columnNames = getColumnNames();
		String function = getAggregationFunction();
		StringBuilder query = new StringBuilder(1000);
		query.append("SELECT " + columnNames + " ," + function + " FROM IDB_POSITION idPos "
				+ " JOIN IDB_POSITION_STATE ps ON ps.entity_id=idPos.id "
				+ " JOIN IDB_INSTRUMENT i ON i.id=ps.instrument "
				+ " JOIN IDB_INSTRUMENT_STATE iSt ON iSt.entity_id=i.id "
				+ " JOIN IDB_TRANSACTION_STATE ts ON ts.positionId=idPos.position_id "
				+ " WHERE idPos.position_id=? GROUP BY " + columnNames);
		return query.toString();
	}
	private String getQuery() {
		String columnNames = getColumnNames();
		String function = getAggregationFunction();
		StringBuilder query = new StringBuilder(1000);
		query.append("SELECT " + columnNames + " ," + function + " FROM IDB_POSITION idPos "
				+ " JOIN IDB_POSITION_STATE ps ON ps.entity_id=idPos.id "
				+ " JOIN IDB_INSTRUMENT i ON i.id=ps.instrument "
				+ " JOIN IDB_INSTRUMENT_STATE iSt ON iSt.entity_id=i.id "
				+ " JOIN IDB_TRANSACTION_STATE ts ON ts.positionId=idPos.position_id "
				+ " WHERE ps.portfolioId=? AND(? BETWEEN ps.valid_from AND ps.valid_to )"
				+ " AND (ps.valid_from BETWEEN iSt.valid_from AND iSt.valid_to) GROUP BY " + columnNames);
		return query.toString();
	}
	private String getColumnNames() {
		StringBuilder columnNames = new StringBuilder(200);
		columnNames.append(" idPos.position_id, ps.valid_from, ps.valid_to, ps.name, ps.longSide, ps.shortSide, "
				+ " ps.portfolioId, i.instrument_id, iSt.valid_from, iSt.valid_to, iSt.isin, iSt.currency, "
				+ " iSt.market, iSt.instrument_type, iSt.tenorMonths, iSt.interestRate, iSt.issue, iSt.frequency, iSt.maturity ");
		return columnNames.toString();
	}

	private String getAggregationFunction() {
		StringBuilder function = new StringBuilder(200);
		function.append(
				" ABS(SUM(((CASE WHEN(ps.longSide=ts.receiver) THEN 0 ELSE -1 END)*2 + 1 )*ts.volume)) AS snapVolume");
		return function.toString();
	}

	private Position createPosition(ResultSet rs) {
		Position pos = new Position();
		try {
			PositionDaoImpl posImpl = new PositionDaoImpl(store);
			posImpl.setPositionProperties(pos, rs);
			InstrumentDaoImpl insImpl = new InstrumentDaoImpl(store);
			Instrument instrument = insImpl.createEntity(rs);
			pos.setInstrument(instrument);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return pos;

	}
}
