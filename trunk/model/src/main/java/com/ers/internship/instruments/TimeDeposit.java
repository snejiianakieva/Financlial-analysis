package com.ers.internship.instruments;

import java.util.Calendar;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author Snejina Yanakieva
 */
@JsonTypeName("timeDeposit")
public class TimeDeposit extends Instrument {

    private static final Logger LOG = Logger.getLogger(TimeDeposit.class.getName());

    /**
     * The number of months between the issue date and the maturity of the time deposit
     */
    private int tenorMonths;

    /**
     * The yearly interest rate percentage
     */
    private double interestRate;

    /**
     * The issue date of the time deposit
     */
    private Calendar issue;

    public TimeDeposit() {
        super();
    }

    /**
     *
     * @param id
     */
    public TimeDeposit(String id) {
        super(id);
    }

    /**
     *
     * @return the number of months between the issue date and the maturity of the time deposit
     */
    public int getTenorMonths() {
        return tenorMonths;
    }

    /**
     *  Sets the number of months between the issue date and the maturity of the time deposit
     *
     * @param tenorMonths number of months between issue and maturity
     */
    public void setTenorMonths(int tenorMonths) {
        this.tenorMonths = tenorMonths;
    }

    /**
     *
     * @return the yearly interest rate percentage
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the yearly interest rate percentage
     *
     * @param interestRate the yearly interest rate percentage
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     *
     * @return the issue date of the time deposit
     */
    public Calendar getIssue() {
        return issue;
    }

    /**
     * Sets the issue date of the time deposit
     *
     * @param issue the issue date of the time deposit
     */
    public void setIssue(Calendar issue) {
        this.issue = issue;
    }

}
