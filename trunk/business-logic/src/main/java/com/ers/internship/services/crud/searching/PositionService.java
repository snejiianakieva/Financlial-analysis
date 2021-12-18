package com.ers.internship.services.crud.searching;

import com.ers.internship.dao.SearchingDao;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.position.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class PositionService extends AbstractSearchingService<String, Position> {

    private static final Logger logger = Logger.getLogger(PositionService.class.getName());

    public PositionService(PersistentStore persistentStore) {
        super(persistentStore);
    }

    @Override
    protected SearchingDao<String, Position> getDao() {
        return getPersistentStore().getPositionDao();
    }

    @Override
    protected List<String> validateItem(Position position) {
        List<String> result = new ArrayList<>();
        validateStringNotEmpty(result, position.getID(), "Position identificator not found or is empty");
        validateStringNotEmpty(result, position.getName(), "Position name not found or is empty");
        validateStringNotEmpty(result, position.getLongSide(), "Position's long side not found or is empty");
        validateStringNotEmpty(result, position.getShortSide(), "Position's short side not found or is empty");
        validateStringNotEmpty(result, position.getPortfolioId(), "Position's portfolio identificator not found or is empty");
        validateNotNull(result, position.getInstrument(), "Position's instrument not found");
        return result;
    }

}
