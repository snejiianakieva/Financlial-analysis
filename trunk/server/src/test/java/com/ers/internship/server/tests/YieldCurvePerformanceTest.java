package com.ers.internship.server.tests;

import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.YieldCurve;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public class YieldCurvePerformanceTest extends EntityPerformanceTest<YieldCurveId,YieldCurve>{
	private static YieldCurveId ycId = new YieldCurveId("BGN", new GregorianCalendar(2015, 5, 5));
	@BeforeClass
	public static void beforeClassYieldCurve(){
		CREATE_URL = "/yieldcurve/create/";
		UPDATE_URL = "/yieldcurve/update/";
		DELETE_URL = "/yieldcurve/delete/" + ycId.getCurrency() + "/" + ycId.getAtDate().getTimeInMillis();
		LOAD_URL = "/yieldcurve/loadById/" + ycId.getCurrency() + "/" + ycId.getAtDate().getTimeInMillis();
		
		CREATE_MSG = "POST YieldCurve CREATE ";
		UPDATE_MSG = "POST YieldCurve UPDATE ";
		DELETE_MSG = "DELETE YieldCurve DELETE ";
		LOAD_MSG = "GET YieldCurve LOADED ";
		
		ENTITY_TABLE = "IDB_YIELDCURVE";
		ENTITY_STATE_TABLE = "IDB_YIELDCURVE_STATE";
	}
	@AfterClass
	public static void afterClassYieldCurve(){
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
		
		ycId = null;
	}
	@Before
	public void beforeYCTest(){
		System.out.println("beforeTest");
		deleteEntityInDB();
	}
	@After
	public void afterYCTest(){
		System.out.println("afterTest");
		deleteEntityInDB();
	}
	@Override
	protected YieldCurve createEntity() {
		YieldCurve yc = new YieldCurve(ycId);
		yc.setZeroYieldThreeMonths(0.3);
		yc.setZeroYieldSixMonths(0.33);
		yc.setZeroYieldOneYear(0.31);
		yc.setZeroYieldTwoYears(0.35);
		yc.setZeroYieldFiveYears(0.34);
		yc.setZeroYieldTenYears(0.4);
		yc.setZeroYieldThirtyYears(0.38);
		return yc;
	}
	@Override
	protected String getPrimaryKey() {
		String pKey = null;
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM " + ENTITY_TABLE + " WHERE currency=? AND at_date=?");
		try(PreparedStatement stmt = persistentStore.getConnection().prepareStatement(query.toString())) {
			stmt.setString(1, ycId.getCurrency());
			stmt.setTimestamp(2, new Timestamp(ycId.getAtDate().getTimeInMillis()));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				pKey = rs.getString("id");
			}
		} catch (SQLException e) {
			fail("Fail select " + e.getMessage());
		}
		return pKey;
	}
}
