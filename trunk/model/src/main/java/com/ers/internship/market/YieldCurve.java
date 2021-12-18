package com.ers.internship.market;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.identificators.YieldCurveId;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class YieldCurve extends HistorizedItem<YieldCurveId> {

    private static final Logger logger = Logger.getLogger(YieldCurve.class.getName());

    private double zeroYieldThreeMonths;
    private double zeroYieldSixMonths;
    private double zeroYieldOneYear;
    private double zeroYieldTwoYears;
    private double zeroYieldFiveYears;
    private double zeroYieldTenYears;
    private double zeroYieldThirtyYears;

    public YieldCurve() {
        super();
    }

    public YieldCurve(YieldCurveId id) {
        super(id);
    }

    public String getCurrency() {
        return getID().getCurrency();
    }

    public void setCurrency(String currency) {
        getID().setCurrency(currency);
    }

    public Calendar getDate() {
        return getID().getAtDate();
    }

    public void setDate(Calendar atDate) {
        getID().setAtDate(atDate);
    }

    public double getZeroYieldThreeMonths() {
        return zeroYieldThreeMonths;
    }

    public void setZeroYieldThreeMonths(double zeroYieldThreeMonths) {
        this.zeroYieldThreeMonths = zeroYieldThreeMonths;
    }

    public double getZeroYieldSixMonths() {
        return zeroYieldSixMonths;
    }

    public void setZeroYieldSixMonths(double zeroYieldSixMonths) {
        this.zeroYieldSixMonths = zeroYieldSixMonths;
    }

    public double getZeroYieldOneYear() {
        return zeroYieldOneYear;
    }

    public void setZeroYieldOneYear(double zeroYieldOneYear) {
        this.zeroYieldOneYear = zeroYieldOneYear;
    }

    public double getZeroYieldTwoYears() {
        return zeroYieldTwoYears;
    }

    public void setZeroYieldTwoYears(double zeroYieldTwoYears) {
        this.zeroYieldTwoYears = zeroYieldTwoYears;
    }

    public double getZeroYieldFiveYears() {
        return zeroYieldFiveYears;
    }

    public void setZeroYieldFiveYears(double zeroYieldFiveYears) {
        this.zeroYieldFiveYears = zeroYieldFiveYears;
    }

    public double getZeroYieldTenYears() {
        return zeroYieldTenYears;
    }

    public void setZeroYieldTenYears(double zeroYieldTenYears) {
        this.zeroYieldTenYears = zeroYieldTenYears;
    }

    public double getZeroYieldThirtyYears() {
        return zeroYieldThirtyYears;
    }

    public void setZeroYieldThirtyYears(double zeroYieldThirtyYears) {
        this.zeroYieldThirtyYears = zeroYieldThirtyYears;
    }

    private double calculateInterestRate(double distance) {
        double interestRate;
        // Interpolate the exact interest rate according to the distance from the evaluation date
        if (distance < 0.25) {
            interestRate = ((zeroYieldThreeMonths - 0) / (0.25 - 0)) * distance;
        } else if (distance < 0.5) {
            interestRate = zeroYieldThreeMonths + ((zeroYieldSixMonths - zeroYieldThreeMonths) / (0.5 - 0.25)) * (distance - 0.25);
        } else if (distance < 1) {
            interestRate = zeroYieldSixMonths + ((zeroYieldOneYear - zeroYieldSixMonths) / (1 - 0.5)) * (distance - 0.5);
        } else if (distance < 2) {
            interestRate = zeroYieldOneYear + ((zeroYieldTwoYears - zeroYieldOneYear) / (2 - 1)) * (distance - 1);
        } else if (distance < 5) {
            interestRate = zeroYieldTwoYears + ((zeroYieldFiveYears - zeroYieldTwoYears) / (5 - 2)) * (distance - 2);
        } else if (distance < 10) {
            interestRate = zeroYieldFiveYears + ((zeroYieldTenYears - zeroYieldFiveYears) / (10 - 5)) * (distance - 5);
        } else if (distance < 30) {
            interestRate = zeroYieldTenYears + ((zeroYieldThirtyYears - zeroYieldTenYears) / (30 - 10)) * (distance - 10);
        } else {
            interestRate = zeroYieldThirtyYears;
        }
        return interestRate;
    }

    /**
     * Calculates the discount factor between the input date and the inner yield curve date
     * 
     * @param date
     * @return 
     */
    public double calculateDiscountFactor(Calendar date) {
        double distance = DayCountConvention.getDistanceYears(getDate(), date);
        double interestRate = calculateInterestRate(distance);
        if (distance < 1) {
            return 1 / (1 + (interestRate / 100) * distance); // If distance is less than a year, return the arithmetic discount factor
        } else {
            return 1 / Math.pow((1 + (interestRate / 100)), distance); // Otherwise return the geometric discount factor
        }
    }

}
