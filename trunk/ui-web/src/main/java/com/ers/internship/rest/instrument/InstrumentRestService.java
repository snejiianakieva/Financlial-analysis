package com.ers.internship.rest.instrument;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ers.internship.instruments.Instrument;

/**
 * A RESTful service interface that handles CRUD operations over Instrument
 * 
 * @author Snejina Yanakieva
 * 
 */
@Path("/instrument")
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
public interface InstrumentRestService {

	/**
	 * Returns a response with an OK (200) status code and an entity which is a
	 * list of all the instruments. If there are no instruments a response with
	 * a NOT FOUND (404) status code is returned. If any errors have occured a
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
	 * list of all the instrument states that have a specific isin. If there are no
	 * Instruments a response with a NOT FOUND (404) status code is returned. If
	 * any errors have occured a response with a BAD REQUEST (400) status code
	 * is returned and the enclosed entity is a list of strings (the errors).
	 * 
	 * @param isin
	 *            the isin of the Instrument
	 * @return A Response object.
	 */
	@GET
	@Path("/searchByName/{isin}")
	public Response searchByName(@PathParam("isin") String isin);

	/**
	 * Returns a Response with an OK (200) status code and the Instrument object
	 * as an entity. If the Instrument is not found a Response with a NOT FOUND
	 * (404) status code is returned. If the input data is not valid a response
	 * with a BAD REQUEST (400) status code is returned and a list of string
	 * (the errors) as an entity.
	 * 
	 * @param id
	 *            The Instrument's ID
	 * @param atDate
	 *            the validity date of the Instrument in UTC format
	 * @return A Response object
	 */
	@GET
	@Path("/loadById/{id}/{date}")
	public Response loadById(@PathParam("id") String id, @PathParam("date") long atDate);

	/**
	 * Creates a new Instrument and returns a Response with CREATED (201) status
	 * code. If any errors have occured the status code of the returned response
	 * id BAD REQUEST (400) and the enclosed entity is a list of strings (the
	 * errors)
	 * 
	 * @param i
	 *            the Instrument to create
	 * @return A Response object
	 */
	@POST
	@Path("/create")
	public Response create(Instrument i);

	/**
	 * Updates an existing Instrument and returns a Response with OK (200)
	 * status code. If any errors have occured the status code of the returned
	 * response id BAD REQUEST (400) and the enclosed entity is a list of
	 * strings (the errors)
	 * 
	 * @param i
	 *            the Instrument to update
	 * @return A Response object
	 */
	@POST
	@Path("/update")
	public Response update(Instrument i);

	/**
	 * Updates an existing Instrument and returns a Response with OK (200)
	 * status code. If any errors have occured the status code of the returned
	 * response id BAD REQUEST (400) and the enclosed entity is a list of
	 * strings (the errors)
	 * 
	 * @param id
	 *            the instrument's ID
	 * @return A Response object
	 */
	@DELETE
	@Path("/delete/{id}")
	public Response deleteById(@PathParam("id") String id);
}