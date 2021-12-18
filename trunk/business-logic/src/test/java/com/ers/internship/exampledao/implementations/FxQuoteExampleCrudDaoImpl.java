package com.ers.internship.exampledao.implementations;

import com.ers.internship.dao.FxQuoteDao;
import com.ers.internship.exampledao.abstracts.ExampleCrudDao;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.FxQuote;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class FxQuoteExampleCrudDaoImpl extends ExampleCrudDao<FxQuoteId, FxQuote> implements FxQuoteDao {

    private static final Logger logger = Logger.getLogger(FxQuoteExampleCrudDaoImpl.class.getName());

    @Override
    public FxQuote loadLatest(String fromCurrency, String toCurrency, Calendar date) {
        FxQuote fxQuote = null;
        Calendar latestCurveDate = DayCountConvention.getCalendarInstance(1970, 1, 1);

        for (FxQuote quote : items) {
            if (isValidOnDate(quote, date)
                    && quote.getBaseCurrency().equals(fromCurrency)
                    && quote.getTargetCurrency().equals(toCurrency)
                    && quote.getDate().after(latestCurveDate)) {

                fxQuote = quote;
                latestCurveDate = quote.getDate();
            }
        }

        return fxQuote;
    }
}
