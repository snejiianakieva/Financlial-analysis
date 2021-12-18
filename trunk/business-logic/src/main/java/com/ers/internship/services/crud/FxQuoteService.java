package com.ers.internship.services.crud;

import com.ers.internship.dao.FxQuoteDao;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.market.FxQuote;
import com.ers.internship.persistentstore.PersistentStore;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class FxQuoteService extends AbstractCrudService<FxQuoteId, FxQuote> {

    private static final Logger logger = Logger.getLogger(FxQuoteService.class.getName());

    public FxQuoteService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected FxQuoteDao getDao() {
        return getPersistentStore().getFxQuoteDao();
    }

    @Override
    protected List<String> validateItem(FxQuote fxQuote) {
        List<String> result = new ArrayList<>();
        FxQuoteId id = fxQuote.getID();
        if (validateNotNull(result, id, "FX quote identificator not found")) {
            validateStringNotEmpty(result, id.getFromCurrency(), "FX quote base currency not found or empty");
            validateStringNotEmpty(result, id.getToCurrency(), "FX quote target currency not found or empty");
            validateNotNull(result, id.getAtDate(), "FX quote identificator validity date not found");
        }
        validateNumberPositive(result, fxQuote.getValue(), "FX value must be positive");
        return result;
    }

}
