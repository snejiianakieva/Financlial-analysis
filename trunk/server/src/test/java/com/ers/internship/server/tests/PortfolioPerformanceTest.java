package com.ers.internship.server.tests;

import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ers.internship.portfolio.Portfolio;;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class PortfolioPerformanceTest extends EntityPerformanceTest<String, Portfolio> {

	private static String entityID = "idPortfolio";

	@BeforeClass
	public static void beforeClassPortfolio() {
		CREATE_URL = "/portfolio/create/";
		UPDATE_URL = "/portfolio/update/";
		DELETE_URL = "/portfolio/delete/" + entityID;
		LOAD_URL = "/portfolio/loadById/" + entityID;

		CREATE_MSG = "POST PORTFOLIO CREATE ";
		UPDATE_MSG = "POST PORTFOLIO UPDATE ";
		DELETE_MSG = "DELETE PORTFOLIO DELETE ";
		LOAD_MSG = "GET PORTFOLIO LOADED ";
		
		ENTITY_TABLE = "IDB_PORTFOLIO";
		ENTITY_STATE_TABLE = "IDB_PORTFOLIO_STATE";
	}

	@AfterClass
	public static void afterClassPortfolio() {
		CREATE_URL = null;
		UPDATE_URL = null;
		DELETE_URL = null;
		LOAD_URL = null;

		CREATE_MSG = null;
		UPDATE_MSG = null;
		DELETE_MSG = null;
		LOAD_MSG = null;
		
		ENTITY_TABLE = null;
		ENTITY_STATE_TABLE = null;
		
		entityID = null;
	}
	@Before
	public void beforePfTest(){
		System.out.println("beforeTest");
		deleteEntityInDB();
	}
	@After
	public void afterPfTest(){
		System.out.println("afterTest");
		deleteEntityInDB();
	}
	@Override
	protected Portfolio createEntity() {
		Portfolio pf = new Portfolio(entityID);
		pf.setCurrency("BGN");
		pf.setName("portfolio");
		return pf;
	}

	@Override
	protected String getPrimaryKey() {
		String pKey = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM " + ENTITY_TABLE + " WHERE portfolio_id=?");
		try (PreparedStatement stmt = persistentStore.getConnection().prepareStatement(query.toString())) {
			stmt.setString(1, entityID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				pKey = rs.getString("id");
			}
		} catch (SQLException e) {
			fail("Fail select " + e.getMessage());
		}
		return pKey;
	}

}
