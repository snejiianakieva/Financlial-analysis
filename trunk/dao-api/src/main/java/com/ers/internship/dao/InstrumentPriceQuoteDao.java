package com.ers.internship.dao;

import java.util.Calendar;
import java.util.List;

import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public interface InstrumentPriceQuoteDao extends CrudDao<InstrumentPriceQuoteId, InstrumentPriceQuote> {
/**
 * Returns current price quote for an instrument by given id
 *
 * @param instrumentId instrument id
 * @param date date
 * @return InstrumentPriceQuote object
 */
	InstrumentPriceQuote loadLatestPrice(String instrumentId, Calendar date);
	
	List<InstrumentPriceQuote> loadAll();
}
