package org.uav;

import java.util.Random;

import org.uav.autopilot.AbstractAutopilot;
import org.uav.status.Position;

abstract public class AbstractUAV {
	
	/**
	 * The autopilot
	 * It calculates the movement of the UAV depending on:
	 * 	- The current status (position, heading, speed, turning rate)
	 * 	- The destination 
	 */
	protected AbstractAutopilot auto;
	
	/**
	 * An intern timer
	 * Used not to take decisions at every time steps
	 */
	protected int time;
	
	/**
	 * A frequency to take local decisions every x time steps
	 */
	private static int decisionFrequency = 5;

	/**
	 * The unique identifier of the UAV
	 */
	protected int uavId;
	
	/**
	 * A random variable 
	 */
	protected Random rand;
	
	
	////////////////////////////////////////////////////////////////////////
		
	/**
	 * Constructor of a UAV
	 * @param id a unique identifier 
	 * @param auto the autopilot
	 * @param area the simulation area
	 */
	public AbstractUAV(AbstractAutopilot auto, int id) {
		super();
		this.auto = auto;
		this.time = 1;
		this.uavId = id;
		this.rand = new Random();
	}

	////////////////////////////////////////////////////////////////////////
	
	/**
	 * This is the main method, it: 
	 * 	- Moves the UAV to its new position
	 * 	- Refreshes its representation on the graph
	 * 	- Allows the UAV to take its own local decisions
	 */
	public void doSimulationStep(){
		/**
		 * Local decision method before moving the UAV
		 */
		localActionBeforeMove();
		
		/**
		 * Summons the autopilot to do the next move
		 */
		this.getAuto().doNextMove();
		
		/**
		 * Local decision method, generally used to locally compute next destination
		 */
		localActionAfterMove();
		
		/**
		 * UAV internal timer update
		 */
		if (getTime() == getDecisionFrequency()){
			setTime(0);
		}
		this.time++;
	}
	
	/**
	 * The action of the local decision after the movement
	 */
	abstract protected void localActionAfterMove();
	
	/**
	 * The action of the local decision after the movement
	 */
	abstract protected void localActionBeforeMove();

	/*********************************************
	 * 
	 * Destination-related methods
	 * 
	 ********************************************/
	
	/**
	 * Update the current destination in the autopilot
	 * @param x the x-axis coordinate of the destination
	 * @param y the y-axis coordinate of the destination
	 */
	public void updateDestination(double x, double y){
		auto.getCurrentDestination().updateDestination(x, y);
	}
	
	/**
	 * Update the current destination of the autopilot
	 * @param p an instance of {@link Destination} 
	 */
	public void updateDestination(Position p){
		auto.getCurrentDestination().updateDestination(p);
	}
	
	/*********************************************
	 * 
	 * Getters / Setters 
	 * 
	 ********************************************/
	
	/**
	 * Return the autopilot
	 * @return the instance of {@link AbstractAutopilot} of the UAV
	 */
	public AbstractAutopilot getAuto() {
		return auto;
	}

	/**
	 * Sets the autopilot
	 * @param auto an instance of {@link AbstractAutopilot}
	 */
	public void setAuto(AbstractAutopilot auto) {
		this.auto = auto;
	}

	/**
	 * Returns the current timer value
	 * @return the intern timer value
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Sets the current timer value
	 * @param time a timer value
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return the decisionFrequency
	 */
	public static int getDecisionFrequency() {
		return decisionFrequency;
	}

	/**
	 * @param decisionFrequency the decisionFrequency to set
	 */
	public static void setDecisionFrequency(int decisionFrequency) {
		AbstractUAV.decisionFrequency = decisionFrequency;
	}

	/**
	 * @return the uavId
	 */
	public int getUavId() {
		return uavId;
	}

	/**
	 * @param uavId the uavId to set
	 */
	public void setUavId(int uavId) {
		this.uavId = uavId;
	}
	
	public void setSeed(long seed){
		this.rand.setSeed(seed*uavId);
		this.auto.setSeed(seed*uavId);
	}
}

