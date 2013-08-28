/**
 * 
 */
package org.uav.aco;

import java.util.HashMap;
import java.util.HashSet;

import org.uav.AbstractUAV;
import org.uav.area.AbstractSimulationArea;
import org.uav.area.RectangularSimulationArea;
import org.uav.simulation.Simulation;
import org.uav.status.Position;

/**
 * An abstract Ant Colony Optimisation module
 * @author Julien Schleich
 */
abstract public class AbstractACO {
    /** The simulation instance */
    protected Simulation simu;

    /** How frequently (in time step) pheromone quantity vanish */
    protected double evaporationFreq;

    /** How much pheromone quantity vanish for a 'evaporationFreq' period */
    protected double evaporationSpeed;

    /** The simulation area on which the ACO is operated */
    protected RectangularSimulationArea area;

    /** The HashSet containing all the types of pheromones */
    protected HashSet<String> allPheromones;

    /** The HashMap containing the types of pheromones per uav */
    protected HashMap < Integer, HashSet<String> > allPheromonesPerUAV;

    /*********************************************
     * 
     * Main constructor
     * 
     ********************************************/

    public AbstractACO(Simulation simu) {
	this.simu = simu;
	this.evaporationFreq = simu.getEvaporationFreq();
	this.evaporationSpeed = simu.getEvaporationSpeed();
	this.area = (RectangularSimulationArea)simu.getArea();
	this.allPheromones = new HashSet<String>();
	this.allPheromonesPerUAV = new HashMap < Integer, HashSet<String> >();
	for (int i = 0; i < simu.u.length; i++)
	    this.allPheromonesPerUAV.put(simu.u[i].getUavId(), new HashSet<String>());

	initPheromones();
    }

    /*********************************************
     * 
     * Pheromones types management
     * 
     ********************************************/

    /**
     * A simple getter method for the pheromones of a UAV
     * @param the UAV identifier
     * @return the hashset of pheromones
     */
    public HashSet<String> getPheromonesOfUav(int uavId){
	return this.allPheromonesPerUAV.get(uavId);
    }

    /**
     * A method to add a new pheromone type to a UAV
     * @param uavId the identifier of the UAV
     * @param phName the new pheromone type
     */
    public void addPheromoneToUav(int uavId, String phName){
	this.allPheromones.add(phName);
	HashSet<String> tmp = this.allPheromonesPerUAV.get(uavId);
	tmp.add(phName);
	this.allPheromonesPerUAV.remove(uavId);
	this.allPheromonesPerUAV.put(uavId, tmp);
    }

    public abstract void initPheromones();

    /*********************************************
     * 
     * Main pheromone mechanism methods
     * 
     ********************************************/

    /**
     * The method allows a UAV to drop some pheromone in the simulation area
     * depending on its previous and current position
     * @param previousPos the previous {@link Position}
     * @param currentPos the current {@link Position}
     * @param uav the considered {@link AbstractUAV}
     * @param area the instance of {@link AbstractSimulationArea}
     */
    abstract public void dropPheromone(Position previousPos, 
	    Position currentPos, 
	    String phName, 
	    Integer uavId);

    /**
     * The method causes pheromone quantity to drop depending on
     * the value of evaporationRate
     * @param area the considered {@link AbstractSimulationArea} 
     */
    abstract public void evaporation();

    /**
     * The method returns the quantity of pheromone
     * around a position p
     * @param p the instance of {@link Position}
     * @return the amount of pheromone
     */
    abstract public double getPheromoneCount(Position p);

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    /**
     * @return the evaporationFreq
     */
    public double getEvaporationFreq() {
	return evaporationFreq;
    }

    /**
     * @return the evaporationRate
     */
    public double getEvaporationSpeed() {
	return evaporationSpeed;
    }

    /**
     * @param evaporationFreq the evaporationFreq to set
     */
    public void setEvaporationFreq(double evaporationFreq) {
	this.evaporationFreq = evaporationFreq;
    }

    /**
     * @param evaporationRate the evaporationRate to set
     */
    public void setEvaporationSpeed(double Speed) {
	this.evaporationSpeed = evaporationSpeed;
    }

    /**
     * @return the area
     */
    protected AbstractSimulationArea getArea() {
	return area;
    }

    /**
     * @return the allPheromones
     */
    public HashSet<String> getAllPheromones() {
	return allPheromones;
    }

    /**
     * @return the allPheromonesPerUAV
     */
    public HashMap<Integer, HashSet<String>> getAllPheromonesPerUAV() {
	return allPheromonesPerUAV;
    }

    /**
     * @param area the area to set
     */
    public void setArea(RectangularSimulationArea area) {
	this.area = area;
    }

    /**
     * @param allPheromones the allPheromones to set
     */
    public void setAllPheromones(HashSet<String> allPheromones) {
	this.allPheromones = allPheromones;
    }

    /**
     * @param allPheromonesPerUAV the allPheromonesPerUAV to set
     */
    public void setAllPheromonesPerUAV(
	    HashMap<Integer, HashSet<String>> allPheromonesPerUAV) {
	this.allPheromonesPerUAV = allPheromonesPerUAV;
    }

}
