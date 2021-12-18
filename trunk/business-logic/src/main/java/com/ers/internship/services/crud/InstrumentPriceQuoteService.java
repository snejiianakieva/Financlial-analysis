package com.ers.internship.services.crud;

import com.ers.internship.dao.InstrumentPriceQuoteDao;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.persistentstore.PersistentStore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ers.internship.market.YieldCurve;
import com.ers.internship.services.results.LoadResults;

/**
 *
 * @author Snejina Yanakieva
 */
public class InstrumentPriceQuoteService
        extends AbstractCrudService<InstrumentPriceQuoteId, InstrumentPriceQuote> {

    private static final Logger logger = Logger.getLogger(InstrumentPriceQuoteService.class.getName());

    public InstrumentPriceQuoteService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected InstrumentPriceQuoteDao getDao() {
        return getPersistentStore().getInstrumentPriceQuoteDao();
    }

    @Override
    protected List<String> validateItem(InstrumentPriceQuote instrumentPriceQuote) {
        List<String> result = new ArrayList<>();
        InstrumentPriceQuoteId id = instrumentPriceQuote.getID();
        if (validateNotNull(result, id, "Instrument price quote identificator not found")) {
            validateStringNotEmpty(result, id.getInstrumentId(), "Instrument price quote instrument identificator not found or empty");
            validateNotNull(result, id.getAtDate(), "Instrument price quote validity date not found");
        }
        validateStringNotEmpty(result, instrumentPriceQuote.getCurrency(), "Instrument price quote currency not found or empty");
        validateNumberPositive(result, instrumentPriceQuote.getInstrumentPrice(), "Instrument price quote instrument price must be positive");
        return result;
    }
	
	public LoadResults<InstrumentPriceQuote> loadAll() {
        LoadResults<InstrumentPriceQuote> results = new LoadResults<>();
        try {
            List<InstrumentPriceQuote> resultList = getDao().loadAll();
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
