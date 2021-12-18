package com.ers.internship.services.calculation;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import com.ers.internship.calculation.CalculationResult;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.services.results.PortfolioItemResultStructure;

/**
 * Represents a request for calculating portfolio results
 *
 * @author Snejina Yanakieva
 */
public class PortfolioCalculationRequest {

    private static final Logger LOGGER = Logger.getLogger(PortfolioCalculationRequest.class.getName());

    private String portfolioId;
    private Calendar portfolioDate;
    private Calendar evaluationDate;
    private PortfolioItemResultStructure structure;
    private List<CalculationResult> requestedResults;

    public PortfolioCalculationRequest() {

    }

    /**
     * 
     * @return The requested structure for the portfolio
     */
    public PortfolioItemResultStructure getStructure() {
        return structure;
    }

    public void setStructure(PortfolioItemResultStructure structure) {
        this.structure = structure;
    }

    /**
     * 
     * @return The id of the {@link Portfolio}
     */
    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    /**
     * 
     * @return The portfolio snapshot date
     */
    public Calendar getPortfolioDate() {
        return portfolioDate;
    }

    public void setPortfolioDate(Calendar portfolioDate) {
        this.portfolioDate = portfolioDate;
    }

    /**
     * 
     * @return The evaluation date
     */
    public Calendar getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Calendar evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    /**
     * 
     * @return List of the results that will be required by this request
     */
    public List<CalculationResult> getRequestedResults() {
        return requestedResults;
    }

    public void setRequestedResults(List<CalculationResult> requestedResults) {
        this.requestedResults = requestedResults;
    }
    
}
