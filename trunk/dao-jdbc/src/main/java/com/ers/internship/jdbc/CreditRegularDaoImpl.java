
package com.ers.internship.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.ers.internship.enums.Frequency;
import com.ers.internship.instrumentdbconnector.InstrumentDBConnector;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Instrument;

/**
 *
 * @author Snejina Yanakieva
 *
 * 
 */
public class CreditRegularDaoImpl implements InstrumentDBConnector {

	@Override
	public String[] getInstrumentColumnNames() {
		String[] columnNames = { "tenorMonths", "interestRate", "issue", "frequency", "maturity" };
		return columnNames;
	}

	@Override
	public void setInstrumentInStatement(PreparedStatement stmt, int columnIndex, Instrument creditRegular) {
		CreditRegular credit = (CreditRegular) creditRegular;
		
		try {
			stmt.setInt(columnIndex++, credit.getTenorMonths());
			stmt.setDouble(columnIndex++, credit.getInterestRate());
			stmt.setTimestamp(columnIndex++, new Timestamp(credit.getIssue().getTimeInMillis()));
			stmt.setString(columnIndex++, credit.getFrequency().toString());
			stmt.setTimestamp(columnIndex++, new Timestamp(credit.getMaturity().getTimeInMillis()));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Instrument createInstrument(ResultSet rs) {
		CreditRegular credit = new CreditRegular();
		try {
			credit.setID(rs.getString("instrument_id"));
			credit.setValidFrom(rs.getTimestamp("valid_from"));
			credit.setValidTo(rs.getTimestamp("valid_to"));
			credit.setIsin(rs.getString("isin"));
			credit.setCurrency(rs.getString("currency"));
			credit.setMarket(rs.getString("market"));
			credit.setTenorMonths(rs.getInt("tenorMonths"));
			credit.setInterestRate(rs.getDouble("interestRate"));
			Calendar issue = new GregorianCalendar();
			issue.setTime(rs.getTimestamp("issue"));
			credit.setIssue(issue);
			Frequency frequency = Frequency.valueOf(rs.getString("frequency"));
			credit.setFrequency(frequency);
			Calendar maturity = new GregorianCalendar();
			maturity.setTime(rs.getTimestamp("maturity"));
			credit.setMaturity(maturity);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return credit;
	}
}
