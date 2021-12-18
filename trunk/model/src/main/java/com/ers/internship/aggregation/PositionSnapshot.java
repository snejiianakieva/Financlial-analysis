package com.ers.internship.aggregation;

import com.ers.internship.position.Position;
import java.util.logging.Logger;

/**
 *
 * @author Snejina Yanakieva
 */
public class PositionSnapshot extends AbstractPortfolioItem {

    private static final Logger logger = Logger.getLogger(PositionSnapshot.class.getName());
    
    /**
     * The volume of the position snapshot
     */
    private double volume;
    
    /**
     * The position of the position snapshot
     */
    private Position position;

    /**
     *
     * @return the volume of the position snapshot
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Sets the volume of the position snapshot to the specified one
     *
     * @param volume the volume of the snapshot
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     *
     * @return the position of the snapshot
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the snapshot to the specified one
     *
     * @param position the position of the snapshot
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getCurrency() {
        return position.getInstrument().getCurrency();
    }

    @Override
    public String getName() {
        return position.getName();
    }

}
