package com.ers.internship.rest.fxquote;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.FxQuote;
import com.ers.internship.rest.AbstractCrudRestServiceTest;
import com.ers.internship.utility.EntityFactory;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class FxQuoteRestServiceTest extends AbstractCrudRestServiceTest<FxQuoteId, FxQuote> {

	@Override
	protected Object getService() {
		return new FxQuoteRestServiceImpl(persistentStore);
	}
	
	@Override
	protected String getRelativeCreateURI() {
		return "/fxQuote/create";
	}

	@Override
	protected String getRelativeUpdateURI() {
		return "/fxQuote/update";
	}

	@Override
	protected String getRelativeLoadByIdURI(FxQuoteId id, Calendar atDate) {
		return "/fxQuote/loadById/" + id.getFromCurrency() + "/" + id.getToCurrency() + "/"
				+ id.getAtDate().getTimeInMillis() + "/" + atDate.getTimeInMillis();
	}

	@Override
	protected String getRelativeDeleteByIdURI(FxQuoteId id) {
		return "/fxQuote/delete/" + id.getFromCurrency() + "/"
				+ id.getToCurrency() + "/" + id.getAtDate().getTimeInMillis();
	}

	@Override
	protected List<FxQuote> generateValidEntities() {		
		List<FxQuote> entities = new ArrayList<>(TEST_ENTITIES_COUNT);
		Calendar startDate = DayCountConvention.getCalendarInstance(2014, 8, 13),
				currentDate;
		FxQuoteId id;
		
		for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
			currentDate = DayCountConvention.getCalendarCopy(startDate);
			DayCountConvention.incrementCalendar(currentDate, Calendar.DAY_OF_MONTH, i * 3);
			id = new FxQuoteId(null, null, currentDate);
			entities.add(EntityFactory.makeValidFxQuote(id));
		}
		
		return entities;
	}

	@Override
	protected List<FxQuote> generateInvalidEntities() {
		FxQuoteId fxQuoteID = new FxQuoteId("", "USD", DayCountConvention.getCalendarInstance(2015, 8, 5));
		
		FxQuote fxQuote = new FxQuote(fxQuoteID);
		
		fxQuote.setBaseCurrency("USD");
		fxQuote.setDate(null);
		fxQuote.setID(fxQuoteID);
		fxQuote.setTargetCurrency("");
		fxQuote.setValidFrom(null);
		fxQuote.setValidTo(null);
		fxQuote.setValue(-10);
		
		List<FxQuote> fxQuoteList = new ArrayList<FxQuote>();
		fxQuoteList.add(fxQuote);
		
		return fxQuoteList;
	}
	
	@Override
	protected void assertEquals(String message, FxQuote expected, FxQuote actual) {
		Assert.assertEquals(message + "Values are not equals!", expected.getValue(), actual.getValue(), 0.1);
		Assert.assertEquals(message + "Base currencies are not equals!", expected.getBaseCurrency(), actual.getBaseCurrency());
		Assert.assertEquals(message + "Dates are not equals!", expected.getDate(), actual.getDate());
		Assert.assertEquals(message + "IDs are not equals!", expected.getID(), actual.getID());
		Assert.assertEquals(message + "Target currencies are not equals!", expected.getTargetCurrency(), actual.getTargetCurrency());
		Assert.assertEquals(message + "Valid form are not equals!", expected.getValidFrom(), actual.getValidFrom());
		Assert.assertEquals(message + "Valid to are not equals!", expected.getValidTo(), actual.getValidTo());
	}

	@Override
	protected CrudDao<FxQuoteId, FxQuote> getDao() {
		return persistentStore.getFxQuoteDao();
	}

	@Override
	protected void updateEntity(FxQuote entity) {		
		entity.setValue(3);
	}

	@Override
	protected Class<FxQuote> getType() {
		return FxQuote.class;
	}
}