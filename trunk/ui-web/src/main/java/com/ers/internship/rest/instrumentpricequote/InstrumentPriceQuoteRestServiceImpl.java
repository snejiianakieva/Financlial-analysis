package com.ers.internship.rest.instrumentpricequote;

/**
 * @author Snejina Yanakieva
 * 
*/
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.crud.InstrumentPriceQuoteService;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.LoadResults;

public class InstrumentPriceQuoteRestServiceImpl implements InstrumentPriceQuoteRestService {

	private InstrumentPriceQuoteService instrumentPriceQuoteService;

	public InstrumentPriceQuoteRestServiceImpl(PersistentStore persistentStore) {
		this.instrumentPriceQuoteService = new InstrumentPriceQuoteService(persistentStore);
	}

	@Override
	public Response loadById(String instrumentID, long quoteDate, long atDate) {
		Calendar dateQuote = Calendar.getInstance();
		Calendar time = Calendar.getInstance();

		dateQuote.setTimeInMillis(quoteDate);

		InstrumentPriceQuoteId priceQuoteID = new InstrumentPriceQuoteId(instrumentID, dateQuote);

		LoadResult<InstrumentPriceQuote> result = instrumentPriceQuoteService.loadById(priceQuoteID, time);

		InstrumentPriceQuote entity = result.getEntity();
		List<String> errors = result.getErrors();

		if (entity == null) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(entity).build();
	}

	@Override
	public Response create(InstrumentPriceQuote p) {
		List<String> errors = instrumentPriceQuoteService.create(p);

		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.CREATED).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
		}).build();
	}

	@Override
	public Response update(InstrumentPriceQuote p) {
		List<String> errors = instrumentPriceQuoteService.update(p);

		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
		}).build();
	}

	@Override
	public Response deleteById(String instrumentID, long date) {
		Calendar atDate = Calendar.getInstance();
		atDate.setTimeInMillis(date);

		InstrumentPriceQuoteId id = new InstrumentPriceQuoteId(instrumentID, atDate);

		List<String> errors = instrumentPriceQuoteService.delete(id);

		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
		}).build();
	}

	@Override
	public Response loadAll() {
		LoadResults<InstrumentPriceQuote> curveResult = instrumentPriceQuoteService.loadAll();

		List<InstrumentPriceQuote> entities = curveResult.getEntities();
		List<String> errors = curveResult.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<InstrumentPriceQuote>>(entities) {
		}).build();
	}

}
