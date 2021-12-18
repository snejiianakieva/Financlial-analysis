package com.ers.internship.instruments;

import com.ers.internship.enums.Frequency;
import java.util.Calendar;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author Snejina Yanakieva
 */
@JsonTypeName("creditRegular")
public class CreditRegular extends Instrument {

    private static final Logger logger = Logger.getLogger(CreditRegular.class.getName());

    /**
     * The number of months between two subsequent principal payments
     */
    private int tenorMonths;

    /**
     * The yearly interest rate percentage of the regular credit
     */
    private double interestRate;

    /**
     * The frequency of the interest payments
     */
    private Frequency frequency;

    /**
     * The issue date of the regular credit
     */
    private Calendar issue;

    /**
     * The maturity date of the regular credit
     */
    private Calendar maturity;

    public CreditRegular() {
        super();
    }

    /**
     *
     * @param id the ID of the regular credit
     */
    public CreditRegular(String id) {
        super(id);
    }

    /**
     *
     * @return the number of months between subsequent principal payments
     */
    public int getTenorMonths() {
        return tenorMonths;
    }

    /**
     * Sets the number of months between subsequent principal payments
     *
     * @param tenorMonths the number of months between subsequent principal payments
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
     * @return the frequency of the interest payments
     */
    public Frequency getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of the interest payments
     *
     * @param frequency the frequency of the interest payments
     */
    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    /**
     *
     * @return the issue date of the regular credit
     */
    public Calendar getIssue() {
        return issue;
    }

    /**
     * Sets the issue date of the regular credit
     *
     * @param issue the issue date of the regular credit
     */
    public void setIssue(Calendar issue) {
        this.issue = issue;
    }

    /**
     *
     * @return the maturity date of the regular credit
     */
    public Calendar getMaturity() {
        return maturity;
    }

    /**
     * Sets the maturity date of the regular credit
     *
     * @param maturity the maturity date of the regular credit
     */
    public void setMaturity(Calendar maturity) {
        this.maturity = maturity;
    }

}
