package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ers.internship.instrumentdbconnector.InstrumentDBConnector;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;

/**
 * @author Snejina Yanakieva
 */
public class ShareDaoImpl implements InstrumentDBConnector {

	@Override
	public String[] getInstrumentColumnNames() {
		String[] shareColumns = {};
		return shareColumns;
	}

	@Override
	public void setInstrumentInStatement(PreparedStatement stmt, int columnIndex, Instrument entity) {
		// Share does not have any different properties then the abstract instrument
	}

	@Override
	public Instrument createInstrument(ResultSet rs) {
		Instrument shareDb = new Share();
		try {
			shareDb.setID(rs.getString("instrument_id"));
			shareDb.setValidFrom(rs.getTimestamp("valid_from"));
			shareDb.setValidTo(rs.getTimestamp("valid_to"));
			shareDb.setIsin(rs.getString("isin"));
			shareDb.setCurrency(rs.getString("currency"));
			shareDb.setMarket(rs.getString("market"));

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return shareDb;
	}

}
