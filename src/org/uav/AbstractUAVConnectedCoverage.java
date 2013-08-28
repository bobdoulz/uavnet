package org.uav;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Node;
import org.uav.autopilot.AbstractAutopilot;
import org.uav.autopilot.AutopilotFixedWingOutOfSimulationAreaStrategy;
import org.uav.graph.UAVGraph;
import org.uav.status.Position;
import org.uav.wireless.HomogeneousPropagationModel;

/**
 * @author Julien Schleich
 * This abstract class provides additional methods for a UAV to deal
 * with its local neighborhood in order to remain connected to the UAV
 * network throughout the simulation. 
 * It is based on the estimation of the position of its neighbours 
 * and by trying various directions and see if the UAV will remain
 * connected in a near future.
 */
abstract public class AbstractUAVConnectedCoverage 
extends AbstractUAVCentralisedNeighbourhood  {

    /** The considered number of simulation step in the future */
    private int nbTimestepsInTheFuture;

    /** The considered granularity for orientation exploration (in degrees) 
     * e.g. if 10 then 360/10 = 36 different directions will be tested */
    private int orientationGranularity;

    /** For each angle, we store the couples (id_uav, distance) */
    protected HashMap< Double, HashMap< Integer, Double > > connectivity;

    /** For each angle, we store the future position */
    protected HashMap< Double, Position > solutions;

    public AbstractUAVConnectedCoverage(AbstractAutopilot auto, 
	    int id, 
	    UAVGraph uavGraph, 
	    int orientationGranularity, 
	    int nbTimestepsInTheFuture) {
	super(auto, id, uavGraph);
	this.connectivity = new HashMap< Double, HashMap< Integer, Double > >();
	this.solutions = new HashMap< Double, Position >();
	this.orientationGranularity = orientationGranularity;
	this.nbTimestepsInTheFuture = nbTimestepsInTheFuture;

    }

    /**
     * Main method
     */
    protected void connectAndExplore() throws NoConnectedSolutionException {
	this.connectivity.clear();
	this.solutions.clear();

	computeConnectedFuturePosition();
	if (this.connectivity.isEmpty()){
	    //System.out.println("UAV "+this.getUavId()+" has no solution");
	    throw new NoConnectedSolutionException();
	}
	Position futureDestination = getBestConnectedFuturePosition();
	this.auto.setCurrentDestination(futureDestination);
    }

    /***********************************************************
     * 
     * Connectivity-related methods
     * 
     ***********************************************************/

    /** 
     * This method deals with generating valid future solutions in terms 
     * of connectivity. 
     * 
     * This basic implementation does:
     * For each orientation (based on orientationGranularity):
     * 1. It computes where the UAV would be in nbTimestepsInTheFuture by 
     * using the autopilot
     * 2. Computes the future positions of the considered neighbors
     * 3. Updates the information in an instance of {@link ConnectedSolutions} 
     * Obviously, it can be overridden in sub-classes if needed
     */
    protected void computeConnectedFuturePosition() {

	/** We compute an unreachable distance for the destination */
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = (AutopilotFixedWingOutOfSimulationAreaStrategy)this.getAuto();
	double distance = nbTimestepsInTheFuture * auto.getCurrentStatus().getCurrentSpeed().getMaxSpeed();

	/** We compute the positions of the considered neighbors */
	HashMap<Integer, Position> neighborsPositionInFuture = getNeighborsPositionInFuture(getConsideredNeighbors());
	Position curDestination;
	/** For each orientation angles */
	for (int angle = 0; angle < 360; angle+=orientationGranularity){

	    /** We compute the new heading considering our current heading and the orientation angle */
	    double newHeading = (this.getAuto().getCurrentStatus().getHeadingValue() 
		    + ((angle * 2 * Math.PI)/ 360) ) % (2 * Math.PI);
	    //System.out.println(newHeading);
	    /** We ask the autopilot to compute where we would be in nbSimulationSteps with this new heading */
	    curDestination = auto.getPointWithAngleAndDistance(newHeading, distance);
	    Position lastPointOnTrajectory = auto.getLastPointOnTrajectory(curDestination, nbTimestepsInTheFuture);
	    //System.out.println(lastPointOnTrajectory.getX()+" "+lastPointOnTrajectory.getY());
	    this.solutions.put((double) angle, lastPointOnTrajectory);

	    /** We store the distances between our future self, and the future neighbors */
	    HashMap<Integer, Double> distances = new HashMap<Integer, Double>();

	    /** We now compute the distance from this point to each future destination */
	    for(Entry<Integer, Position> entry : neighborsPositionInFuture.entrySet()) {
		Integer uavId = entry.getKey();
		Position pos = entry.getValue();
		/** If distance is too big for wireless reception, the value is Double.MAX_VALUE */
		if (HomogeneousPropagationModel.areCloseEnoughToDecode(pos, lastPointOnTrajectory) ){
		    distances.put(uavId, HomogeneousPropagationModel.getDistance(pos, lastPointOnTrajectory));
		}
	    }
	    /** We only store valid solutions, i.e. there should at least be a future connected neighbor */
	    if (!distances.isEmpty()){
		this.connectivity.put((double)angle, distances);
	    }
	}
	//System.out.println("*** NbSolution: "+this.connectivity.size());

    }

    /**
     * This method returns the considered neighbors for the connectivity checking.
     * @return the set of considered neighbors
     */
    abstract protected HashSet<AbstractUAV> getConsideredNeighbors();

    /**
     * This method provides the future positions of the considered neighbors
     * based on their current destination and the considered number of simulation
     * steps. The autopilot is used to compute their trajectory.
     * @param neighbors the considered set of neighbors
     * @param nbSimulationSteps the number of simulation steps in the future
     * @return the positions of the UAVs in the future
     */
    protected HashMap<Integer, Position> getNeighborsPositionInFuture(
	    HashSet<AbstractUAV> neighbors, int nbSimulationSteps){

	HashMap<Integer, Position> neighborsPositionInFuture = new HashMap<Integer, Position>();
	for (AbstractUAV uav : neighbors){
	    neighborsPositionInFuture.put(uav.getUavId(), 
		    uav.getAuto().getLastPointOnTrajectory(
			    uav.getAuto().getCurrentDestination().getCurrentDestination(), 
			    nbSimulationSteps));
	}
	return neighborsPositionInFuture;
    }

    /**
     * This method provides the future positions of the considered neighbors
     * based on their current destination and the considered number of simulation
     * steps. The autopilot is used to compute their trajectory.
     * This version uses nbTimestepsInTheFuture 
     * @param neighbors the considered set of neighbors
     * @return the positions of the UAVs in the future
     */
    protected HashMap<Integer, Position> getNeighborsPositionInFuture(HashSet<AbstractUAV> neighbors){
	HashMap<Integer, Position> neighborsPositionInFuture = new HashMap<Integer, Position>();
	for (AbstractUAV uav : neighbors){
	    neighborsPositionInFuture.put(uav.getUavId(), 
		    uav.getAuto().getLastPointOnTrajectory(
			    uav.getAuto().getCurrentDestination().getCurrentDestination(), 
			    this.nbTimestepsInTheFuture));
	}
	return neighborsPositionInFuture;
    }

    /***********************************************************
     * 
     * Solution comparison related methods
     * 
     ***********************************************************/

    /**
     * This method compares all the valid solution and return
     * the position of the best one
     * @return the position of the best solution
     */
    abstract protected Position getBestConnectedFuturePosition();



    /***********************************************************
     * 
     * Action related method
     * 
     ***********************************************************/
    /**
     * Compute next destination before moving
     */
    @Override
    protected void localActionBeforeMove() {
	if (getTime() == getDecisionFrequency()){
	    try{
		connectAndExplore();
	    }
	    catch(NoConnectedSolutionException e){
		noConnectedSolutionAction();
	    }
	}
    }

    /**
     * What to do when no connected solution exist
     */
    abstract void noConnectedSolutionAction();
}
