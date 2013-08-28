/**
 * 
 */
package org.uav;

import java.util.HashMap;
import java.util.HashSet;

import java.util.Map.Entry;

import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Node;
import org.uav.aco.AbstractACO;

import org.uav.autopilot.AbstractAutopilot;
import org.uav.graph.UAVGraph;
import org.uav.status.Position;


/**
 * @author Julien Schleich
 * Implementation of a simple distributed behavior to cover an area
 * while the communication graph remain connected  
 */
public class UAVBasicConnectedCoverage extends AbstractUAVConnectedCoverage {

    /** The current number of hop to the base station */
    protected int nbHopToBase;
    /** The base station */
    protected Node base;
    /** The Ant Colony Optimisation module */
    protected AbstractACO aco;

    /**
     * Constructor
     * @param auto the autopilot
     * @param id the identifier of the UAV
     * @param uavGraph the instance of graph containing the UAVs
     */
    public UAVBasicConnectedCoverage(	AbstractAutopilot auto, 
	    int id, 
	    UAVGraph uavGraph, 
	    Node base,
	    AbstractACO aco,
	    int orientationGranularity,
	    int nbStepsInFuture) {
	super(auto, id, uavGraph, orientationGranularity, nbStepsInFuture);
	this.nbHopToBase = 0;
	this.base = base;
	this.aco = aco;
    }

    /**
     * A small method to determine which UAV is "smaller" by
     * considering first their distance to the base station
     * and if useful their id
     * @param n1 the first uav
     * @param n2 the second uav
     * @return true if n1 < n2, false otherwise
     */
    protected boolean smallerThan(UAVBasicConnectedCoverage n1, 
	    UAVBasicConnectedCoverage n2){
	return 	(n1.getNbHopToBase() < n2.getNbHopToBase()) || 
		( (n1.getNbHopToBase() == n2.getNbHopToBase()) && 
			(n1.getUavId() < n2.getUavId()) );
    }

    /**
     *  A method to update the current nbHopToBase variable
     */
    protected void updateNbHopToBase(){
	Node me = getMyGraphNode();
	BreadthFirstIterator<Node> bfi = new BreadthFirstIterator<Node>(me);
	int smallestNbHop = Integer.MAX_VALUE;
	while (bfi.hasNext()){
	    Node n = bfi.next();
	    if (n.getId() != "uav"+this.getUavId()) { /** If not me */
		if (bfi.getDepthOf(n) <= 1){ /** If one-hop neighbor */
		    if (n.getId() == "base"){
			smallestNbHop = 0;
			break;
		    }
		    UAVBasicConnectedCoverage u = 
			    (UAVBasicConnectedCoverage)n.getAttribute("uavinstance");
		    if (smallerThan(u, this)){
			if (u.nbHopToBase < smallestNbHop)
			    smallestNbHop = u.nbHopToBase;
		    }
		}
	    }
	}
	if (smallestNbHop == Integer.MAX_VALUE)
	    this.nbHopToBase = Integer.MAX_VALUE;
	else
	    this.nbHopToBase = smallestNbHop + 1;
    }

    /**
     * Returns the list of considered neighbors, in this case
     * only the "lower" neighbors
     * @return the list of lower neighbors
     */	
    protected HashSet<AbstractUAV> getLowerNeighbors(){
	HashSet<AbstractUAV> uavSet = new HashSet<AbstractUAV>();
	Node me = getMyGraphNode();
	BreadthFirstIterator<Node> bfi = new BreadthFirstIterator<Node>(me);
	int smallestNbHop = Integer.MAX_VALUE;
	while (bfi.hasNext()){
	    Node n = bfi.next();
	    { /** If not me */
		if ( (n.getId() != "uav"+this.getUavId()) && (n.getId() != "base"))
		    if (bfi.getDepthOf(n) <= 1){ /** If one-hop neighbor */
			UAVBasicConnectedCoverage u = 
				(UAVBasicConnectedCoverage)n.getAttribute("uavinstance");
			if (smallerThan(u, this)){
			    uavSet.add(u);
			    if (u.nbHopToBase < smallestNbHop)
				smallestNbHop = u.nbHopToBase;
			}
		    }
	    }
	}
	return uavSet;	
    }

    /**
     * What to do when no connected solutions are found 
     */
    @Override
    void noConnectedSolutionAction() {
	this.auto.setCurrentDestination(
		bestFuturePositionNoConnectedPosition());
    }

    /**
     * The best future position when no connected solution exist. Here
     * we select the base station as a backup plan
     * @return the base station position
     */
    protected Position bestFuturePositionNoConnectedPosition(){
	/** Find the current best spot around base */
	Position p = new Position((Double)base.getAttribute("x"), 
		(Double)base.getAttribute("y"));
	return p;
    }

    /**
     * The best future connected position is arbitrarily chosen here
     * as the lowest pheromone connected position. The goal is to maximise
     * exploration while maintaining connectivity.  
     * @return the lowest pheromone connected position
     */
    protected Position bestFuturePositionConnectedPosition(){
	double bestPhCount = Double.MAX_VALUE;
	Position bestPosition = new Position(-1,-1);
	for(Entry<Double, HashMap<Integer, Double>> entry : this.connectivity.entrySet()) {
	    Double angle = entry.getKey();
	    Position p = this.solutions.get(angle);
	    Double phCount = (double) this.aco.getPheromoneCount(p);
	    if (phCount < bestPhCount){
		bestPhCount = phCount;
		bestPosition = p;
	    }
	}
	return bestPosition;
    }

    /**
     * Returns the best future connected position. If no connected solutions 
     * are found, it will be the base station position.
     */
    @Override
    protected Position getBestConnectedFuturePosition() {
	/** If there is no connected position in the future*/
	if (this.connectivity.isEmpty()){
	    return bestFuturePositionNoConnectedPosition();
	}
	else {
	    return bestFuturePositionConnectedPosition();
	}
    }

    /**
     * @param aco the aco to set
     */
    public void setAco(AbstractACO aco) {
	this.aco = aco;
    }

    @Override
    protected HashSet<AbstractUAV> getConsideredNeighbors(){
	return getLowerNeighbors();
    }


    @Override
    protected void localActionAfterMove() {
	updateNbHopToBase();
    }

    /**
     * @return the nbHopToBase
     */
    public int getNbHopToBase() {
	return nbHopToBase;
    }

    /**
     * @param nbHopToBase the nbHopToBase to set
     */
    public void setNbHopToBase(int nbHopToBase) {
	this.nbHopToBase = nbHopToBase;
    }


}





