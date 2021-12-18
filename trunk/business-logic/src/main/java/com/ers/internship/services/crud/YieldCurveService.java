package com.ers.internship.services.crud;

import com.ers.internship.dao.CrudDao;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.persistentstore.PersistentStore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ers.internship.dao.YieldCurveDao;
import com.ers.internship.services.results.LoadResults;

/**
 *
 * @author Snejina Yanakieva
 */
public class YieldCurveService extends AbstractCrudService<YieldCurveId, YieldCurve> {

    private static final Logger logger = Logger.getLogger(YieldCurveService.class.getName());

    private final int minimumInterestRate = 0;
    private final int maximumInterestRate = 30;

    public YieldCurveService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected YieldCurveDao getDao() {
        return getPersistentStore().getYieldCurveDao();
    }

    private boolean validateInterestRate(List<String> errors, double interestRate, String period) {
        if (interestRate > minimumInterestRate && interestRate < maximumInterestRate) {
            return true;
        } else {
            errors.add(String.format("%s interest rate must be between %d and %d", period, minimumInterestRate, maximumInterestRate));
            return false;
        }
    }

    @Override
    protected List<String> validateItem(YieldCurve yieldCurve) {
        List<String> result = new ArrayList<>();
        YieldCurveId id = yieldCurve.getID();
        if (validateNotNull(result, id, "Yield curve identificator not found")) {
            validateStringNotEmpty(result, id.getCurrency(), "Yield curve currency not found or empty");
            validateNotNull(result, id.getAtDate(), "Yield curve validity date not found");
        }
        validateInterestRate(result, yieldCurve.getZeroYieldThreeMonths(), "Three months");
        validateInterestRate(result, yieldCurve.getZeroYieldSixMonths(), "Six months");
        validateInterestRate(result, yieldCurve.getZeroYieldOneYear(), "One year");
        validateInterestRate(result, yieldCurve.getZeroYieldTwoYears(), "Two years");
        validateInterestRate(result, yieldCurve.getZeroYieldFiveYears(), "Five years");
        validateInterestRate(result, yieldCurve.getZeroYieldTenYears(), "Ten years");
        validateInterestRate(result, yieldCurve.getZeroYieldThirtyYears(), "Thirty years");
        return result;
    }
	
	 public LoadResults<YieldCurve> loadAll() {
        LoadResults<YieldCurve> results = new LoadResults<>();
        try {
            List<YieldCurve> resultList = getDao().loadAll();
            if (resultList == null) {
                results.addError("No entities");
            } else {
                results.setEntities(resultList);
            }
        } catch (Exception e) {
            results.addError(e.getMessage());
        }
        return results;
    }
    
}
