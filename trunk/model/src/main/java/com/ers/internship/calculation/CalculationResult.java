package com.ers.internship.calculation;

import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 *
 * @author Snejina Yanakieva
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public interface CalculationResult {

    public String getName();

}
