package com.ers.internship.services.crud.searching;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.persistentstore.PersistentStore;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class InstrumentService extends AbstractSearchingService<String, Instrument> {

    private static final Logger logger = Logger.getLogger(InstrumentService.class.getName());

    public InstrumentService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected SearchingDao<String, Instrument> getDao() {
        return getPersistentStore().getInstrumentDao();
    }

    @Override
    protected List<String> validateItem(Instrument instrument) {
        List<String> result = new ArrayList<>();
        validateStringNotEmpty(result, instrument.getID(), "Instrument identificator not found or empty");
        validateStringNotEmpty(result, instrument.getIsin(), "Instrument isin not found or empty");
        validateStringNotEmpty(result, instrument.getCurrency(), "Instrument currency not found or empty");
        validateStringNotEmpty(result, instrument.getMarket(), "Instrument market not found or empty");
        return result;
    }

}
