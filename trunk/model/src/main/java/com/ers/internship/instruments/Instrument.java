package com.ers.internship.instruments;

import com.ers.internship.historization.HistorizedItem;
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
public abstract class Instrument extends HistorizedItem<String> {

    /**
     * The ISIN of the instrument
     */
    private String isin;
    
    /**
     * The currency of the instrument
     */
    private String currency;
    
    /**
     * The market of the instrument
     */
    private String market;

    public Instrument() {
        super();
    }

    /**
     *
     * @param id the ID of the instrument
     */
    public Instrument(String id) {
        super(id);
    }

    /**
     *
     * @return the ISIN of the instrument
     */
    public String getIsin() {
        return isin;
    }

    /**
     * Sets the ISIN of the instrument
     *
     * @param isin the ISIN of the instrument
     */
    public void setIsin(String isin) {
        this.isin = isin;
    }

    /**
     *
     * @return the currency of the instrument
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the instrument
     *
     * @param currency the currency of the instrument
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     *
     * @return the market of the instrument
     */
    public String getMarket() {
        return market;
    }

    /**
     * Sets the market of the instrument
     *
     * @param market the market of the instrument
     */
    public void setMarket(String market) {
        this.market = market;
    }

}
