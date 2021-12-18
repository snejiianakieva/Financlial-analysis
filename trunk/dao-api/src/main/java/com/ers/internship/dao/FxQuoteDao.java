package com.ers.internship.dao;

import java.util.Calendar;

import com.ers.internship.market.FxQuote;
import com.ers.internship.identificators.FxQuoteId;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public interface FxQuoteDao extends CrudDao<FxQuoteId, FxQuote>{
	/**
	 * Returns current FX quote by given id
	 * 
	 * @param fromCurrency base Currency
	 * @param toCurrency quote Currency
	 * @param date date
	 * @return FxQuote object
	 */
	FxQuote loadLatest(String fromCurrency, String toCurrency, Calendar date);
}
