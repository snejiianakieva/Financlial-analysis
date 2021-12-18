package com.ers.internship.exampledao.implementations;

import com.ers.internship.exampledao.abstracts.ExampleSearchingDao;
import com.ers.internship.instruments.Instrument;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class InstrumentExampleSearchingDaoImpl extends ExampleSearchingDao<String, Instrument> {

    private static final Logger logger = Logger.getLogger(InstrumentExampleSearchingDaoImpl.class.getName());

    @Override
    protected String getName(Instrument entity) {
        return entity.getIsin();
    }
}
