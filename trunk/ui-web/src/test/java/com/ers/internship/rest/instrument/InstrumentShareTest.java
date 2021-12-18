package com.ers.internship.rest.instrument;

import java.util.ArrayList;
import java.util.List;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.utility.EntityFactory;


/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class InstrumentShareTest extends InstrumentRestServiceTest<Share> {
	
	@Override
	protected List<Instrument> generateValidEntities() {
		List<Instrument> shareList = new ArrayList<Instrument>(TEST_ENTITIES_COUNT);
		
		for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
			shareList.add(EntityFactory.makeValidShare(null));
		}
		
		return shareList;
	}

	@Override
	protected List<Instrument> generateInvalidEntities() {
		Share share = new Share();
		
		share.setCurrency("BGN");
		share.setID("1");
		share.setIsin("");
		share.setMarket(null);
		share.setValidFrom(java.sql.Timestamp.valueOf("2015-7-27 23:59:00"));
		share.setValidTo(java.sql.Timestamp.valueOf("2017-7-27 23:59:00"));
		
		List<Instrument> shareList = new ArrayList<Instrument>();
		shareList.add(share);
		
		return shareList;
	}

	protected void assertEqualsShare (String message, Share expected, Share actual) {
		assertEquals(message, expected, actual);
	}

	@Override
	protected void updateEntity(Instrument entity) {
		entity.setCurrency("USD");
	}

	@Override
	protected Class<Instrument> getType() {
		return Instrument.class;
	}

	@Override
	protected boolean isEqual(Instrument a, Instrument b) {
		return InstrumentRestServiceTest.deepEquals(a, b);
	}
}