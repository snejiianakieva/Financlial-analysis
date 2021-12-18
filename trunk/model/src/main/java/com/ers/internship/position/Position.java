package com.ers.internship.position;

import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.instruments.Instrument;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class Position extends HistorizedItem<String> {

    private static final Logger logger = Logger.getLogger(Position.class.getName());

    /**
     * The name of the position
     */
    private String name;
    
    /**
     * The long side of the position
     */
    private String longSide;
    
    /**
     * The short side of the position
     */
    private String shortSide;
    
    /**
     * The ID of the portfolio containing the position
     */
    private String portfolioId;
    
    /**
     * The instrument of the position
     */
    private Instrument instrument;

    public Position() {
    }

    /**
     *
     * @param id the ID of the position
     */
    public Position(String id) {
        super(id);
    }

    /**
     *
     * @return the name of the position
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the position
     *
     * @param name the name of the position
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the long side of the position
     */
    public String getLongSide() {
        return longSide;
    }

    /**
     * Sets the long side of the position
     *
     * @param longSide the long side of the position
     */
    public void setLongSide(String longSide) {
        this.longSide = longSide;
    }

    /**
     *
     * @return the short side of the position
     */
    public String getShortSide() {
        return shortSide;
    }

    /**
     * Sets the short side of the position
     *
     * @param shortSide the short side of the position
     */
    public void setShortSide(String shortSide) {
        this.shortSide = shortSide;
    }

    /**
     *
     * @return the ID of the portfolio containing the position
     */
    public String getPortfolioId() {
        return portfolioId;
    }

    /**
     * Sets the ID of the portfolio containing the position to the specified one
     *
     * @param portfolioId the ID of the portfolio containing the position
     */
    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    /**
     *
     * @return the instrument of the position
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * Sets the instrument of the position
     *
     * @param instrument the instrument of the position
     */
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

}
