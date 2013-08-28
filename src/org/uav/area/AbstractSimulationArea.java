package org.uav.area;

import java.util.Iterator;

import org.graphstream.graph.implementations.DefaultGraph;
import org.uav.status.Position;

/**
 * @author Julien Schleich
 * An abstract class to represent a simulation area
 */
abstract public class AbstractSimulationArea {

    /** The main graph (for some access) */
    protected DefaultGraph g;

    /** 
     * This method refresh the display of the simulation area 
     */
    abstract public void refreshDisplay();

    /**
     * This method generates the complete environment  
     */
    public abstract void generateEnvironment();

    /**
     * This method test if a position is in the area
     * @param p the position to test
     * @return true if the position is out of the area, false otherwise
     */
    public abstract boolean isOutOfArea(Position p);

    /**
     * This method test if a position is in the area
     * @param x the x axis coordinate of the position to test
     * @param y the y axis coordinate of the position to test
     * @return true if out of the area, false otherwise
     */
    public abstract boolean isOutOfArea(int x, int y);

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    public DefaultGraph getG() {
	return g;
    }

    public void setG(DefaultGraph g) {
	this.g = g;
    }


}
