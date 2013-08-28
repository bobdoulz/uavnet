package org.uav.status;

/**
 * @author Julien Schleich
 * A class to take care of the current speed
 * of a UAV
 */
public class Speed {
    /** The current speed value */
    private double currentSpeed;
    /** The minimum speed value */
    private double minSpeed;
    /** The maximum speed value */
    private double maxSpeed;
    /** The maximum acceleration value */
    private double maxAccel;
    /** The maximum deceleration value */
    private double maxDecel;

    /**
     * A complete constructor
     * @param currentSpeed the initial speed
     * @param minSpeed the min speed value
     * @param maxSpeed the max speed value
     * @param maxAccel the max accel value
     * @param maxDecel the max decel value
     */
    public Speed(double currentSpeed, double minSpeed, double maxSpeed, 
	    double maxAccel, double maxDecel) {
	super();
	this.currentSpeed = currentSpeed;
	this.maxSpeed = maxSpeed;
	this.minSpeed = minSpeed;
	this.maxAccel = maxAccel;
	this.maxDecel = maxDecel;
    }

    /**
     * A constructor without accel / decel values
     * @param currentSpeed the initial speed
     * @param minSpeed the min speed value
     * @param maxSpeed the max speed value
     */
    public Speed(double currentSpeed, double minSpeed, double maxSpeed) {
	super();
	this.currentSpeed = currentSpeed;
	this.maxSpeed = maxSpeed;
	this.minSpeed = minSpeed;
	this.maxAccel = 0;
	this.maxDecel = 0;
    }

    /**
     * A fixed speed constructor
     * @param currentSpeed the initial speed value
     */
    public Speed(double currentSpeed) {
	super();
	this.currentSpeed = currentSpeed;
	this.maxSpeed = currentSpeed;
	this.minSpeed = currentSpeed;
	this.maxAccel = 0;
	this.maxDecel = 0;
    }

    /**
     * Copy constructor
     * @param speed
     */
    public Speed(Speed speed){
	this(speed.currentSpeed, speed.minSpeed, speed.maxSpeed, speed.maxAccel, speed.maxDecel);
    }

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    /**
     * To get the current speed value
     * @return the current speed value
     */
    public double getCurrentSpeed() {
	return currentSpeed;
    }

    /**
     * To set the current speed value depending on a target speed
     * @param targetSpeed the target speed value
     */
    public void setCurrentSpeed(double targetSpeed) {
	// Changing current speed and checking if the change does not violate the UAV possibilities
	if ( (targetSpeed > this.currentSpeed)
		&& (this.currentSpeed < maxSpeed) ){
	    if ((targetSpeed - this.currentSpeed) > maxAccel)
		this.currentSpeed += maxAccel;
	    else
		this.currentSpeed = targetSpeed;

	}
	if ( (targetSpeed < this.currentSpeed)
		&& (this.currentSpeed > minSpeed) ){
	    if ((this.currentSpeed - targetSpeed) > maxDecel)
		this.currentSpeed -= maxDecel;
	    else
		this.currentSpeed = targetSpeed;
	}

    }

    /**
     * To get the max speed value
     * @return the max speed value
     */
    public double getMaxSpeed() {
	return maxSpeed;
    }

    /**
     * To set the max speed value
     * @param maxSpeed the max speed value
     */
    public void setMaxSpeed(double maxSpeed) {
	this.maxSpeed = maxSpeed;
    }

    /** 
     * To get the min speed value
     * @return the min speed value
     */
    public double getMinSpeed() {
	return minSpeed;
    }

    /**
     * To set the min speed value
     * @param minSpeed the min speed value
     */
    public void setMinSpeed(double minSpeed) {
	this.minSpeed = minSpeed;
    }

    /**
     * To get the max accel value
     * @return the max accel value
     */
    public double getMaxAccel() {
	return maxAccel;
    }

    /**
     * To set the max accel value
     * @param maxAccel the max accel value
     */
    public void setMaxAccel(double maxAccel) {
	this.maxAccel = maxAccel;
    }

    /**
     * To get the max decel value
     * @return the max decel value
     */
    public double getMaxDecel() {
	return maxDecel;
    }

    /**
     * To set the max decel value
     * @param maxDecel the max decel value
     */
    public void setMaxDecel(double maxDecel) {
	this.maxDecel = maxDecel;
    }

}
