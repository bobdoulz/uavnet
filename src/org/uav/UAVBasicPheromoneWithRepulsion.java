package org.uav;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashSet;

import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Node;

import org.uav.aco.AbstractACO;

import org.uav.autopilot.AbstractAutopilot;
import org.uav.autopilot.AutopilotFixedWingOutOfSimulationAreaStrategy;

import org.uav.graph.UAVGraph;

import org.uav.status.Position;

/**
 * @author Julien Schleich
 * This class implements a pheromone choice for the movement of UAVs. 
 * Additionally, it prevents UAV to come to close to each other with a 
 * repulsion mecanism.
 */
public class UAVBasicPheromoneWithRepulsion 
extends AbstractUAVCentralisedNeighbourhood{

    /** The Ant Colony Optimisation module */
    protected AbstractACO aco;
  
    public UAVBasicPheromoneWithRepulsion(AbstractAutopilot auto, int id,
	    UAVGraph uavGraph,AbstractACO aco) {
	super(auto, id, uavGraph);
	this.aco=aco;
    }

    /**
     * Computation of the future destination based on the repulsion mechanism.
     * All sufficiently close neighbours are considered but closer one will
     * influence more the future destination. 
     * @return the combination of the resulting repulsion forces
     */
    public Position repulsion(){
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = 
		(AutopilotFixedWingOutOfSimulationAreaStrategy) (this.getAuto());
	Position currentpos = auto.getCurrentStatus().getCurrentPosition();

	/**
	 *  The maximum distance at which the neighbors
	 *  can have an influence on the destination
	 *  Max is 400 (wifi max distance)
	 */
	double maxConsideredDistance = 200;

	/**
	 *  Compute the position deltas for each one-hop neighbor
	 */
	HashSet<AbstractUAV> neighbors = getNeighbors(1);
	ArrayList<Position> delta = new ArrayList<Position>();
	for(AbstractUAV curNeighbor : neighbors){
	    if (curNeighbor.uavId != uavId){
		Position curNeighborPosition = curNeighbor.auto.getCurrentStatus().getCurrentPosition();
		double distance = curNeighborPosition.euclideanDistance(currentpos);
		double repulsionForce = (maxConsideredDistance - distance) / maxConsideredDistance;

		if (distance <= maxConsideredDistance)
		{
		    /**
		     * We compute a normalized point at a distance 1 of the UAV 
		     * with the same direction as the current neighbor.
		     * The strength of the repulsion is then used to multiply the force.
		     */
		    double normalizeX = ((curNeighborPosition.getX() + 
			    ((distance - 1)* currentpos.getX())) / distance);
		    double normalizeY = ((curNeighborPosition.getY() + 
			    ((distance - 1)* currentpos.getY())) / distance);
		    Position normalizeNeighbor = new Position(normalizeX, normalizeY);
		    
		    delta.add((currentpos.minus(normalizeNeighbor)).times(repulsionForce));
		}
	    }
	}

	/**
	 *  Aggregate the position deltas to obtain a total resulting force
	 */
	Position resulting = new Position(currentpos);
	for (Position p : delta){
	    resulting = resulting.add(p);
	}

	/** Taking car of a potentionally lack of neighbors
	 * The computation is then considered failed and we will
	 * only rely on pheromones
	 */
	Position invalidPosition = new Position(
		Integer.MAX_VALUE,
		Integer.MAX_VALUE,
		Integer.MAX_VALUE);
	if (delta.size() == 0){
	    return invalidPosition;
	}
	else {
	    return resulting;
	}
    }

    /**
     * Computation of the destination only based on pheromone attrictiveness
     * @return the future destination
     */
    public Position pheromone(){
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = 
		(AutopilotFixedWingOutOfSimulationAreaStrategy) (this.getAuto());
	Position currentpos = auto.getCurrentStatus().getCurrentPosition();

	Position bestPheromone = new Position(Integer.MAX_VALUE,
		Integer.MAX_VALUE,
		Integer.MAX_VALUE);

	Position left = auto.getLeftPoint();
	Position right = auto.getRightPoint();
	Position front = auto.getFrontPoint();

	double phLeft = aco.getPheromoneCount(left);
	double phRight = aco.getPheromoneCount(right);
	double phFront = aco.getPheromoneCount(front);

	int retValue = smallerPheromone(phLeft, phRight, phFront);
	switch (retValue) {
	case 1: bestPheromone = front;break; // front
	case 2: bestPheromone = right;break; // right
	case 3: 
	    if (rand.nextBoolean()){
		bestPheromone = right;
	    }
	    else {
		bestPheromone = front;
	    }
	    break; // right = front
	case 4: bestPheromone = left;break; // left
	case 5: 
	    if (rand.nextBoolean()){
		bestPheromone = left;
	    }
	    else {
		bestPheromone = front;
	    }
	    break; // left = front
	case 6: 
	    if (rand.nextBoolean()){
		bestPheromone = left;
	    }
	    else {
		bestPheromone = right;
	    }
	    break; // left = right
	case 7: 
	    int intrand = rand.nextInt(3);
	    if (intrand == 0){
		bestPheromone = front;
	    }
	    if (intrand == 1){
		bestPheromone = left;
	    }
	    if (intrand == 2){
		bestPheromone = right;
	    }
	    break; // left = right = front
	}

	return bestPheromone;
    }

    /**
     * The final choice of destination. Here, if we have too close neighbours
     * we choose repulsion(). Otherwise it is regular pheromone() choice.
     */
    public void newPheromoneChoice(){ 
	//System.out.println("UAV #"+this.uavId);
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = 
		(AutopilotFixedWingOutOfSimulationAreaStrategy) (this.getAuto());
	Position currentpos = auto.getCurrentStatus().getCurrentPosition();
	
	/**
	 * The two positions computed for repulsion and pheromone
	 */
	Position posRepulsion = repulsion();
	Position posPheromone = pheromone();

	Position invalidPosition = new Position( /** The invalid position */
		Integer.MAX_VALUE,
		Integer.MAX_VALUE,
		Integer.MAX_VALUE);

	/**
	 *  Case 1: non valid position for both repulsion
	 */
	if (posRepulsion.isEqualTo(invalidPosition)){
	    this.updateDestination(posPheromone);
	}
	/**
	 *  Case 2: both positions should be mixed somehow
	 */
	else {
	    /** Here we don't mix, we just take repulsion */
	    this.updateDestination(posRepulsion);
	}

    }

    /**
     * A hashset containing the considered neighbours of a UAV, filtered 
     * by a maximum distance nbhop
     * @param nhop the maximum graph distance to the UAV
     * @return the hashset of considerd neighbours
     */
    protected HashSet<AbstractUAV> getNeighbors(int nhop){
	HashSet<AbstractUAV> uavSet = new HashSet<AbstractUAV>();
	Node me = getMyGraphNode();
	BreadthFirstIterator<Node> bfi = new BreadthFirstIterator<Node>(me);
	while (bfi.hasNext()){
	    Node n = bfi.next();
	    if ( (n.getId() != "uav"+this.getUavId()) && (n.getId() != "base")){ /** If not me */
		if (bfi.getDepthOf(n) <= nhop){ /** If n-hop neighbor */
		    UAVBasicPheromoneWithRepulsion u = 
			    (UAVBasicPheromoneWithRepulsion)n.getAttribute("uavinstance");
		    uavSet.add(u);
		}
	    }
	}
	return uavSet;	
    }

    /**
     * A simple method returning which of the three destinations has the
     * less pheromone
     * @param left the left destination
     * @param right the right destination
     * @param front the front destination
     * @return the less pheromone destination
     */
    public int smallerPheromone(double left, double right, double front){
	ArrayList<Double> set = new ArrayList<Double>();
	set.add(left);
	set.add(right);
	set.add(front);
	Collections.sort(set);
	double smallest = set.get(0);
	int retValue = 0;
	if (smallest == left){
	    retValue+=4;
	}
	if (smallest == right){
	    retValue+=2;
	}
	if (smallest == front){
	    retValue+=1;
	}
	return retValue;
    }

    @Override
    protected void localActionAfterMove() {}

    /**
     * The pheromone choice is taken at the decision frequency
     */
    @Override
    protected void localActionBeforeMove() {
	newPheromoneChoice();
    }

    /**
     * @return the aco
     */
    public AbstractACO getAco() {
	return aco;
    }

    /**
     * @param aco the aco to set
     */
    public void setAco(AbstractACO aco) {
	this.aco = aco;
    }

}
