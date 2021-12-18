package com.ers.internship.rest.position;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ers.internship.position.Position;

/**
 * A RESTful service interface that handles CRUD operations over Position
 * 
 * @author Snejina Yanakieva
 * 
*/
@Path("/position")
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public interface PositionRestService {
	
	/**
	 * Returns a response with an OK (200) status code and an entity which is a list
	 * of all the Positions. If there are no Positions a response with a NOT FOUND (404)
	 * status code is returned. If any errors have occured a response with a BAD REQUEST (400)
	 * status code is returned and the enclosed entity is a list of strings (the errors).
	 * @return A Response object.
	 */
	@GET
	@Path("/loadAll")
	public Response getAll();

	/**
	 * Returns a response with an OK (200) status code and an entity which is a
	 * list of all the Positions that have a specific name. If there are no
	 * Positions a response with a NOT FOUND (404) status code is returned. If
	 * any errors have occured a response with a BAD REQUEST (400) status code
	 * is returned and the enclosed entity is a list of strings (the errors).
	 * 
	 * @param name 
	 * 				the name of the Position
	 * @return A Response object.
	 */
	@GET
	@Path("/searchByName/{name}")
	public Response searchByName(@PathParam("name") String name);
	
	
	/**
	 * Returns a response with an OK (200) status code and an entity which is a
	 * list of all the Positions that have a specific name. If there are no
	 * Positions a response with a NOT FOUND (404) status code is returned. If
	 * any errors have occured a response with a BAD REQUEST (400) status code
	 * is returned and the enclosed entity is a list of strings (the errors).
	 * 
	 * @param positionId 
	 * 				the name of the Position
	 * @return A Response object.
	 */
	@GET
	@Path("/buildCashFlow/{positionId}")
	public Response buildCashFlow(@PathParam("positionId") String positionId);
	
	/**
	 * Returns a response with an OK (200) status code and an entity which is a
	 * list of all the Positions that have a specific name. If there are no
	 * Positions a response with a NOT FOUND (404) status code is returned. If
	 * any errors have occured a response with a BAD REQUEST (400) status code
	 * is returned and the enclosed entity is a list of strings (the errors).
	 * 
	 * @param positionId 
	 * 				the name of the Position
	 * @return A Response object.
	 */
	@GET
	@Path("/buildPrincipalCashFlow/{positionId}")
	public Response getPrincipalCashFlow(@PathParam("positionId") String positionId);
	
	/**
	 * Returns a response with an OK (200) status code and an entity which is a
	 * list of all the Positions that have a specific name. If there are no
	 * Positions a response with a NOT FOUND (404) status code is returned. If
	 * any errors have occured a response with a BAD REQUEST (400) status code
	 * is returned and the enclosed entity is a list of strings (the errors).
	 * 
	 * @param positionId 
	 * 				the name of the Position
	 * @return A Response object.
	 */
	@GET
	@Path("/buildInterestCashFlow/{positionId}")
	public Response getInterestCashFlow(@PathParam("positionId") String positionId);
	
	/**
	 * Returns a Response with an OK (200) status code and the Position
	 * object as an entity. If the Position is not found a Response with a
	 * NOT FOUND (404) status code is returned. If the input data is not valid
	 * a response with a BAD REQUEST (400) status code is returned and a list of
	 * string (the errors) as an entity.
	 * 
	 * @param id The Position's ID
	 * @param time the validity date of the Position in UTC format
	 * @return A Response object
	 */
	@GET
	@Path("/loadById/{id}/{date}")
	public Response loadById ( @PathParam("id") String id, @PathParam("date") long time );
	
	/**
	 * Creates a new Position and returns a Response with CREATED (201) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param p the Position to create
	 * @return A Response object
	 */
	@POST
	@Path("/create")
	public Response create ( Position p );
	
	/**
	 * Updates an existing Position and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param p the Position to update
	 * @return A Response object
	 */
	@POST
	@Path("/update")
	public Response update ( Position p );
	
	/**
	 * Updates an existing Position and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param id the Position's ID
	 * @return A Response object
	 */
	@DELETE
	@Path("/delete/{id}")
	public Response deleteById ( @PathParam("id") String id );
}