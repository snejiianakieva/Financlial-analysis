package com.ers.internship.enums;

import java.util.Calendar;

/**
 *
 * @author Snejina Yanakieva
 */
public enum Frequency {

    // Every frequency object holds it's tenor type (DATE/MONTH/YEAR) and value (integer) for easier date hops
    ZERO(Calendar.DATE, 0),
    DAY(Calendar.DATE, 1),
    WEEK(Calendar.DATE, 7),
    TWO_WEEKS(Calendar.DATE, 14),
    MONTH(Calendar.MONTH, 1),
    TWO_MONTHS(Calendar.MONTH, 2),
    THREE_MONTHS(Calendar.MONTH, 3),
    FOUR_MONTHS(Calendar.MONTH, 4),
    SIX_MONTHS(Calendar.MONTH, 6),
    YEAR(Calendar.YEAR, 1),
    TWO_YEARS(Calendar.YEAR, 2);

    private final int type;
    private final int value;

    Frequency(int type, int value) {
        this.type = type;
        this.value = value;
    }

    /**
     *
     * @return the type of the calendar entity to be modified (DATE, MONTH or YEAR)
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @return the magnitude of the calendar entity modification
     */
    public int getValue() {
        return value;
    }

}
