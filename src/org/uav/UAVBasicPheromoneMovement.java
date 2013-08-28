/**
 * 
 */
package org.uav;


import java.util.ArrayList;
import java.util.Collections;

import org.uav.aco.AbstractACO;

import org.uav.autopilot.AbstractAutopilot;
import org.uav.autopilot.AutopilotFixedWingOutOfSimulationAreaStrategy;

import org.uav.status.Position;

/**
 * @author Julien Schleich
 * This is a very basic implementation of a pheromone choice for the 
 * movement of UAVs. It checks three destination : left, front and right
 * and selects the one with the biggest attractiveness.
 */
public class UAVBasicPheromoneMovement extends AbstractUAV {
    /** The Ant Colony Optimisation module */
    protected AbstractACO aco;

    /**
     * Constructor
     * @param auto The instance of {@link AbstractAutopilot}
     * @param area the simulation area
     */
    public UAVBasicPheromoneMovement(AbstractAutopilot auto, int id, AbstractACO aco) {
	super(auto, id);
	this.aco = aco;
    }

    /**
     * A simplistic pheromone choice 
     */
    public void newPheromoneChoice(){
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = 
		(AutopilotFixedWingOutOfSimulationAreaStrategy) (this.getAuto());
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
	case 6: 
	    if (rand.nextBoolean()){
		this.updateDestination(left);
	    }
	    else {
		this.updateDestination(right);
	    }
	    break; // left = right
	case 7: 
	    int intrand = rand.nextInt(3);
	    if (intrand == 0){
		this.updateDestination(front);
	    }
	    if (intrand == 1){
		this.updateDestination(left);
	    }
	    if (intrand == 2){
		this.updateDestination(right);
	    }
	    System.out.println(intrand);
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

    @Override
    protected void localActionAfterMove() {}

    /**
     * The pheromone choice is taken at the decision frequency
     */
    @Override
    protected void localActionBeforeMove() {
	//if (getTime() == getDecisionFrequency())
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
