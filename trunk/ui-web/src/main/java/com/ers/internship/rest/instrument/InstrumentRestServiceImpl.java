package com.ers.internship.rest.instrument;

/**
 * @author Snejina Yanakieva
 * 
*/

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.crud.searching.InstrumentService;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.LoadResults;

public class InstrumentRestServiceImpl implements InstrumentRestService{
	private InstrumentService instrumentService;
	
	public InstrumentRestServiceImpl (PersistentStore persistentStore) {
		this.instrumentService = new InstrumentService(persistentStore);
	}

	@Override
	public Response loadById(String id, long atDate) {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(atDate);
		
		LoadResult<Instrument> result = instrumentService.loadById(id, date);
		
		Instrument entity = result.getEntity();
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
	public Response create(Instrument i) {
		List<String> errors = instrumentService.create(i);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.CREATED).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}

	@Override
	public Response update(Instrument i) {
		List<String> errors = instrumentService.update(i);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}

	@Override
	public Response deleteById(String id) {
		List<String> errors = instrumentService.delete(id);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).build();
	}


	@Override
	public Response getAll() {
		LoadResults<Instrument> instrumentResult = instrumentService.searchByName("%");
		
		List<Instrument> entities = instrumentResult.getEntities();
		List<String> errors = instrumentResult.getErrors();
		
		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Instrument>>(entities){}).build();
	}

	@Override
	public Response searchByName(String isin) {
		LoadResults<Instrument> instrumentResults = instrumentService.searchByName(isin);

		List<Instrument> entities = instrumentResults.getEntities();
		List<String> errors = instrumentResults.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Instrument>>(entities) {
		}).build();
	}
}