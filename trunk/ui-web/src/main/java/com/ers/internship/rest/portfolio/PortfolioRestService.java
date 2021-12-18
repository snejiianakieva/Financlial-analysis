package com.ers.internship.rest.portfolio;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ers.internship.portfolio.Portfolio;

/**
 * A RESTful service interface that handles CRUD operations over Portfolio
 * 
 * @author Snejina Yanakieva
 * 
 */
@Path("/portfolio")
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
public interface PortfolioRestService {

	/**
	 * Returns a response with an OK (200) status code and an entity which is a
	 * list of all the Portfolios. If there are no Portfolios a response with a
	 * NOT FOUND (404) status code is returned. If any errors have occured a
	 * response with a BAD REQUEST (400) status code is returned and the
	 * enclosed entity is a list of strings (the errors).
	 * 
	 * @return A Response object.
	 */
	@GET
	@Path("/loadAll")
	public Response getAll();

	/**
	 * Returns a response with an OK (200) status code and an entity which is a
	 * list of all the portfolio states that have a specific name. If there are no
	 * Portfolios a response with a NOT FOUND (404) status code is returned. If
	 * any errors have occured a response with a BAD REQUEST (400) status code
	 * is returned and the enclosed entity is a list of strings (the errors).
	 * 
	 * @param name
	 * 				the name of the Portfolio
	 * @return A Response object.
	 */
	@GET
	@Path("/searchByName/{name}")
	public Response searchByName(@PathParam("name") String name);

	/**
	 * Returns a Response with an OK (200) status code and the Portfolio object
	 * as an entity. If the Portfolio is not found a Response with a NOT FOUND
	 * (404) status code is returned. If the input data is not valid a response
	 * with a BAD REQUEST (400) status code is returned and a list of string
	 * (the errors) as an entity.
	 * 
	 * @param id
	 *            The Portfolio's ID
	 * @param time
	 *            the validity date of the Portfolio in UTC format
	 * @return A Response object
	 */
	@GET
	@Path("/loadById/{id}/{date}")
	public Response loadById(@PathParam("id") String id, @PathParam("date") long time);

	/**
	 * Creates a new Portfolio and returns a Response with CREATED (201) status
	 * code. If any errors have occured the status code of the returned response
	 * id BAD REQUEST (400) and the enclosed entity is a list of strings (the
	 * errors)
	 * 
	 * @param p
	 *            the Portfolio to create
	 * @return A Response object
	 */
	@POST
	@Path("/create")
	public Response create(Portfolio p);

	/**
	 * Updates an existing Portfolio and returns a Response with OK (200) status
	 * code. If any errors have occured the status code of the returned response
	 * id BAD REQUEST (400) and the enclosed entity is a list of strings (the
	 * errors)
	 * 
	 * @param p
	 *            the Portfolio to update
	 * @return A Response object
	 */
	@POST
	@Path("/update")
	public Response update(Portfolio p);

	/**
	 * Updates an existing Portfolio and returns a Response with OK (200) status
	 * code. If any errors have occured the status code of the returned response
	 * id BAD REQUEST (400) and the enclosed entity is a list of strings (the
	 * errors)
	 * 
	 * @param id
	 *            the Portfolio's ID
	 * @return A Response object
	 */
	@DELETE
	@Path("/delete/{id}")
	public Response deleteById(@PathParam("id") String id);
	
	/**
	 * Updates an existing Portfolio and returns a Response with OK (200) status
	 * code. If any errors have occured the status code of the returned response
	 * id BAD REQUEST (400) and the enclosed entity is a list of strings (the
	 * errors)
	 * 
	 * @param id
	 *            the Portfolio's ID
	 * @return A Response object
	 */
	@GET
	@Path("/loadPositions/{id}")
	public Response loadPositions(@PathParam("id") String id);
}