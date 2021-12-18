package com.ers.internship.exampledao.implementations;

import com.ers.internship.dao.YieldCurveDao;
import com.ers.internship.exampledao.abstracts.ExampleCrudDao;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.YieldCurve;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class YieldCurveExampleCrudDaoImpl extends ExampleCrudDao<YieldCurveId, YieldCurve> implements YieldCurveDao {

    private static final Logger logger = Logger.getLogger(YieldCurveExampleCrudDaoImpl.class.getName());

    @Override
    public YieldCurve loadLatestCurve(String currency, Calendar date) {
        YieldCurve latestCurve = null;
        Calendar latestCurveDate = DayCountConvention.getCalendarInstance(1970, 1, 1);

        for (YieldCurve curve : items) {
            if (isValidOnDate(curve, date)
                    && curve.getCurrency().equals(currency)
                    && curve.getDate().after(latestCurveDate)) {

                latestCurve = curve;
                latestCurveDate = curve.getDate();
            }
        }

        return latestCurve;
    }

    @Override
    public List<YieldCurve> loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
