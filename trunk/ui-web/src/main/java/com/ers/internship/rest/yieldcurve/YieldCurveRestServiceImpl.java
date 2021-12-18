package com.ers.internship.rest.yieldcurve;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.crud.YieldCurveService;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.LoadResults;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class YieldCurveRestServiceImpl implements YieldCurveRestService {
	private YieldCurveService yieldCurveService;
	
	public YieldCurveRestServiceImpl(PersistentStore store) {
		yieldCurveService = new YieldCurveService(store);
	}
	
	@Override
	public Response loadById(String currency, long yieldCurveDate, long atDate) {
		Calendar yieldCurveCalendar = Calendar.getInstance();
		Calendar validDate = Calendar.getInstance();
		
		yieldCurveCalendar.setTimeInMillis(yieldCurveDate);
		validDate.setTimeInMillis(atDate);

		YieldCurveId id = new YieldCurveId(currency, yieldCurveCalendar);
		
		LoadResult<YieldCurve> result = yieldCurveService.loadById(id, validDate);
		Response response;
		YieldCurve entity = result.getEntity();
		List<String> errorList = result.getErrors();
		
		if (entity == null) {
			if (errorList.isEmpty()) {
				response = Response.status(Status.NOT_FOUND).build();
			}
			else {
				response = Response.status(Status.BAD_REQUEST)
						.entity(new GenericEntity<List<String>>(errorList){})
						.build();
			}
		}
		else {
			response = Response.status(Status.OK).entity(entity).build();
		}
		
		return response;
	}
	
	@Override
	public Response update(YieldCurve curve) {
		Response response;
		List<String> errorList = yieldCurveService.update(curve);
		
		if (errorList.isEmpty()) {
			response = Response.status(Status.OK)
					.entity(curve)
					.build();
		}
		else {
			response = Response.status(Status.BAD_REQUEST)
					.entity(new GenericEntity<List<String>>(errorList){})
					.build();
		}
		
		return response;
	}
	
	@Override
	public Response deleteById(String currency, long curveDate) {

		Calendar curveDateCalendar = Calendar.getInstance();
		curveDateCalendar.setTimeInMillis(curveDate);
		
		YieldCurveId id = new YieldCurveId(currency, curveDateCalendar);
		
		Response response;
		List<String> errorList = yieldCurveService.delete(id);
		
		if (errorList.isEmpty()) {
			response = Response.status(Status.OK).build();
		}
		else {
			response = Response.status(Status.BAD_REQUEST)
					.entity(new GenericEntity<List<String>>(errorList){})
					.build();
		}
		
		return response;
	}
	
	@Override
	public Response create(YieldCurve curve) {
		
		Response response = null;
		List<String> errorList = yieldCurveService.create(curve);
		
		if (errorList.isEmpty()) {
			response = Response.status(Status.CREATED).build();
		}
		else {
			response = Response.status(Status.BAD_REQUEST)
					.entity(new GenericEntity<List<String>>(errorList){})
					.build();
		}
		return response;
	}

	@Override
	public Response loadAll() {
		LoadResults<YieldCurve> curveResult = yieldCurveService.loadAll();

		List<YieldCurve> entities = curveResult.getEntities();
		List<String> errors = curveResult.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<YieldCurve>>(entities) {
		}).build();}
}
