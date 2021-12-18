package com.ers.internship.services.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ers.internship.aggregation.PortfolioItem;
import com.ers.internship.aggregation.PortfolioSnapshot;
import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.calculation.Calculator;
import com.ers.internship.calculation.PresentValueCalculator;
import com.ers.internship.enums.DoubleResult;
import com.ers.internship.enums.PortfolioItemType;
import com.ers.internship.market.Market;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.services.loaders.ByCurrencyLoader;
import com.ers.internship.services.loaders.FlatLoader;
import com.ers.internship.services.loaders.PortfolioLoader;
import com.ers.internship.services.results.LoadResult;
import com.ers.internship.services.results.PortfolioItemResult;
import com.ers.internship.services.results.PortfolioItemResultStructure;

/**
 * An object that calculates various results for a portfolio snapshot.
 * 
 * @author Snejina Yanakieva
 */
public class CalculationService {

    private static final Logger LOGGER = Logger.getLogger(CalculationService.class.getName());

    private final PersistentStore persistentStore;

    private Map<PortfolioItemResultStructure, PortfolioLoader> loaders;
    private Map<CalculationResult, Calculator> calculators;
    private Map<Class<? extends PortfolioItem>, PortfolioItemType> types;

    private Market market;

    /**
     * Initializes the loaders, calculators and types maps. 
     */
    private void initializeMaps() {
        loaders = new HashMap<>();
        calculators = new HashMap<>();
        types = new HashMap<>();

        setPortfolioLoader(PortfolioItemResultStructure.FLAT,
                new FlatLoader(persistentStore));
        setPortfolioLoader(PortfolioItemResultStructure.BY_CURRENCY,
                new ByCurrencyLoader(persistentStore));

        calculators.put(DoubleResult.PV, new PresentValueCalculator());

        types.put(PortfolioSnapshot.class, PortfolioItemType.PORTFOLIO);
        types.put(PositionSnapshot.class, PortfolioItemType.POSITION);
    }
    
    /**
     * Recursively builds a {@link PortfolioItemResult} from a {@link PortfolioItem} which 
     * has all the requested results already calculated
     * @param snapshot The {@link PortfolioItem} with the calculated results from which the 
     * {@link PortfolioItemResult} is created
     * @param resultTypes The results which the {@link PortfolioItemResult} should contain
     * @return The {@link PortfolioItemResult} containing the requested results
     */
    private PortfolioItemResult buildPortfolioItemResult(PortfolioItem snapshot,
            List<CalculationResult> resultTypes) {

        PortfolioItemResult result = new PortfolioItemResult();

        result.setItemId(snapshot.getId());
        result.setItemName(snapshot.getName());
        result.setItemType(types.get(snapshot.getClass()));
        Map<CalculationResult, Object> snapshotResultValues = snapshot.getResults();

        for (CalculationResult resultType : resultTypes) {
            Object currentResult = snapshotResultValues.get(resultType);
            result.addResult(resultType, currentResult);
        }

        if (snapshot instanceof PortfolioSnapshot) {
            PortfolioSnapshot portfolioSnapshot = (PortfolioSnapshot) snapshot;

            for (PortfolioItem item : portfolioSnapshot.getItems()) {
                result.addChild(buildPortfolioItemResult(item, resultTypes));
            }

        }

        return result;
    }
       
    /**
     * Validates if an object is NOT null.
     * 
     * @param errorList The list of errors in which the message is inserted
     * if the object being validated is null. If the object is NOT null the
     * list is left unchanged
     * @param o The object to validate
     * @param message The message to be inserted in the errorList if the object
     * is null
     * @return true if the object is NOT null, false otherwise
     */
    private boolean validateNotNull(List<String> errorList, Object o, String message) {
        if (o == null) {
            errorList.add(message);
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if a {@link PortfolioCalculationRequest} is considered valid.
     * @param request The {@link PortfolioCalculationRequest} to be validated
     * @return A list containing description of the errors found during the validation. The 
     * {@link PortfolioCalculationRequest} should be considered valid if and only if this list is empty.
     */
    private List<String> validateRequest(PortfolioCalculationRequest request) {
        List<String> errorList = new ArrayList<>();

        List<CalculationResult> requestedResults = request.getRequestedResults();
        if (requestedResults == null) {
        	errorList.add("No results requested!");
        }
        else {
        	if (requestedResults.isEmpty()) {
        		errorList.add("No results requested!");
        	}
        	else if (requestedResults.contains(null)) {
        		errorList.add("Invalid result requested!");
        	}
        }

        validateNotNull(errorList, request.getEvaluationDate(), "The evaluation date is not set!");
        validateNotNull(errorList, request.getPortfolioDate(), "The portfolio date is not set!");
        validateNotNull(errorList, request.getPortfolioId(), "The portfolio ID is not set!");
        validateNotNull(errorList, request.getStructure(), "The result structure is not set!");

        return errorList;
    }

    /**
     * Constructs CalculationService
     * 
     * @param persistentStore The {@link PersistentStore} object which will be used as the 
     * source for the data needed to do the calculations.
     */
    public CalculationService(PersistentStore persistentStore) {
        this.persistentStore = persistentStore;
        initializeMaps();
        market = new MarketImpl(persistentStore);
    }
    
    /**
     * Calculates the requested results
     * @param request The calculation request
     * @return The requested results
     */

    public LoadResult<PortfolioItemResult> calculate(PortfolioCalculationRequest request) {
        CachingMarket cachingMarket = new CachingMarket(market);
        LoadResult<PortfolioItemResult> result = new LoadResult<>();

        List<String> requestValidationErrors = validateRequest(request);
        if (requestValidationErrors != null && !requestValidationErrors.isEmpty()) {
            result.setErrors(requestValidationErrors);
            LOGGER.info("Request validation failed!");
            return result;
        }

        PortfolioLoader loader = loaders.get(request.getStructure());

        if (loader == null) {
            result.addError("SYSTEM ERROR! The necessary portfolio snapshot loader was not found!");
            LOGGER.info("SYSTEM ERROR! The necessary portfolio snapshot loader was not found!");
            return result;
        }

        try {
            PortfolioSnapshot snapshot = loader.loadSnapshot(request.getPortfolioId(),
                    request.getPortfolioDate());
            List<CalculationResult> requestedResults = request.getRequestedResults();

            Calculator currentCalculator;
            for (CalculationResult requestedResult : requestedResults) {
                currentCalculator = calculators.get(requestedResult);
                currentCalculator.calculate(snapshot, cachingMarket, request.getEvaluationDate());
            }

            result.setEntity(buildPortfolioItemResult(snapshot, requestedResults));
        } catch (Exception e) {
        	LOGGER.info("Exception caught while calculating!" + e.getClass().getCanonicalName());
            result.addError("Calculation Service Error");
        }

        return result;
    }
    
    /**
     * Sets a portfolio loader for the specified structure. If a loader for the structure specified
     * already exists, it will be overridden
     * @param structure The structure for which the loader is set
     * @param loader The loader to be set for the structure specified
     * @throws IllegalArgumentException if at least one of the parameters is null
     */

    public void setPortfolioLoader(PortfolioItemResultStructure structure, PortfolioLoader loader) {
    	if (structure == null || loader == null) {
    		throw new IllegalArgumentException("A null argument was passed!");
    	}
    	
        loaders.put(structure, loader);
    }

    /**
     * Returns the {@link Market} object used by this service to extract the data needed
     * for calculations
     * @return the {@link Market} object used by this service
     */

    public Market getMarket() {
        return market;
    }

}
