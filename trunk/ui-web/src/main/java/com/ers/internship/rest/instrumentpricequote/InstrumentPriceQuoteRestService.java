package com.ers.internship.rest.instrumentpricequote;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ers.internship.market.InstrumentPriceQuote;

/**
 * A RESTful service interface that handles CRUD operations over InstrumentPriceQuote
 * 
 * @author Snejina Yanakieva
 * 
*/
@Path("/instrumentPriceQuote")
@Consumes({MediaType.APPLICATION_JSON, "text/json"})
@Produces({MediaType.APPLICATION_JSON, "text/json"})
public interface InstrumentPriceQuoteRestService {
	
	/**
	 * Returns a Response with an OK (200) status code and the InstrumentPriceQuote
	 * object as an entity. If the InstrumentPriceQuote is not found a Response with a
	 * NOT FOUND (404) status code is returned. If the input data is not valid
	 * a response with a BAD REQUEST (400) status code is returned and a list of
	 * string (the errors) as an entity.
	 * 
	 * @param instrumentID The instrument's ID
	 * @param quoteDate The InstrumentPriceQuote's date in UTC format
	 * @param atDate the validity date of the InstrumentPriceQuote in UTC format
	 * @return A Response object
	 */
	@GET
	@Path("/loadById/{instrument_id}/{quote_date}/{at_date}")
	public Response loadById (@PathParam("instrument_id") String instrumentID,
							  @PathParam("quote_date") long quoteDate,
							  @PathParam("at_date") long atDate );
	
	/**
	 * Creates a new InstrumentPriceQuote and returns a Response with CREATED (201) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param p the InstrumentPriceQuote to create
	 * @return A Response object
	 */
	@POST
	@Path("/create")
	public Response create (InstrumentPriceQuote p);
	
	/**
	 * Updates an existing InstrumentPriceQuote and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @param p the InstrumentPriceQuote to update
	 * @return A Response object
	 */
	@POST
	@Path("/update")
	public Response update (InstrumentPriceQuote p);
	
	/**
	 * Updates an existing InstrumentPriceQuote and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * 
	 * @param instrumentID The instrument's ID
	 * @param date The InstrumentPriceQuote's date in UTC format
	 * @return A Response object
	 */
	@DELETE
	@Path("/delete/{instrument_id}/{quote_date}")
	public Response deleteById (@PathParam("instrument_id") String instrumentID, @PathParam("quote_date") long date);

	/**
	 * Updates an existing InstrumentPriceQuote and returns a Response with OK (200) status code.
	 * If any errors have occured the status code of the returned response id
	 * BAD REQUEST (400) and the enclosed entity is a list of strings (the errors)
	 * @return A Response object
	 */
	@GET
	@Path("/loadAll")
	public Response loadAll ();
}