/**
 * 
 */
package org.uav.autopilot;

import org.uav.autopilot.Destination;
import org.uav.area.AbstractSimulationArea;
import org.uav.area.RectangularSimulationArea;
import org.uav.status.Position;
import org.uav.status.Status;

/**
 * @author Julien Schleich
 * An specialization of the fixed wing autopilot with 
 * a strategy to avoid going outside of the simulation
 * area 
 */
public class AutopilotFixedWingOutOfSimulationAreaStrategy extends
		AutopilotFixedWing {

	/**
	 * A simple boolean variable to state if a node is currently
	 * applying an out of simulation area strategy
	 */
	protected boolean isCurrentlyInOutOfSimulationAreaStrategy;
	
	/**
	 * Constructor
	 * @param currentDestination
	 * @param currentStatus
	 * @param area
	 */
	public AutopilotFixedWingOutOfSimulationAreaStrategy(
			Destination currentDestination, 
			Status currentStatus,
			AbstractSimulationArea area) {
		super(currentDestination, currentStatus, area);
	}
	
	/**
	 * We override the main {@link AbstractAutopilot} method
	 * by checking the trajectory before anything else
	 */
	@Override
	public void doNextMove(){
		simpleOutOfAreaStrategy();
		updateSpeed();
		updateTurnRate();
		updateHeading();
		updatePosition();	
	}
	
	/**
	 * A simple check method to determines if the coordinates are
	 * outside the simulation area. Only works for rectangular shape areas
	 * @param x the x-axis coordinate
	 * @param y the y-axis coordinate
	 * @return true if (x,y) is out, false otherwise
	 */
	public boolean isOutOfSimulationArea(double x, double y){
		RectangularSimulationArea rec = (RectangularSimulationArea)area;
		return (x<0) || (x>=rec.getMaxX()) || (y<0) || (y>=rec.getMaxY());
	}
	
	/**
	 * A simple check method to determines if the coordinates are
	 * outside the simulation area. Only works for rectangular shape areas
	 * @param p an instance of {@link Position}
	 * @return true if p is out, false otherwise
	 */
	public boolean isOutOfSimulationArea(Position p){
		RectangularSimulationArea rec = (RectangularSimulationArea)area;
		return (p.getX()<0) || (p.getX()>=rec.getMaxX()) || (p.getY()<0) 
			|| (p.getY()>=rec.getMaxY());
	}
	
	/**
	 * This method detects if the UAV is going to be out of the
	 * simulation area soon, by checking if the front point is 
	 * in the simulation area
	 * @return true if the UAV will soon be out
	 */
	private boolean outOfSimulationAreaTrajectory(){
		return isOutOfSimulationArea(getFrontPoint());
	}
	
	/**
	 * Checks if the left point is in the simulation area
	 * @return true if the left point is in the simulation area
	 */
	private boolean canWeTurnLeft(){
		return !isOutOfSimulationArea(getLeftPoint());	
	}
	
	/**
	 * This simple strategy checks if left point is in the 
	 * simulation area, if yes, we turn left, otherwise 
	 * the UAV goes on the right.
	 * Note that this simple strategy limits time out of simulation 
	 * area but does not prevent it
	 * @return true if the strategy is used, false otherwise
	 */
	protected boolean simpleOutOfAreaStrategy(){
		boolean out = outOfSimulationAreaTrajectory();
		if (out){
			isCurrentlyInOutOfSimulationAreaStrategy = true;
			if (canWeTurnLeft())
				this.setCurrentDestination(
					this.getPointWithAngle(Math.PI/2));
			else 
				this.setCurrentDestination(
					this.getPointWithAngle(-Math.PI/2));
		}
		else
			isCurrentlyInOutOfSimulationAreaStrategy = false;
		return out;
	}
	
	/**
	 * @return the isCurrentlyInOutOfSimulationAreaStrategy
	 */
	public boolean isCurrentlyInOutOfSimulationAreaStrategy() {
		return isCurrentlyInOutOfSimulationAreaStrategy;
	}

	/**
	 * @param isCurrentlyInOutOfSimulationAreaStrategy the 
	 * isCurrentlyInOutOfSimulationAreaStrategy to set
	 */
	public void setCurrentlyInOutOfSimulationAreaStrategy(
			boolean isCurrentlyInOutOfSimulationAreaStrategy) {
		this.isCurrentlyInOutOfSimulationAreaStrategy = 
			isCurrentlyInOutOfSimulationAreaStrategy;
	}



}
