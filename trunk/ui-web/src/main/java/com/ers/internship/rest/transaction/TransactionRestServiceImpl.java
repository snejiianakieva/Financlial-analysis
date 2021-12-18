package com.ers.internship.rest.transaction;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.crud.searching.TransactionService;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.LoadResults;
import com.ers.internship.transaction.Transaction;

/**
 * @author Snejina Yanakieva
 *
 */

public class TransactionRestServiceImpl implements TransactionRestService {
	private TransactionService transactionService;
	
	public TransactionRestServiceImpl ( PersistentStore persistentStore ) {
		this.transactionService = new TransactionService(persistentStore);
	}

	@Override
	public Response loadById(String id, long time) {
		Calendar atDate = Calendar.getInstance();
		atDate.setTimeInMillis(time);
		
		LoadResult<Transaction> result = transactionService.loadById(id, atDate);
		
		Transaction entity = result.getEntity();
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
	public Response create(Transaction t) {
		List<String> errors = transactionService.create(t);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.CREATED).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}

	@Override
	public Response update(Transaction t) {
		List<String> errors = transactionService.update(t);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}

	@Override
	public Response deleteById(String id) {
		List<String> errors = transactionService.delete(id);
		
		if (errors == null || errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors){}).build();
	}

	@Override
	public Response searchByName(String name) {
		LoadResults<Transaction> transactionResults = transactionService.searchByName(name);

		List<Transaction> entities = transactionResults.getEntities();
		List<String> errors = transactionResults.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Transaction>>(entities) {
		}).build();
	}
	
		@Override
	public Response getAll() {
		LoadResults<Transaction> positionResult = transactionService.searchByName("%");

		List<Transaction> entities = positionResult.getEntities();
		List<String> errors = positionResult.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Transaction>>(entities) {
		}).build();
	}

}