package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.ers.internship.instrumentdbconnector.InstrumentDBConnector;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.TimeDeposit;

/**
 * @author Snejina Yanakieva
 */
public class TimeDepositDaoImpl implements InstrumentDBConnector {

	@Override
	public String[] getInstrumentColumnNames() {
		String[] columnNames = { "tenorMonths", "interestRate", "issue" };
		return columnNames;
	}

	@Override
	public void setInstrumentInStatement(PreparedStatement stmt, int columnIndex, Instrument entity) {
		TimeDeposit tempDeposit = (TimeDeposit) entity;
		try {
			stmt.setInt(columnIndex++, tempDeposit.getTenorMonths());
			stmt.setDouble(columnIndex++, tempDeposit.getInterestRate());
			stmt.setTimestamp(columnIndex++, new Timestamp(tempDeposit.getIssue().getTimeInMillis()));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Instrument createInstrument(ResultSet rs) {
		TimeDeposit tempDeposit = new TimeDeposit();
		try {
			tempDeposit.setID(rs.getString("instrument_id"));
			tempDeposit.setValidFrom(rs.getTimestamp("valid_from"));
			tempDeposit.setValidTo(rs.getTimestamp("valid_to"));
			tempDeposit.setIsin(rs.getString("isin"));
			tempDeposit.setCurrency(rs.getString("currency"));
			tempDeposit.setMarket(rs.getString("market"));
			tempDeposit.setTenorMonths(rs.getInt("tenorMonths"));
			tempDeposit.setInterestRate(rs.getDouble("interestRate"));
			Calendar date = new GregorianCalendar();
			date.setTime(rs.getTimestamp("issue"));
			tempDeposit.setIssue(date);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return tempDeposit;
	}

}
