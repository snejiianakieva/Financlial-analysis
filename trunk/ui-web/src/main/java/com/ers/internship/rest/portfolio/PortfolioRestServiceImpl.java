package com.ers.internship.rest.portfolio;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.LoadResults;
import com.ers.internship.services.crud.searching.PortfolioService;
import com.ers.internship.services.loaders.FlatLoader;

/**
 * @author Snejina Yanakieva
 *
 */

public class PortfolioRestServiceImpl implements PortfolioRestService {
	private PortfolioService portfolioService;

	public PortfolioRestServiceImpl(PersistentStore persistentStore) {
		this.portfolioService = new PortfolioService(persistentStore);
	}

	@Override
	public Response getAll() {
		LoadResults<Portfolio> portfolioResults = portfolioService.searchByName("%");

		List<Portfolio> entities = portfolioResults.getEntities();
		List<String> errors = portfolioResults.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Portfolio>>(entities) {
		}).build();
	}

	@Override
	public Response loadById(String id, long time) {
		Calendar portfolioDate = Calendar.getInstance();
		portfolioDate.setTimeInMillis(time);
		LoadResult<Portfolio> portfolioResult = portfolioService.loadById(id, portfolioDate);

		Portfolio entity = portfolioResult.getEntity();
		List<String> errorList = portfolioResult.getErrors();

		if (entity == null) {

			if (errorList == null || errorList.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errorList) {
			}).build();

		}
		return Response.status(Status.OK).entity(entity).build();

	}

	@Override
	public Response create(Portfolio p) {
		List<String> error = portfolioService.create(p);

		if (error == null || error.isEmpty()) {
			return Response.status(Status.CREATED).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(error) {
		}).build();
	}

	public Response update(Portfolio p) {
		List<String> error = portfolioService.update(p);
		if (error == null || error.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(error) {
		}).build();
	}

	@Override
	public Response deleteById(String id) {
		List<String> error = portfolioService.delete(id);

		if (error == null || error.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(error) {
		}).build();
	}

	@Override
	public Response searchByName(String name) {
		LoadResults<Portfolio> portfolioResults = portfolioService.searchByName(name);

		List<Portfolio> entities = portfolioResults.getEntities();
		List<String> errors = portfolioResults.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Portfolio>>(entities) {
		}).build();
	}

	@Override
	public Response loadPositions(String id) {
		FlatLoader loader = new FlatLoader(portfolioService.getPersistentStore());
		PortfolioSnapshot snapshot = loader.loadSnapshot(id, new GregorianCalendar());
		List<PortfolioItem> entities = snapshot.getItems();

		if (entities == null || entities.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<PortfolioItem>>(entities) {
		}).build();
		
	}

}