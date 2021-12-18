package com.ers.internship.rest.calculation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ers.internship.services.calculation.PortfolioCalculationRequest;
import com.ers.internship.services.results.PortfolioItemResult;

/**
 * A RESTful service interface which handles {@link PortfolioCalculationRequest}
 * 
 * @author Snejina Yanakieva
 */
@Path("/calculation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CalculationRestService {
	
	/**
	 * This method handles {@link PortfolioCalculationRequest}. If errors have occured
	 * during the calculations a Response with BAD REQUEST (400) status code is returned
	 * and the enclosed entity is a list of the errors that occured. If no errors have occured
	 * the response returned has an OK (200) status code and the enclosed entity is a 
	 * {@link PortfolioItemResult} representing the result of the calculation.
	 * 
	 * @param request The calculation request
	 * @return a Response object
	 */
	@POST
	public Response calculate(PortfolioCalculationRequest request);
}
