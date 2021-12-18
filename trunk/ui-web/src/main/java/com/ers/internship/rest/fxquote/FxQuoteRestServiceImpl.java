package com.ers.internship.rest.fxquote;

/**
 * @author Snejina Yanakieva
 * 
*/

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.FxQuote;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.crud.FxQuoteService;
import com.ers.internship.services.results.LoadResult;

public class FxQuoteRestServiceImpl implements FxQuoteRestService {
	private FxQuoteService fxQuoteService;

	public FxQuoteRestServiceImpl (PersistentStore persistentStore) {
		this.fxQuoteService = new FxQuoteService(persistentStore);
	}

	@Override
	public Response loadById(String baseCurrency, String targetCurrency, long time, long atTime) {
		Calendar date = Calendar.getInstance(),
				 atDate = Calendar.getInstance();
		date.setTimeInMillis(time);
		atDate.setTimeInMillis(atTime);
		
		FxQuoteId id = new FxQuoteId(baseCurrency, targetCurrency, date);
		
		LoadResult<FxQuote> result = fxQuoteService.loadById(id, atDate);
		
		FxQuote entity = result.getEntity();
		List<String> errors = result.getErrors();
		
		if (entity == null) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
		}
		return Response.status(Status.OK).entity(entity).build();
	}

	@Override
	public Response create(FxQuote fx) {
		List<String> errors = fxQuoteService.create(fx);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.CREATED).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}

	@Override
	public Response update(FxQuote fx) {
		List<String> errors = fxQuoteService.update(fx);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}

	@Override
	public Response deleteById(String baseCurrency, String targerCurrency, long time) {
		Calendar atDate = Calendar.getInstance();
		atDate.setTimeInMillis(time);
		
		FxQuoteId id = new FxQuoteId(baseCurrency, targerCurrency, atDate);
		
		List<String> errors = fxQuoteService.delete(id);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}
}