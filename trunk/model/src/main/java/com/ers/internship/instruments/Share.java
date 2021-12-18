package com.ers.internship.instruments;

import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author Snejina Yanakieva
 */
@JsonTypeName("share")
public class Share extends Instrument {

    private static final Logger logger = Logger.getLogger(Share.class.getName());

    public Share() {
        super();
    }

    /**
     *
     * @param id the ID of the share
     */
    public Share(String id) {
        super(id);
    }

}
