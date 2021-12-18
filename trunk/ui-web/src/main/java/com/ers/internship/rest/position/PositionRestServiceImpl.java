package com.ers.internship.rest.position;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.calculation.instrument.InstrumentCalculator;
import com.ers.internship.calculation.instrument.InstrumentCalculatorResolver;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.calculation.portfolioitem.PortfolioItemCalculator;
import com.ers.internship.calculation.portfolioitem.PortfolioItemCalculatorResolver;
import com.ers.internship.position.Position;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.LoadResults;
import com.ers.internship.services.crud.searching.PositionService;

/**
 * @author Snejina Yanakieva
 *
 */
public class PositionRestServiceImpl implements PositionRestService {

	private PositionService positionService;

	public PositionRestServiceImpl(PersistentStore persistentStore) {
		this.positionService = new PositionService(persistentStore);
	}

	@Override
	public Response buildCashFlow(String positionId) {
		Calendar date = new GregorianCalendar();
		PositionSnapshot snepshot = positionService.getPersistentStore().getPositionSnapshotLoader().loadSnapshot(positionId, date);
		Position entity = snepshot.getPosition();
		if (entity == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
		InstrumentCalculator calculator = InstrumentCalculatorResolver.getInstrumentCalculator(entity.getInstrument().getClass());
		Map<Calendar, Double> result = calculator.buildCashFlow(entity.getInstrument(),snepshot.getVolume());
          if (result == null || result.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
		return Response.status(Status.OK).entity(new GenericEntity<Map<Calendar, Double>>(result) {
		}).build();
	}
	
	@Override
	public Response getPrincipalCashFlow(String positionId) {
		Calendar date = new GregorianCalendar();
		PositionSnapshot snepshot = positionService.getPersistentStore().getPositionSnapshotLoader().loadSnapshot(positionId, date);
		Position entity = snepshot.getPosition();
		if (entity == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
		InstrumentCalculator calculator = InstrumentCalculatorResolver.getInstrumentCalculator(entity.getInstrument().getClass());
		calculator.buildCashFlow(entity.getInstrument(),snepshot.getVolume());
        Map<Calendar, Double> result = calculator.getPrincipalCashFLow();
        
		if (result == null || result.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
		return Response.status(Status.OK).entity(new GenericEntity<Map<Calendar, Double>>(result) {
		}).build();
	}
	
	@Override
	public Response getInterestCashFlow(String positionId) {
		Calendar date = new GregorianCalendar();
		PositionSnapshot snepshot = positionService.getPersistentStore().getPositionSnapshotLoader().loadSnapshot(positionId, date);
		Position entity = snepshot.getPosition();
		if (entity == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
		InstrumentCalculator calculator = InstrumentCalculatorResolver.getInstrumentCalculator(entity.getInstrument().getClass());
		calculator.buildCashFlow(entity.getInstrument(),snepshot.getVolume());
		Map<Calendar, Double> result = calculator.getInterestCashFLow();
          if (result == null || result.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
		return Response.status(Status.OK).entity(new GenericEntity<Map<Calendar, Double>>(result) {
		}).build();
	}

	@Override
	public Response getAll() {
		LoadResults<Position> positionResult = positionService.searchByName("%");

		List<Position> entities = positionResult.getEntities();
		List<String> errors = positionResult.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Position>>(entities) {
		}).build();
	}

	@Override
	public Response loadById(String id, long time) {
		Calendar atDate = Calendar.getInstance();

		LoadResult<Position> result = positionService.loadById(id, atDate);

		Position entity = result.getEntity();
		List<String> errors = result.getErrors();

		if (entity == null) {
			if (errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(entity).build();
	}

	@Override
	public Response create(Position p) {
		List<String> errors = positionService.create(p);

		if (errors.isEmpty()) {
			return Response.status(Status.CREATED).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
		}).build();
	}

	@Override
	public Response update(Position p) {
		List<String> errors = positionService.update(p);

		if (errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
		}).build();
	}

	@Override
	public Response deleteById(String id) {
		List<String> errors = positionService.delete(id);

		if (errors.isEmpty()) {
			return Response.status(Status.OK).build();
		}
		return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
		}).build();
	}

	@Override
	public Response searchByName(String name) {
		LoadResults<Position> positionResult = positionService.searchByName(name);

		List<Position> entities = positionResult.getEntities();
		List<String> errors = positionResult.getErrors();

		if (entities == null || entities.isEmpty()) {
			if (errors == null || errors.isEmpty()) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.status(Status.BAD_REQUEST).entity(new GenericEntity<List<String>>(errors) {
			}).build();
		}
		return Response.status(Status.OK).entity(new GenericEntity<List<Position>>(entities) {
		}).build();
	}
}
