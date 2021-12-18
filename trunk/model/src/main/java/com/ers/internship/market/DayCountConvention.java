package com.ers.internship.market;

import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Utility class that provides functionality related to creation and manipulation of Calendar objects
 *
 * @author Snejina Yanakieva
 */
public class DayCountConvention {

    private static final Logger logger = Logger.getLogger(DayCountConvention.class.getName());

    /**
     * Calculates the distance in years between two calendar dates
     * 
     * @param from
     * @param to
     * @return
     */
    public static double getDistanceYears(Calendar from, Calendar to) {
        double years = to.get(Calendar.YEAR) - from.get(Calendar.YEAR);
        double months = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        double days = to.get(Calendar.DATE) - from.get(Calendar.DATE);
        double distance = (360 * years + 30 * months + days) / 360;
        return distance;
    }

    /**
     * Creates a Calendar instance with the specified year, month and day. All other fields are set to 0
     * 
     * @param year
     * @param month
     * @param day
     * @return 
     */
    public static Calendar getCalendarInstance(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(year, month - 1, day);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    /**
     * Creates a copy of a Calendar instance extracting its date and nullifying other fields
     * 
     * @param input
     * @return 
     */
    public static Calendar getCalendarCopy(Calendar input) {
        Calendar date = Calendar.getInstance();
        date.set(input.get(Calendar.YEAR), input.get(Calendar.MONTH), input.get(Calendar.DATE));
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    /**
     * Increases the date in the calendar object in accordance to the other parameters 
     * 
     * @param calendar The calendar instance to be modified
     * @param type Type of field to modify (e.g. Calendar.YEAR, Calendar.MONTH, Calendar.DATE)
     * @param value The magnitude of the modification
     */
    public static void incrementCalendar(Calendar calendar, int type, int value) {
        calendar.add(type, value);
    }

    /**
     * Decreases the date in the calendar object in accordance to the other parameters 
     * 
     * @param calendar The calendar instance to be modified
     * @param type Type of field to modify (e.g. Calendar.YEAR, Calendar.MONTH, Calendar.DATE)
     * @param value The magnitude of the modification
     */
    public static void decrementCalendar(Calendar calendar, int type, int value) {
        calendar.add(type, -value);
    }

    private DayCountConvention() {
    }

}
