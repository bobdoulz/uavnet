package org.uav;

import java.util.ArrayList;
import java.util.HashSet;

import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Node;

import org.uav.aco.AbstractACO;

import org.uav.autopilot.AbstractAutopilot;
import org.uav.autopilot.AutopilotFixedWingOutOfSimulationAreaStrategy;

import org.uav.graph.UAVGraph;

import org.uav.status.Position;

public class UAVRandomMovementWithRepulsion 
extends AbstractUAVCentralisedNeighbourhood{

    /** The Ant Colony Optimisation module */
    protected AbstractACO aco;

    public UAVRandomMovementWithRepulsion(AbstractAutopilot auto, int id,
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
	 *  Max is 400 as the maximum wifi distance
	 */
	double maxConsideredDistance = 200;

	/**
	 *  Compute the position deltas for each one-hop neighbor
	 */
	HashSet<AbstractUAV> neighbors = getNeighbors(1);
	ArrayList<Position> delta = new ArrayList<Position>();
	for(AbstractUAV curNeighbor : neighbors){
	    if (curNeighbor.uavId != uavId){
		Position curNeighborPosition = 
			curNeighbor.auto.getCurrentStatus().getCurrentPosition();
		double distance = curNeighborPosition.euclideanDistance(currentpos);
		double repulsionForce = (maxConsideredDistance - distance) 
			/ maxConsideredDistance;

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
	    //System.out.print("To add: ");p.print();
	    //System.out.print("Result: ");resulting.print();
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
     * A hashset containing the considered neighbours of a UAV, filtered 
     * by a maximum distance nbhop
     * @param nhop the maximum graph distance to the UAV
     * @return the hashset of considerd neighbours
     */
    protected HashSet<AbstractUAV> getNeighbors(int nhop){
	HashSet<AbstractUAV> uavSet = new HashSet<AbstractUAV>();
	Node me = getMyGraphNode();
	BreadthFirstIterator<Node> bfi = new BreadthFirstIterator<Node>(me);
	try {
	    while (bfi.hasNext()){
		Node n = bfi.next();
		 /** If not me */
		if ( (n.getId() != "uav"+this.getUavId()) && (n.getId() != "base")){
		    if (bfi.getDepthOf(n) <= nhop){ /** If n-hop neighbor */
			UAVRandomMovementWithRepulsion u = 
				(UAVRandomMovementWithRepulsion)n.getAttribute("uavinstance");
			uavSet.add(u);
		    }
		}
	    }

	}
	catch (Exception e){
	}
	return uavSet;	
    }

    /**
     * The destination choice based on either a repulsion force if some 
     * neighbours are too close, or a random choice in the other case.
     */
    public void newDestinationChoice(){ 
	/**
	 * The two positions computed for repulsion and pheromone
	 */
	Position posRepulsion = repulsion();
	Position posRandom = newRandomChoice();
	Position invalidPosition = new Position( /** The invalid position */
		Integer.MAX_VALUE,
		Integer.MAX_VALUE,
		Integer.MAX_VALUE);

	/**
	 *  Case 1: non valid position for both repulsion
	 */
	if (posRepulsion.isEqualTo(invalidPosition)){
	    this.updateDestination(posRandom);
	}
	/**
	 *  Case 2: both positions should be mixed somehow
	 */
	else {
	    /**
	     *  Here we don't mix, we just take repulsion
	     */
	    this.updateDestination(posRepulsion);
	}
    }

    /**
     * Basic random choice between the three main destination
     * @return the selected destination
     */
    public Position newRandomChoice(){
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = 
		(AutopilotFixedWingOutOfSimulationAreaStrategy)getAuto();
	double random = rand.nextDouble();
	if (random < 0.2)
	    return auto.getLeftPoint();
	else if ((random >= 0.2) && (random < 0.8))
	    return auto.getFrontPoint();
	else if (random >= 0.8)
	    return auto.getRightPoint();
	else 
	    return auto.getFrontPoint();

    }


    /**
     * The pheromone choice is taken at the decision frequency
     */
    @Override
    protected void localActionBeforeMove() {
	if (getTime() == getDecisionFrequency())
	    newDestinationChoice();
    }

    @Override
    protected void localActionAfterMove() {
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
