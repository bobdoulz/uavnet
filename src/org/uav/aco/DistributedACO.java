/**
 * 
 */
package org.uav.aco;

import java.util.HashSet;
import java.util.Iterator;

import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Node;
import org.uav.AbstractUAV;
import org.uav.area.AbstractSimulationArea;
import org.uav.area.RectangularSimulationArea;
import org.uav.simulation.Simulation;
import org.uav.status.Position;

/**
 * @author Julien Schleich
 * A distributed implementation of a simplistic ACO
 * UNDER WORK
 */
public class DistributedACO extends AbstractACO {

    /**
     * @param evaporationFreq
     * @param evaporationRate
     * @param area
     */
    public DistributedACO(Simulation simu) {
	super(simu);
    }

    public void updateGeneralPheromoneInfoOnACell(int x, int y){
	Node n = area.getG().getNode(x+"-"+y);
	if (n != null){
	    Iterator<String> it = allPheromones.iterator();
	    double maxPhValue = -1;
	    while (it.hasNext()){
		String current = it.next();
		Double phValue = n.getAttribute(current);
		if (phValue != null){
		    if (phValue > maxPhValue){
			maxPhValue = phValue;
		    }
		}
	    }
	    n.setAttribute("pheromone", maxPhValue);
	}
    }

    @Override
    public void dropPheromone(Position previousPos, Position currentPos,
	    String phName, Integer uavId) {
	RectangularSimulationArea recArea = (RectangularSimulationArea) area;
	int int_x = (int)(currentPos.getX() / recArea.getGranularity());
	int int_y = (int)(currentPos.getY() / recArea.getGranularity());

	dropPheromoneOnCell(int_x, int_y, "ph"+uavId);
    }

    /**
     * For a given cell, the pheromone level is increased to maximum value
     * as the UAV fly over this position. The signification is that the UAV
     * just scanned this area.
     * @param x the x axis value of the cell
     * @param y the y axis value of the cell
     * @param area the {@link AbstractSimulationArea}
     */
    private void dropPheromoneOnCell(int x, int y, String phName){
	Node n = area.getG().getNode(x+"-"+y);
	if (n != null){
	    if (n.hasAttribute(phName)){
		double phValue = n.getAttribute(phName);
		phValue = 1;
		if (phValue > 1){
		    phValue = 1;
		}
		n.setAttribute(phName, phValue);
	    }
	    else {
		n.setAttribute(phName, 1.0);
	    }

	}
	updateGeneralPheromoneInfoOnACell(x, y);
    }

    /**
     * In this implementation, the pheromones on each cell
     * are reduced homogeneously
     */
    @Override
    public void evaporation() {
	double stepX = (area.getMaxX()-area.getMinX())/area.getGranularity();
	double stepY = (area.getMaxY()-area.getMinY())/area.getGranularity();

	for (int i=0; i<stepX; i++){
	    for (int j=0; j<stepY; j++){
		Node n = area.getG().getNode(i+"-"+j);
		Iterator<String> it = allPheromones.iterator();
		double maxPhOnCell = 0;
		while (it.hasNext()){
		    String current = it.next();

		    if (n.hasAttribute(current)){
			double phValue = n.getAttribute(current);
			if (phValue > 0.0){
			    phValue -= this.getEvaporationSpeed();
			    if (phValue < 0.0){
				phValue = 0.0;
			    }
			    if (maxPhOnCell < phValue){
				maxPhOnCell = phValue;
			    }
			}
			else {
			    phValue -= 1;
			}
			n.setAttribute(current, phValue);
		    }
		}
		n.setAttribute("pheromone", maxPhOnCell);
	    }
	}
    }

    /**
     * 
     * @param n
     * @param phNameFrom
     * @param phNameTo
     */
    public void mergeOneCell(Node n, String phNameFrom, String phNameTo){
	/** Simple maximum value */
	double from = (Double)n.getAttribute(phNameFrom);
	double to = (Double)n.getAttribute(phNameTo);
	if (from > to){
	    n.setAttribute(phNameTo, (Double)n.getAttribute(phNameFrom));
	}
    }

    /**
     * 
     * @param p
     * @param i
     * @param j
     * @param radius
     * @return
     */
    public boolean checkDistanceOfCells(Position p, int i, int j, int radius){
	boolean res = false;
	Position pCell = new Position(i*this.area.getGranularity(), 
		j*this.area.getGranularity());
	if (!this.area.isOutOfArea(pCell)){
	    if ((p.getX() * p.getX()) + (p.getY()*p.getY())< radius * radius)
		res = true;
	}
	return res;
    }

    /**
     * 
     * @param phNameFrom
     * @param phNameTo
     * @param p actual center of the UAV
     * @param radius in number of cells
     */
    public void mergePheromones(String phNameFrom, 
	    String phNameTo, 
	    Position p, 
	    int radius){
	int rSquare = radius * radius;
	Position pInArea = new Position(p.getX()/this.area.getGranularity(), 
		p.getY()/this.area.getGranularity());
	for (int i = (int)pInArea.getX() - radius; 
		i < (int)pInArea.getX() + radius; i++){
	    for (int j = (int)pInArea.getY() - radius; 
		    j < (int)pInArea.getY() + radius; i++){
		if (checkDistanceOfCells(pInArea, i, j, radius)){
		    Node n = this.simu.getG().getNode(i+"-"+j);
		    mergeOneCell(n, phNameFrom, phNameTo);
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

    @Override
    public void initPheromones() {
	for (int i = 0; i < simu.getNbUAV(); i++){
	    int id = simu.u[i].getUavId();
	    HashSet<String> tmp = this.allPheromonesPerUAV.get(i);
	    tmp.add("ph"+id);
	    allPheromonesPerUAV.put(id, tmp);
	    allPheromones.add("ph"+id);
	}
    }



}
