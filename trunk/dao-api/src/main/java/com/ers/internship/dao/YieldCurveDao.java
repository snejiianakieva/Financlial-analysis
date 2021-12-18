package com.ers.internship.dao;

import java.util.Calendar;
import java.util.List;

import com.ers.internship.market.YieldCurve;
import com.ers.internship.identificators.YieldCurveId;

/**
 * 
 * @author Snejina Yanakieva
 *
 */
public interface YieldCurveDao extends CrudDao<YieldCurveId, YieldCurve> {
	/**
	 * Returns current yield curve by given id
	 * 
	 * @param currency current currency
	 * @param date current date
	 * @return YieldCurve object
	 * 
	 */
	YieldCurve loadLatestCurve(String currency, Calendar date);
	
	List<YieldCurve> loadAll();
}
