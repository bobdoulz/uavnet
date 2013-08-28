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
 * A simple implementation of an autopilot for fixed wing aircrafts 
 */
public class AutopilotFixedWing extends AbstractAutopilot {

    /**
     * Constructor
     * @param currentDestination an instance of {@link Destination}
     * @param currentStatus an instance of {@link Status}
     * @param area an instance of {@link AbstractSimulationArea}
     */
    public AutopilotFixedWing(
	    Destination currentDestination,
	    Status currentStatus,
	    AbstractSimulationArea area) {
	super(currentStatus, currentDestination, area);
    }

    /*********************************************
     * 
     * Implementation of the abstract methods
     * of the abstract {@link Autopilot} class
     * 
     ********************************************/

    /**
     * Basic implementation of the heading change through time
     * The method computes the required angle for the UAV to go to the 
     * current destination
     */
    protected double getUpdatedHeading(){
	// Heading directly to the right direction, no turn rate effect	
	double yDiff = currentDestination.getCurrentDestination().getY() - 
		currentStatus.getCurrentPosition().getY();
	double xDiff = currentDestination.getCurrentDestination().getX() - 
		currentStatus.getCurrentPosition().getX();

	double newHeading = currentStatus.getCurrentHeading().getHeadingValue();

	if ( (xDiff == 0) && (yDiff == 0)){
	    // The UAV is exactly on the destination
	}
	else
	    // Taking care of vertical issues (division by 0)
	    if (xDiff == 0)
		if (yDiff > 0)
		    newHeading = Math.PI/2.0;
		else
		    newHeading = 3*Math.PI/2.0;
	    else 
		if (yDiff == 0)
		    if (xDiff > 0)
			newHeading = 0;
		    else	
			newHeading = Math.PI;
		else {

		    double tanHeading = Math.abs(yDiff / xDiff);

		    if ((xDiff > 0) && (yDiff > 0))
			newHeading = Math.atan(tanHeading);
		    if ((xDiff < 0) && (yDiff > 0))
			newHeading = Math.PI - Math.atan(tanHeading);
		    if ((xDiff > 0) && (yDiff < 0))
			newHeading = 2*Math.PI - Math.atan(tanHeading);
		    if ((xDiff < 0) && (yDiff < 0))
			newHeading = Math.atan(tanHeading)+Math.PI;
		}
	return newHeading;
    }

    /**
     * Turning rate is not taken into account in this basic implementation
     */
    protected int getUpdatedTurnRate(){
	return 0;
    }

    /**
     * In this implementation, the speed is set to the maximum value
     */
    protected double getUpdatedSpeed(){
	// Augment speed to maximum
	return Double.MAX_VALUE;

    }

    /**
     * In this implementation, the new position is calculated based on
     * the current heading, the initial position and the speed value
     */
    protected Position getUpdatedPosition(){
	return nextPosition();
    }


    /*********************************************
     * 
     * Sub-routines to computes points on a
     * feasible trajectory
     * 
     ********************************************/

    /**
     * Returns the minimum turning radius, i.e.,
     * when the UAV turns at its maximum 
     * @return the min turning radius
     */
    public double getTurningRadius(){
	return 
		getCurrentStatus().getCurrentSpeed().getMaxSpeed() / 
		getCurrentStatus().getCurrentHeading().getMaxHeadingChange();
    }

    /**
     * This method provides the coordinate of a point at a given distance
     * and angle from the UAV
     * @param angle the relative angle of the point
     * @param distance the distance from the uav
     * @return an instance of {@link Position}
     */
    public Position getPointWithAngleAndDistance(double angle, double distance){
	/// Computing point
	double moveX = Math.cos((getCurrentStatus().getHeadingValue() + 
		(angle)) % (2*Math.PI)) * distance;
	double moveY = Math.sin((getCurrentStatus().getHeadingValue() + 
		(angle)) % (2*Math.PI)) * distance;

	double pointX = getCurrentStatus().getPositionX() + moveX;
	double pointY = getCurrentStatus().getPositionY() + moveY;

	return new Position (pointX, pointY);
    }

    /**
     * This method provides the coordinate of a point
     * by considering the turning radius
     * @param angle the relative angle of the point
     * @return an instance of {@link Position}
     */
    public Position getPointWithAngle(double angle){
	/// Computing turning radius
	double turningRadius = getTurningRadius();

	/// Computing point
	double moveX = Math.cos((getCurrentStatus().getHeadingValue() + 
		(angle)) % (2*Math.PI)) * turningRadius;
	double moveY = Math.sin((getCurrentStatus().getHeadingValue() + 
		(angle)) % (2*Math.PI)) * turningRadius;

	double pointX = getCurrentStatus().getPositionX() + moveX;
	double pointY = getCurrentStatus().getPositionY() + moveY;

	return new Position (pointX, pointY);
    }

    /**
     * Return the point the more on the left in the UAV trajectory
     * @return an instance of {@link Position}
     */
    public Position getLeftPoint(){
	return getPointWithAngle(Math.PI/4);
    }

    /**
     * Return the point the more on the right in the UAV trajectory
     * @return an instance of {@link Position}
     */
    public Position getRightPoint(){
	return getPointWithAngle(-Math.PI/4);
    }

    /**
     * Return the point in front of the UAV
     * @return an instance of {@link Position}
     */
    public Position getFrontPoint(){
	return getPointWithAngle(0);
    }

    /**
     * Returns the next position if the heading / speed do not change
     * @return the possible future position
     */
    public Position nextPosition(){
	/// Computing point
	double moveX = Math.cos(getCurrentStatus().getHeadingValue()) * 
		getCurrentStatus().getSpeedValue();
	double moveY = Math.sin(getCurrentStatus().getHeadingValue()) * 
		getCurrentStatus().getSpeedValue();

	double pointX = getCurrentStatus().getPositionX() + moveX;
	double pointY = getCurrentStatus().getPositionY() + moveY;

	return new Position (pointX, pointY);
    }

    @Override
    public Position newRandomDestination(){
	RectangularSimulationArea rec = (RectangularSimulationArea)area;
	Position p = new Position(
		rand.nextDouble()*rec.getMaxX(), 
		rand.nextDouble()*rec.getMaxY());
	return p;
    }


}
