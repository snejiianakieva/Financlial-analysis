package com.ers.internship.rest.position;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.position.Position;
import com.ers.internship.rest.AbstractSearchingRestServiceTest;
import com.ers.internship.rest.instrument.InstrumentRestServiceTest;
import com.ers.internship.utility.EntityFactory;

/**
 * @author Snejina Yanakieva
 *
 */
public class PositionRestServiceTest extends AbstractSearchingRestServiceTest<String, Position>{
	
	@Override
	protected Object getService() {
		return new PositionRestServiceImpl(persistentStore);
	}
	
	@Override
	public String getRelativeLoadAllURI() {
		return "/position/loadAll";
	}

	@Override
	public SearchingDao<String, Position> getDao() {
		return persistentStore.getPositionDao();
	}

	@Override
	protected String getRelativeCreateURI() {
		return "/position/create";
	}

	@Override
	protected String getRelativeUpdateURI() {
		return "/position/update";
	}

	@Override
	protected String getRelativeLoadByIdURI(String id, Calendar atDate) {
		return "/position/loadById/" + id + "/" + atDate.getTimeInMillis();
	}

	@Override
	protected String getRelativeDeleteByIdURI(String id) {
		return "/position/delete/" + id;
	}

	@Override
	protected List<Position> generateValidEntities() {
		List<Position> positionList = new ArrayList<Position>(TEST_ENTITIES_COUNT);
		
		for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
			positionList.add(EntityFactory.makeValidPosition(null));
		}
		
		return positionList;
	}

	@Override
	protected List<Position> generateInvalidEntities() {
		Position position = new Position();
		
		position.setID("-1");
		position.setInstrument(new CreditRegular("-1"));
		position.setLongSide("s");
		position.setName("");
		position.setPortfolioId("");
		position.setShortSide("a");
		position.setValidFrom(java.sql.Timestamp.valueOf("2015-7-27 23:59:00"));
		position.setValidTo(java.sql.Timestamp.valueOf("2115-7-27 23:59:00"));
		
		List<Position> positionList = new ArrayList<Position>();
		positionList.add(position);
		
		return positionList;

	}

	@Override
	protected void assertEquals(String message, Position expected, Position actual) {
		Assert.assertEquals(message + " Ids are not equal!", expected.getID(), actual.getID());
		//TODO : Uncomment that mothafucka once it's done!
//		InstrumentRestServiceTest.assertDeepEquals("Instruments are not equal!", expected.getInstrument(), actual.getInstrument() );
		Assert.assertEquals(message + " Long sides are not equal!", expected.getLongSide(), 
				actual.getLongSide());
		Assert.assertEquals(message + " Names are not equal! ", expected.getName(),
				actual.getName());
		Assert.assertEquals(message + " Portfolio Ids are not equal!", expected.getPortfolioId(), 
				actual.getPortfolioId());
		Assert.assertEquals(message + " Short sides are not equal!", expected.getShortSide(),
				actual.getShortSide());
		Assert.assertEquals(message + " Valid from are not equal!", expected.getValidFrom(),
				actual.getValidFrom());
		Assert.assertEquals(message + " Valid to are not equal!", expected.getValidTo(),
				actual.getValidTo());
	}

	@Override
	public boolean isEqual(Position a, Position b) {
		if (a == null) {
			return b == null;
		}
		
		return checkIfEqual(a.getID(), b.getID())&&
				checkIfEqual(a.getLongSide(), b.getLongSide()) &&
				checkIfEqual(a.getName(), b.getName()) &&
				checkIfEqual(a.getPortfolioId(), b.getPortfolioId()) &&
				checkIfEqual(a.getShortSide(), b.getShortSide()) &&
				checkIfEqual(a.getValidFrom(), b.getValidFrom()) &&
				checkIfEqual(a.getValidTo(), b.getValidTo()) &&
				InstrumentRestServiceTest.deepEquals(a.getInstrument(), b.getInstrument()) ;
	}

	@Override
	protected void updateEntity(Position entity) {

		Calendar newValidFrom = Calendar.getInstance(),
				newValidTo = Calendar.getInstance();
		
		newValidFrom.setTimeInMillis(entity.getValidTo().getTime() + 1);
		newValidTo.set(Calendar.YEAR, newValidFrom.get(Calendar.YEAR) + 1);
		
		entity.setShortSide("TestUpdatedShortSide");
		
		entity.setValidFrom(new Timestamp(newValidFrom.getTimeInMillis()));
		entity.setValidTo(new Timestamp(newValidTo.getTimeInMillis()));	
	}

	@Override
	protected Class<Position> getType() {
		return Position.class;
	}
}