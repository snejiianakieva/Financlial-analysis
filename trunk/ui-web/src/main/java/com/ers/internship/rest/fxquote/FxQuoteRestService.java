package com.ers.internship.rest.fxquote;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ers.internship.market.FxQuote;


/**
 * A RESTful service interface that handles CRUD operations over FxQuote
 * 
 * @author Snejina Yanakieva
 * 
*/
@Path("/fxQuote")
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public interface FxQuoteRestService {
	
	/**
	 * Returns a Response with an OK (200) status code and the FxQuote
	 * object as an entity. If the FxQuote is not found a Response with a
	 * NOT FOUND (404) status code is returned. If the input data is not valid
	 * a response with a BAD REQUEST (400) status code is returned and a list of
	 * string (the errors) as an entity.
	 * 
	 * @param baseCurrency The FxQuote's base currency
	 * @param targetCurrency The FxQuote's target currency
	 * @param time The date for the FxQuote in UTC format
	 * @param atTime the validity date of the FxQuote in UTC format
	 * @return A Response object
	 */
	@GET
	@Path("/loadById/{baseCurrency}/{targetCurrency}/{date}/{atDate}")
	public Response loadById (@PathParam("baseCurrency") String baseCurrency, 
							  @PathParam("targetCurrency") String targetCurrency,
			                  @PathParam("date") long time,  
			                  @PathParam("atDate") long atTime);
	
	/**
	 * Creates a new FxQuote and returns a Response with CREATED (201) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param fx the FxQuote to create
	 * @return A Response object
	 */
	@POST
	@Path("/create")
	public Response create (FxQuote fx);
	
	/**
	 * Updates an existing FxQuote and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param fx the FxQuote to update
	 * @return A Response object
	 */
	@POST
	@Path("/update")
	public Response update (FxQuote fx);
	
	/**
	 * Updates an existing FxQuote and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param baseCurrency The FxQuote's base currency
	 * @param targetCurrency The FxQuote's target currency
	 * @param time The date for the FxQuote in UTC format
	 * @return A Response object
	 */
	@DELETE
	@Path("/delete/{baseCurrency}/{targetCurrency}/{date}")
	public Response deleteById (@PathParam("baseCurrency") String baseCurrency, 
								@PathParam("targetCurrency") String targetCurrency,
								@PathParam("date") long time);
}