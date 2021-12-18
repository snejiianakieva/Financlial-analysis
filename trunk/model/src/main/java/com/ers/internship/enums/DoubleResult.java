package com.ers.internship.enums;

import com.ers.internship.calculation.CalculationResult;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author Snejina Yanakieva
 */
@JsonTypeName("doubleResult")
public enum DoubleResult implements CalculationResult {

    PV,
    PL;

    /**
     *
     * @return the name of the double result
     */
    @Override
    public String getName() {
        return this.name();
    }

}
