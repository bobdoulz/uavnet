package org.uav.aco;

import java.util.ArrayList;

import org.graphstream.graph.Node;

import org.uav.AbstractUAV;

import org.uav.area.RectangularSimulationArea;
import org.uav.area.AbstractSimulationArea;
import org.uav.autopilot.AutopilotFixedWingOutOfSimulationAreaStrategy;

import org.uav.simulation.Simulation;
import org.uav.status.Position;

/**
 * @author Julien Schleich
 * A centralized implementation of a simplistic ACO
 */
public class CentralizedACO extends AbstractACO {

    /** The maximum pheromone count */
    static protected double maxPheromoneValue = 1;

    /** The maximum pheromone count */
    static protected double minPheromoneValue = Double.MIN_VALUE;

    public CentralizedACO(Simulation simu) {
	super(simu);
    }

    /*********************************************
     * 
     * Main pheromone mechanism methods
     * 
     ********************************************/

    /**
     * In this implementation, we impact the four cells closest to the UAV
     */
    @Override
    public void dropPheromone(Position previousPos, 
	    Position currentPos, 
	    String phName, 
	    Integer uavId) {
	RectangularSimulationArea recArea = (RectangularSimulationArea) area;
	int int_x = (int)(currentPos.getX() / recArea.getGranularity());
	int int_y = (int)(currentPos.getY() / recArea.getGranularity());
	dropPheromoneOnCell(int_x, int_y, phName, uavId);
    }

    /**
     * For a given cell, the pheromone level is increased to maximum value
     * as the UAV fly over this position. The signification is that the UAV
     * just scanned this area.
     * @param x the x axis value of the cell
     * @param y the y axis value of the cell
     * @param area the {@link AbstractSimulationArea}
     */
    private void dropPheromoneOnCell(int x, int y, String phName, Integer uavId){
	Node n = area.getG().getNode(x+"-"+y);
	if (n != null){
	    /** Updating pheromone count */
	    Double phValue = 1.0;
	    n.setAttribute(phName, phValue);

	    /** Updating scan time list */
	    if (simu.isUseLastUAVId()){
		Integer lastUAV = (Integer)n.getAttribute("lastUAV");
		if (simu.isUseScanTimes()){
		    ArrayList<Integer> scanTime = 
			    (ArrayList<Integer>)n.getAttribute("scanTime");
		    Integer lastScan = -1;
		    if (!scanTime.isEmpty()){
			lastScan = scanTime.get(scanTime.size()-1);
		    }
		    if ((lastUAV != uavId) || ((simu.getBigCpt() - lastScan) > 1 )){
			scanTime.add(simu.getBigCpt());
			n.setAttribute("scanTime", scanTime);
			n.setAttribute("lastUAV", uavId);
		    }
		}
		else
		    n.setAttribute("lastUAV", uavId);
	    }
	}
    }

    /**
     * In this implementation, the pheromone on each cell
     * is reduced homogeneously
     */
    @Override
    public void evaporation() {
	double stepX = ( area.getMaxX()-area.getMinX() ) / area.getGranularity();
	double stepY = ( area.getMaxY()-area.getMinY() ) / area.getGranularity();

	for (int i=0; i<stepX; i++){
	    for (int j=0; j<stepY; j++){
		Node n = area.getG().getNode(i+"-"+j);
		if (n.hasAttribute("pheromone")){
		    Double phValue = n.getAttribute("pheromone");
		    if (phValue > 0.0){
			phValue -= this.getEvaporationSpeed();
			if (phValue < 0.0)
			    phValue = 0.0;	
		    }
		    else
			phValue -= 1;
		    n.setAttribute("pheromone", phValue);
		}
	    }
	}
    }

    /**
     * Get the pheromone of one cell. The value is either:
     * 	- -1000 if the cell is out of the simulation area
     * 	- +1000 if the cell has never been visited
     * 	- The value of lastScan in the other cases
     * @param x the x-axis coordinate in the simulation area grid
     * @param y the y-axis coordinate in the simulation area grid
     * @return the pheromone value of the grid cell
     */
    protected double getPheromoneCount(int x, int y){
	double phCount = 0.0;
	int cell_x = x/area.getGranularity();
	int cell_y = y/area.getGranularity();

	if (!area.isOutOfArea(x, y)){
	    Node n = area.getG().getNode(cell_x+"-"+cell_y);
	    Double ph = n.getAttribute("pheromone");
	    /** Never visited */
	    if (ph == Integer.MIN_VALUE)
		phCount += -1000.0;
	    else
		/** Still pheromone traces --> visited not too long time ago */
		if (ph > 0)
		    phCount += ph;
	    /** No more pheromone traces --> visited long time ago */
		else
		    phCount += ph;
	}
	else
	    phCount += 1000.0;
	return phCount;
    }

    /**
     * We count the pheromones around a particular position p
     * We pick the 4 closest points
     * @param p the instance of {@link Position}
     */
    @Override
    public double getPheromoneCount(Position p){
	double phCount = 0;
	int x = (int)(p.getX() / area.getGranularity());
	int y = (int)(p.getY() / area.getGranularity());

	phCount += getPheromoneCount(x*(int)area.getGranularity(), 
		y*(int)area.getGranularity());
	phCount += getPheromoneCount((x+1)*(int)area.getGranularity(), 
		y*(int)area.getGranularity());
	phCount += getPheromoneCount(x*(int)area.getGranularity(), 
		(y+1)*(int)area.getGranularity());
	phCount += getPheromoneCount((x+1)*(int)area.getGranularity(), 
		(y+1)*(int)area.getGranularity());

	return phCount;
    }

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    /**
     * @return the maxPheromoneValue
     */
    public static double getMaxPheromoneValue() {
	return maxPheromoneValue;
    }

    /**
     * @return the minPheromoneValue
     */
    public static double getMinPheromoneValue() {
	return minPheromoneValue;
    }

    /**
     * @param maxPheromoneValue the maxPheromoneValue to set
     */
    public static void setMaxPheromoneValue(double maxPheromoneValue) {
	CentralizedACO.maxPheromoneValue = maxPheromoneValue;
    }

    /**
     * @param minPheromoneValue the minPheromoneValue to set
     */
    public static void setMinPheromoneValue(double minPheromoneValue) {
	CentralizedACO.minPheromoneValue = minPheromoneValue;
    }

    @Override
    public void initPheromones() {
	allPheromones.add("pheromone");

    }

}
