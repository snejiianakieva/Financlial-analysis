package com.ers.internship.rest.portfolio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.rest.AbstractSearchingRestServiceTest;
import com.ers.internship.utility.EntityFactory;

/**
 * @author Snejina Yanakieva
 *
 */

public class PortfolioRestServiceTest extends AbstractSearchingRestServiceTest<String, Portfolio> {
	
	@Override
	protected Object getService() {
		return new PortfolioRestServiceImpl(persistentStore);
	}
	
	@Override
	public String getRelativeLoadAllURI() {
		return "/portfolio/loadAll";
	}

	@Override
	public SearchingDao<String, Portfolio> getDao() {
		return persistentStore.getPortfolioDao();
	}

	@Override
	protected String getRelativeCreateURI() {
		return "/portfolio/create";
	}

	@Override
	protected String getRelativeUpdateURI() {
		return "/portfolio/update";
	}

	@Override
	protected String getRelativeLoadByIdURI(String id, Calendar atDate) {
		return "/portfolio/loadById/" + id + "/" + atDate.getTimeInMillis();
	}

	@Override
	protected String getRelativeDeleteByIdURI(String id) {
		return "/portfolio/delete/" + id;
	}

	@Override
	protected List<Portfolio> generateValidEntities() {
		List<Portfolio> portfolioList = new ArrayList<Portfolio>(TEST_ENTITIES_COUNT);
		
		for (int i = 0; i < TEST_ENTITIES_COUNT; i++) {
			portfolioList.add(EntityFactory.makeValidPortfolio(null));
		}
		
		return portfolioList;
	}

	@Override
	protected List<Portfolio> generateInvalidEntities() {
		Portfolio portfolio = new Portfolio();
		
		portfolio.setCurrency("");
		portfolio.setID("");
		portfolio.setName(null);
		portfolio.setValidFrom(null);
		portfolio.setValidTo(null);
		
		List<Portfolio> portfolioList = new ArrayList<Portfolio>();
		portfolioList.add(portfolio);
		
		return portfolioList;
	}

	@Override
	protected void assertEquals(String message, Portfolio expected, Portfolio actual) {
		Assert.assertEquals(message + "Curencies are not equals!", expected.getCurrency(), actual.getCurrency());
		Assert.assertEquals(message + "IDs are not equals!", expected.getID(), actual.getID());
		Assert.assertEquals(message + "Names are not equals!", expected.getName(), actual.getName());
		Assert.assertEquals(message + "Valid from  are not equals!", expected.getValidFrom(), actual.getValidFrom());
		Assert.assertEquals(message + "Valid to are not equals!", expected.getValidTo(), actual.getValidTo());
	}

	@Override
	public boolean isEqual(Portfolio a, Portfolio b) {
		return checkIfEqual(a.getCurrency(), b.getCurrency()) &&
			   checkIfEqual(a.getName(), b.getName()) &&
			   checkIfEqual(a.getID(), b.getID()) &&
			   checkIfEqual(a.getValidFrom(), b.getValidFrom()) &&
			   checkIfEqual(a.getValidTo(), b.getValidTo());
	}

	@Override
	protected void updateEntity(Portfolio entity) {
		entity.setValidFrom(java.sql.Timestamp.valueOf("2018-3-13 23:59:00"));
		entity.setValidTo(java.sql.Timestamp.valueOf("2019-3-13 23:59:00"));
		entity.setName("portfolio2");
	}

	@Override
	protected Class<Portfolio> getType() {
		return Portfolio.class;
	}
}