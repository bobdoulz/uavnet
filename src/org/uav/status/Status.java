package org.uav.status;

import org.uav.status.Position;

public class Status {
    /**
     * Basic class containing all the status information
     * of a UAV
     */
    /** The position of the UAV */
    private Position currentPosition;
    /** The heading of the UAV */
    private Heading currentHeading;
    /** The speed of the UAV */
    private Speed currentSpeed;
    /** The turning rate of the UAV */
    private TurnRate currentTurnRate;

    /**
     * A complete constructor
     * @param currentPosition an instance of {@link Position}
     * @param currentHeading an instance of {@link Heading}
     * @param currentSpeed an instance of {@link Speed}
     * @param currentTurnRate an instance of {@link TurnRate} 
     */
    public Status(Position currentPosition, Heading currentHeading,
	    Speed currentSpeed, TurnRate currentTurnRate) {
	super();
	this.currentPosition = currentPosition;
	this.currentHeading = currentHeading;
	this.currentSpeed = currentSpeed;
	this.currentTurnRate = currentTurnRate;
    }

    /**
     * Copy constructor
     * @param currentStatus the status to copy
     */
    public Status(Status currentStatus) {
	this.currentHeading = new Heading(currentStatus.getCurrentHeading());
	this.currentPosition = new Position(currentStatus.getCurrentPosition());
	this.currentSpeed = new Speed(currentStatus.getCurrentSpeed());
	this.currentTurnRate = new TurnRate(currentStatus.getCurrentTurnRate());
    }

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    /**
     * To get the X-axis value of the current position
     * (shortcut method)
     * @return the X-axis value of the position
     */
    public double getPositionX(){
	return this.getCurrentPosition().getX();
    }

    /**
     * To get the Y-axis value of the current position
     * (shortcut method)
     * @return the Y-axis value of the position
     */
    public double getPositionY(){
	return this.getCurrentPosition().getY();
    }

    /**
     * To get the heading value 
     * (shortcut method)
     * @return the current heading value
     */
    public double getHeadingValue(){
	return this.getCurrentHeading().getHeadingValue();
    }

    /**
     * To get the speed value
     * (shortcut method)
     * @return the current speed value
     */
    public double getSpeedValue(){
	return this.getCurrentSpeed().getCurrentSpeed();
    }

    /**
     * To get the turning rate
     * @return the turning rate
     */
    public TurnRate getCurrentTurnRate() {
	return currentTurnRate;
    }

    /**
     * To set the turning rate
     * @param currentTurnRate an instance of {@link TurnRate}
     */
    public void setCurrentTurnRate(TurnRate currentTurnRate) {
	this.currentTurnRate = currentTurnRate;
    }

    /**
     * To get the instance of {@link Position} 
     * @return an instance of {@link Position} 
     */
    public Position getCurrentPosition() {
	return currentPosition;
    }

    /**
     * To set the currentPosition
     * @param currentPosition an instance of {@link Position} 
     */
    public void setCurrentPosition(Position currentPosition) {
	this.currentPosition = currentPosition;
    }

    /**
     * To get the instance of {@link Heading} 
     * @return an instance of {@link Heading} 
     */
    public Heading getCurrentHeading() {
	return currentHeading;
    }

    /**
     * To set the currentHeading
     * @param currentHeading an instance of {@link Heading} 
     */
    public void setCurrentHeading(Heading currentHeading) {
	this.currentHeading = currentHeading;
    }

    /**
     * To set the currentSpeed
     * @param currentSpeed an instance of {@link Speed} 
     */
    public Speed getCurrentSpeed() {
	return currentSpeed;
    }
    public void setCurrentSpeed(Speed currentSpeed) {
	this.currentSpeed = currentSpeed;
    }

}
