package com.ers.internship.rest.calculation;

import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.calculation.CalculationService;
import com.ers.internship.services.calculation.PortfolioCalculationRequest;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.PortfolioItemResult;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class CalculationRestServiceImpl implements CalculationRestService{
	
	private CalculationService service;
	
	public CalculationRestServiceImpl(PersistentStore persistentStore) {
		service = new CalculationService(persistentStore);
	}
	
	@Override
	public Response calculate(PortfolioCalculationRequest request) {
		LoadResult<PortfolioItemResult> result = service.calculate(request);
		
		if (result.getErrors() == null || result.getErrors().isEmpty()) {
			return Response.status(Status.OK).entity(result.getEntity()).build();
		}
		else {
			return Response.status(Status.BAD_REQUEST)
					.entity(new GenericEntity<List<String>>(result.getErrors()){})
					.build();
		}
	}
}
