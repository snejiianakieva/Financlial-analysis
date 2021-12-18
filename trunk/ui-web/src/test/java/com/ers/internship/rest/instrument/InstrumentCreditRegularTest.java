package com.ers.internship.rest.instrument;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.enums.Frequency;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.utility.EntityFactory;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class InstrumentCreditRegularTest extends InstrumentRestServiceTest<CreditRegular> {
	
	protected static Instrument generateValidInstrument(String id, String isin) {
		Share validInstrument = new Share(id);
		
		validInstrument.setCurrency("EUR");
		validInstrument.setIsin(isin);
		validInstrument.setMarket("Markeet");
		validInstrument.setValidFrom(Timestamp.valueOf("2015-01-01 00:00:00"));
		validInstrument.setValidTo(Timestamp.valueOf("2020-12-31 23:59:59"));
		
		return validInstrument;
	}
	
	@Override
	protected List<Instrument> generateValidEntities() {
		List<Instrument> creditRegularList = new ArrayList<Instrument>();
		
		for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
			creditRegularList.add(EntityFactory.makeValidCreditRegular(null));
		}
		
		return creditRegularList;
	}

	@Override
	protected List<Instrument> generateInvalidEntities() {
		CreditRegular creditRegular = new CreditRegular();
		
		creditRegular.setCurrency("BGN");
		creditRegular.setFrequency(Frequency.MONTH);
		creditRegular.setID(null);
		creditRegular.setInterestRate(5.3);
		creditRegular.setIsin("12589");
		creditRegular.setIssue(DayCountConvention.getCalendarInstance(2015, 8, 5));
		creditRegular.setMarket(null);
		creditRegular.setMaturity(DayCountConvention.getCalendarInstance(2016, 8, 5));
		creditRegular.setTenorMonths(6);
		creditRegular.setValidFrom(java.sql.Timestamp.valueOf("2015-7-27 23:59:00"));
		creditRegular.setValidTo(java.sql.Timestamp.valueOf("2017-7-27 23:59:00"));
		
		List<Instrument> creditRegularList = new ArrayList<Instrument>();
		creditRegularList.add(creditRegular);
		
		return creditRegularList;
	}
	
	protected void aseertEqualsCreditRegular (String message, CreditRegular expected, CreditRegular actual) {
		assertEquals(message, expected, actual);
		Assert.assertEquals(message + "Interest rates are not equals!", expected.getInterestRate(), actual.getInterestRate(), 0.1);
		Assert.assertEquals(message + "Frequencies are not equals!", expected.getFrequency(), actual.getFrequency());
		Assert.assertEquals(message + "Issues are not equals!", expected.getIssue().getTime(), actual.getIssue().getTime());
		Assert.assertEquals(message + "Maturities are not equals!", expected.getMaturity().getTime(), actual.getMaturity().getTime());
		Assert.assertEquals(message + "Tenor months are not equals!", expected.getTenorMonths(), actual.getTenorMonths());
	}

	@Override
	protected void updateEntity(Instrument entity) {
		String instrumentCurrency = entity.getCurrency();
		instrumentCurrency = instrumentCurrency.equals("BGN") ? "EUR" : "BGN";
		entity.setCurrency(instrumentCurrency);
		
		if (entity instanceof CreditRegular) {
			CreditRegular instr = (CreditRegular) entity;
			instr.setInterestRate(2);
		}
	}

	@Override
	protected boolean isEqual(Instrument a, Instrument b) {
		if (! InstrumentRestServiceTest.deepEquals(a, b)) {
			return false;
		}
		
		if ( (a instanceof CreditRegular) && (b instanceof CreditRegular) ) {
			CreditRegular creditA = (CreditRegular) a,
					creditB = (CreditRegular) b;
			
			if ( creditA.getInterestRate() == creditB.getInterestRate() &&
				checkIfEqual(creditA.getIssue(), creditB.getIssue()) &&
				checkIfEqual(creditA.getMaturity(), creditB.getMaturity()) &&
				creditA.getTenorMonths() == creditB.getTenorMonths()) {
				
				return true;
			}
			
			return false;
		}
		
		return false;
	}

}