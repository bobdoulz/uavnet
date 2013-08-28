package org.uav;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Iterator;


import org.graphstream.graph.Node;
import org.uav.aco.AbstractACO;
import org.uav.aco.DistributedACO;
import org.uav.autopilot.AbstractAutopilot;
import org.uav.autopilot.AutopilotFixedWingOutOfSimulationAreaStrategy;
import org.uav.status.Position;

/**
 * Under work
 * @author Julien Schleich
 *
 */
public class UAVDistributedPheromoneMovement 
extends AbstractUAVNeighborhood {

    protected AbstractACO aco;

    /** For exchanging pheromones informations */
    protected int exchangeFrequency;
    protected int exchangeCounter;
    protected int informationRadius; 

    /**
     * Constructor
     * @param auto
     * @param id
     * @param aco
     */
    public UAVDistributedPheromoneMovement(AbstractAutopilot auto, int id, AbstractACO aco) {
	super(auto, id, uavGraph);
	this.aco = aco;
    }

    /**
     * Constructor
     * @param auto
     * @param id
     * @param aco
     * @param exchangeFrequency
     * @param informationRadius
     */
    public UAVDistributedPheromoneMovement(AbstractAutopilot auto, int id,
	    AbstractACO aco, int exchangeFrequency, int informationRadius) {
	super(auto, id, uavGraph);
	this.aco = aco;
	this.exchangeFrequency = exchangeFrequency;
	this.informationRadius = informationRadius;
	this.exchangeCounter = 0;
    }

    @Override
    protected void localActionAfterMove() {

    }


    /** 
     * In the future this should be managed by the org.uav.wireless package
     * */
    protected void broadcastInformation(){
	DistributedACO distrACO = (DistributedACO)this.aco;
	String phNameFrom = "ph"+Integer.toString(this.uavId);

	Iterator<Node> itNeighbors = this.getLocalGraph(1).getNodeIterator();
	while (itNeighbors.hasNext()){
	    Node curNeighbor = itNeighbors.next();

	    String phNameTo = "ph"+curNeighbor.getId();
	    System.out.println(phNameTo);
	    distrACO.mergePheromones(	phNameFrom, 
		    phNameTo, 
		    this.auto.getCurrentStatus().getCurrentPosition(), 
		    informationRadius);
	}
    }

    @Override
    protected void localActionBeforeMove() {
	exchangeCounter ++;
	if (getTime() == exchangeFrequency){
	    broadcastInformation();
	    exchangeCounter = 0;
	}

	if (getTime() == getDecisionFrequency())
	    newPheromoneChoice();

    }

    /**
     * A simplistic pheromone choice 
     */
    public void newPheromoneChoice(){
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = (AutopilotFixedWingOutOfSimulationAreaStrategy) (this.getAuto());
	Position left = auto.getLeftPoint();
	Position right = auto.getRightPoint();
	Position front = auto.getFrontPoint();

	double phLeft = aco.getPheromoneCount(left);
	double phRight = aco.getPheromoneCount(right);
	double phFront = aco.getPheromoneCount(front);

	int retValue = smallerPheromone(phLeft, phRight, phFront);
	switch (retValue) {
	case 1: this.updateDestination(front);break; // front
	case 2: this.updateDestination(right);break; // right
	case 3: 
	    if (rand.nextBoolean()){
		this.updateDestination(right);
	    }
	    else {
		this.updateDestination(front);
	    }
	    break; // right = front
	case 4: this.updateDestination(left);break; // left
	case 5: 
	    if (rand.nextBoolean()){
		this.updateDestination(left);
	    }
	    else {
		this.updateDestination(front);
	    }
	    break; // left = front
	case 6: this.updateDestination(left);break; // left = right
	case 7: 
	    int intrand = rand.nextInt(3);
	    if (intrand == 0){
		this.updateDestination(left);
	    }
	    if (intrand == 1){
		this.updateDestination(right);
	    }
	    if (intrand == 2){
		this.updateDestination(front);
	    }
	    break; // left = right = front
	}
    }


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
