package com.ers.internship.exampledao.implementations;

import com.ers.internship.dao.InstrumentPriceQuoteDao;
import com.ers.internship.exampledao.abstracts.ExampleCrudDao;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.InstrumentPriceQuote;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class InstrumentPriceQuoteExampleCrudDaoImpl extends ExampleCrudDao<InstrumentPriceQuoteId, InstrumentPriceQuote> implements InstrumentPriceQuoteDao {

    private static final Logger logger = Logger.getLogger(InstrumentPriceQuoteExampleCrudDaoImpl.class.getName());

    @Override
    public InstrumentPriceQuote loadLatestPrice(String instrumentId, Calendar date) {

        InstrumentPriceQuote latestQuote = null;
        Calendar latestQuoteDate = DayCountConvention.getCalendarInstance(1970, 1, 1);

        for (InstrumentPriceQuote quote : items) {
            if (isValidOnDate(quote, date)
                    && quote.getDate().after(latestQuoteDate)) {

                latestQuote = quote;
                latestQuoteDate = latestQuote.getDate();
            }
        }

        return latestQuote;
    }

    @Override
    public List<InstrumentPriceQuote> loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
