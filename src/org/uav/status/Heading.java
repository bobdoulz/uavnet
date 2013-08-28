package org.uav.status;

/**
 * @author Julien Schleich
 * A class in charge of dealing with the current heading
 * and its changing behavior considering the current
 * destination 
 */
public class Heading {
    /** The current heading value (in rad) */
    private double currentHeading;

    /** The maximum heading change per timestep */
    private double maxHeadingChange;

    /** The maximum rad value */
    private static double maxHeading = 2*Math.PI;

    /**
     * A constructor with the initial heading parameter
     * @param currentHeading the value of the initial heading
     */
    public Heading(double currentHeading) {
	this.currentHeading = currentHeading;
	this.maxHeadingChange = Heading.maxHeading;
    }

    /**
     * A constructor with both parameters
     * @param currentHeading the initial heading value
     * @param maxHeadingChange the maximum heading change per timestep
     */
    public Heading(double currentHeading, double maxHeadingChange) {
	this.currentHeading = currentHeading;
	this.maxHeadingChange = maxHeadingChange;
    }

    /**
     * Copy constructor
     * @param heading
     */
    public Heading(Heading heading){
	this(heading.currentHeading, heading.maxHeadingChange);
    }


    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    /**
     * To get the current heading value
     * @return the current heading value
     */
    public double getHeadingValue() {
	return currentHeading;
    }

    /**
     * To set the next heading value depending on the destination 
     * @param targetHeading the heading to go to the destination
     * in a straight line
     */
    public void setCurrentHeading(double targetHeading) {

	if (targetHeading - this.currentHeading > 0)
	    if (Math.abs(targetHeading - this.currentHeading) > Math.PI)
		if (Math.abs(targetHeading - this.currentHeading) > 
		maxHeadingChange)
		    this.currentHeading -= maxHeadingChange;
		else
		    this.currentHeading = targetHeading;
	    else
		if (Math.abs(targetHeading - this.currentHeading) >
		maxHeadingChange)
		    this.currentHeading += maxHeadingChange;
		else
		    this.currentHeading = targetHeading;
	else
	    if (Math.abs(targetHeading - this.currentHeading) > Math.PI)
		if (Math.abs(targetHeading - this.currentHeading) >
maxHeadingChange)
		    this.currentHeading += maxHeadingChange;
		else
		    this.currentHeading = targetHeading;
	    else
		if (Math.abs(targetHeading - this.currentHeading) > 
		maxHeadingChange)
		    this.currentHeading -= maxHeadingChange;
		else
		    this.currentHeading = targetHeading;

	if (this.currentHeading < 0)
	    this.currentHeading = 2*Math.PI + this.currentHeading;
	this.currentHeading = currentHeading % maxHeading;
    }

    /**
     * Returns the max heading change value
     * @return the max heading change value
     */
    public double getMaxHeadingChange() {
	return maxHeadingChange;
    }

    /**
     * To set the maxHeadingChange value
     * @param maxHeadingChange the maxHeadingChange value
     */
    public void setMaxHeadingChange(double maxHeadingChange) {
	this.maxHeadingChange = maxHeadingChange;
    }

}
