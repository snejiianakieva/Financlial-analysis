package com.ers.internship.rest.instrument;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.rest.AbstractSearchingRestServiceTest;



/**
 * @author Snejina Yanakieva
 *
 * @param <InstrumentType> the type of Instrument managed by the service
 */
public abstract class InstrumentRestServiceTest<InstrumentType extends Instrument>
		extends AbstractSearchingRestServiceTest<String, Instrument> {
	
	@Override
	protected Object getService() {
		return new InstrumentRestServiceImpl(persistentStore);
	}
	
	@Override
	protected Class<Instrument> getType(){
		return Instrument.class;
	}
	
	@Override
	protected String getRelativeLoadAllURI() {
		return "/instrument/loadAll";
	}
	
	@Override
	protected String getRelativeCreateURI() {
		return "/instrument/create";
	}

	@Override
	protected String getRelativeUpdateURI() {
		return "/instrument/update";
	}

	@Override
	protected String getRelativeLoadByIdURI(String id, Calendar atDate) {
		return "/instrument/loadById/" + id + "/" + atDate.getTimeInMillis();
	}

	@Override
	protected String getRelativeDeleteByIdURI(String id) {
		return "/instrument/delete/" + id;
	}

	@Override
	protected abstract List<Instrument> generateValidEntities();

	@Override
	protected abstract List<Instrument> generateInvalidEntities();

	@Override
	protected void assertEquals(String message, Instrument expected, Instrument actual) {
		InstrumentRestServiceTest.assertDeepEquals(message, expected, actual);
	}

	@Override
	protected SearchingDao<String, Instrument> getDao() {
		return persistentStore.getInstrumentDao();
	}
	
	public static void assertDeepEquals(String message, Instrument expected, Instrument actual) {
		Assert.assertEquals(message + "Curencies are not equals!", expected.getCurrency(), actual.getCurrency());
		Assert.assertEquals(message + "Ids are not equals! ", expected.getID(), actual.getID());
		Assert.assertEquals(message + "Isisns are not equals! ", expected.getIsin(), actual.getIsin());
		Assert.assertEquals(message + "Markets are not equals! ", expected.getMarket(), actual.getMarket());
		Assert.assertEquals(message + "Valid from are not equals!", expected.getValidFrom(), actual.getValidFrom());
		Assert.assertEquals(message + "Valid to are not equals! ", expected.getValidTo(), actual.getValidTo());
	}


	
	public static boolean deepEquals(Instrument a, Instrument b) {
		if (a == null) {
			return b == null;
		}
		
		return checkIfEqual(a.getCurrency(), b.getCurrency()) &&
				checkIfEqual(a.getID(), b.getID()) &&
				checkIfEqual(a.getIsin(), b.getIsin()) &&
				checkIfEqual(a.getMarket(), b.getMarket()) &&
				checkIfEqual(a.getValidFrom(), b.getValidFrom()) &&
				checkIfEqual(a.getValidTo(), b.getValidTo());
	}
}