package org.uav.autopilot;

import java.util.Random;

import org.uav.autopilot.Destination;
import org.uav.area.AbstractSimulationArea;
import org.uav.status.Position;
import org.uav.status.Status;

/**
 * @author Julien Schleich
 * The abstract class for the autopilot hierarchy 
 */
public abstract class AbstractAutopilot {
    /**
     * Contain several information relative to the UAV status:
     * 	- The speed
     * 	- The heading
     * 	- The turning rate
     * 	- The position 
     */
    protected Status currentStatus;

    /**
     * The position of the current destination
     */
    protected Destination currentDestination;

    /**
     * The simulation area
     */
    protected AbstractSimulationArea area;

    protected Random rand;


    ////////////////////////////////////////////////////////////////////////

    /**
     * Constructor
     * @param currentStatus the current status of the UAV
     * @param currentDestination the current destination
     */
    public AbstractAutopilot(
	    Status currentStatus, 
	    Destination currentDestination,
	    AbstractSimulationArea area) {
	super();
	this.currentDestination = currentDestination;
	this.currentStatus = currentStatus;
	this.area = area;
	this.rand = new Random();
    }

    ////////////////////////////////////////////////////////////////////////


    /*********************************************
     * 
     * High-level methods of the autopilot
     * to update the status
     * 
     ********************************************/

    /**
     * This method returns the new heading to go to the destination
     * @return the new heading
     */
    abstract protected double getUpdatedHeading();

    /**
     * This method should influence the heading of the UAV
     * in order to target the current destination 
     */
    protected void updateHeading(){
	this.getCurrentStatus().getCurrentHeading().setCurrentHeading(
		getUpdatedHeading());
    }

    /**
     * This method returns the new turning rate to go to the destination
     * @return the new turning rate 
     */
    abstract protected int getUpdatedTurnRate();

    /**
     * This method permits to emulate the turning motion of a UAV
     */
    protected void updateTurnRate(){
	this.getCurrentStatus().getCurrentTurnRate().setCurrentTurnRate(
		getUpdatedTurnRate());
    }

    /**
     * This method returns the new speed to go to the destination
     * @return the new speed value
     */
    abstract protected double getUpdatedSpeed();

    /**
     * This method permits to adapt the current speed of a UAV
     * depending on the situation.
     */
    protected void updateSpeed(){
	this.getCurrentStatus().getCurrentSpeed().setCurrentSpeed(
		getUpdatedSpeed());
    }

    /**
     * This method takes into account the current status to compute
     * the next position of the UAV.
     * @return the new position
     */
    abstract protected Position getUpdatedPosition();

    /**
     * This method sets the new position to go to the destination
     */
    protected void updatePosition(){
	this.getCurrentStatus().setCurrentPosition(getUpdatedPosition());
    }

    /**
     * Modify the currentStatus based on:
     * 	- The current status
     *  - The current destination
     */
    public void doNextMove(){
	updateSpeed();
	updateTurnRate();
	updateHeading();
	updatePosition();	
    }

    /*********************************************
     * 
     * Destination-based methods
     * 
     ********************************************/


    /**
     * Creates a new random Destination instance
     * @return an instance of {@link Destination}
     */
    abstract public Position newRandomDestination();

    /**
     * This method updates the position of the destination
     * @param newPosition an instance of {@link Position}
     */
    public void setCurrentDestination(Position newPosition) {
	this.currentDestination.setCurrentDestination(newPosition);
    }

    /**
     * This methods computes the next nbSimulationStep positions composing
     * the trajectory to a destination
     * @param destination the destination
     * @param nbSimulationStep the number of simulation steps
     * @return the trajectory positions
     */
    public Position[] getTrajectorySteps(Position destination, 
	    int nbSimulationStep) {
	Position[] trajectory = new Position[nbSimulationStep];

	/** Backuping the current autopilot */
	Status statusBackup = new Status(this.currentStatus);
	Destination destBackup = new Destination(this.currentDestination);

	/** Trajectory computation */
	this.currentDestination.setCurrentDestination(destination);
	for (int i=0; i < nbSimulationStep; i++){
	    this.doNextMove();
	    trajectory[i] = this.currentStatus.getCurrentPosition();
	}

	/** Restoring the autopilot */
	this.currentStatus = statusBackup;
	this.currentDestination = destBackup;

	return trajectory;
    }

    /**
     * Returns the last point in the trajectory going from the current
     * position to the given destination in nbSimulation Steps
     * @param destination the destination
     * @param nbSimulationStep the number of simulation steps
     * @return the last position
     */
    public Position getLastPointOnTrajectory(Position destination, 
	    int nbSimulationStep) {
	Position[] trajectory = getTrajectorySteps(destination, 
		nbSimulationStep);
	return trajectory[trajectory.length-1];
    }

    /**
     * This methods computes the next nbSimulationStep positions composing
     * the trajectory from some status to a destination
     * @param initStatus the status (position, heading ...) to start
     * @param destination the destination
     * @param nbSimulationStep the number of simulation steps
     * @return the trajectory positions
     */
    public Position[] getTrajectorySteps(Status initStatus, 
	    Position destination, int nbSimulationStep) {
	Position[] trajectory = new Position[nbSimulationStep];

	/** Backuping the current autopilot */
	Status statusBackup = new Status(this.currentStatus);
	Destination destBackup = new Destination(this.currentDestination);

	/** Trajectory computation */
	this.currentDestination.setCurrentDestination(destination);
	this.setCurrentStatus(initStatus);
	for (int i=0; i < nbSimulationStep; i++){
	    this.doNextMove();
	    trajectory[i] = this.currentStatus.getCurrentPosition();
	}

	/** Restoring the autopilot */
	this.currentStatus = statusBackup;
	this.currentDestination = destBackup;

	return trajectory;
    }

    /**
     * Returns the last point in the trajectory going from the current
     * position to the given destination in nbSimulation Steps
     * @param initStatus the status (position, heading ...) to start
     * @param destination the destination
     * @param nbSimulationStep the number of simulation steps
     * @return the last position
     */
    public Position getLastPointOnTrajectory(Status initStatus, 
	    Position destination, int nbSimulationStep) {
	Position[] trajectory = getTrajectorySteps(initStatus, destination, 
		nbSimulationStep);
	return trajectory[trajectory.length-1];
    }

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/


    /**
     * Returns the current destination
     * @return the current destination
     */
    public Destination getCurrentDestination() {
	return currentDestination;
    }

    /**
     * Sets the destination
     * @param newDestination the new destination
     */
    public void setCurrentDestination(Destination newDestination) {
	this.currentDestination = newDestination;
    }

    /**
     * Returns the current status
     * @return the current status
     */
    public Status getCurrentStatus() {
	return currentStatus;
    }

    /**
     * Sets the current status
     * This method should be used exceptionnally 
     * @param currentStatus the current status
     */
    public void setCurrentStatus(Status currentStatus) {
	this.currentStatus = currentStatus;
    }

    public void setSeed(long seed){
	this.rand.setSeed(seed);
    }

}
