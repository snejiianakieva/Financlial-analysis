package com.ers.internship.rest.yieldcurve;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ers.internship.market.YieldCurve;

/**
 * A RESTful service interface that handles CRUD operations over YieldCurve
 * 
 * @author Snejina Yanakieva
 *
 */
@Path("/yieldcurve")
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public interface YieldCurveRestService {
	

	/**
	 * Returns a Response with an OK (200) status code and the YieldCurve
	 * object as an entity. If the YieldCurve is not found a Response with a
	 * NOT FOUND (404) status code is returned. If the input data is not valid
	 * a response with a BAD REQUEST (400) status code is returned and a list of
	 * string (the errors) as an entity.
	 * 
	 * @param currency The YieldCurve's currency
	 * @param curve_date The YieldCurve's date in UTC format
	 * @param atDate the validity date of the YieldCurve in UTC format
	 * @return A Response object
	 */
	@GET
	@Path("/loadById/{currency}/{curve_date}/{at_date}")
	public Response loadById(@PathParam("currency") String currency,
			@PathParam("curve_date") long curve_date,
			@PathParam("at_date") long atDate);

	/**
	 * Updates an existing YieldCurve and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param curve the YieldCurve to update
	 * @return A Response object
	 */
	@POST
	@Path("/update")
	public Response update(YieldCurve curve);

	/**
	 * Updates an existing YieldCurve and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * 
	 * @param currency The YieldCurve's currency
	 * @param curve_date The YieldCurve's date in UTC format
	 * @return A Response object
	 */
	@DELETE
	@Path("/delete/{curve_currency}/{curve_date}")
	public Response deleteById(@PathParam("curve_currency") String currency,
			@PathParam("curve_date") long curve_date);


	/**
	 * Creates a new YieldCurve and returns a Response with CREATED (201) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param curve the YieldCurve to create
	 * @return A Response object
	 */
	@POST
	@Path("/create")
	public Response create(YieldCurve curve);
	
	/**
	 * Creates a new YieldCurve and returns a Response with CREATED (201) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @return A Response object
	 */
	@GET
	@Path("/loadAll")
	public Response loadAll();
}