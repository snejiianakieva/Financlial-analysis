package com.ers.internship.enums;

import com.ers.internship.calculation.CalculationResult;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author Snejina Yanakieva
 */
@JsonTypeName("stringResult")
public enum StringResult implements CalculationResult {

    ID,
    NAME;

    /**
     *
     * @return the name of the string result
     */
    @Override
    public String getName() {
        return this.name();
    }

}
