package com.ers.internship.exampledao.implementations;

import com.ers.internship.exampledao.abstracts.ExampleSearchingDao;
import com.ers.internship.position.Position;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class PositionExampleSearchingDaoImpl extends ExampleSearchingDao<String, Position> {

    private static final Logger logger = Logger.getLogger(PositionExampleSearchingDaoImpl.class.getName());

    @Override
    protected String getName(Position entity) {
        return entity.getName();
    }
}
