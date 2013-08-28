/**
 * 
 */
package org.uav.area;

import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.uav.status.Position;

/**
 * @author Julien Schleich
 * This implementation consider a rectangular simulation area.
 * For the sake of simplicity, the space is discretized into 
 * square size grid cell based on the value of granularity.
 * This class mainly deals with the generation of the space
 * and its display.
 */
public class RectangularSimulationArea extends AbstractSimulationArea {

    /** The minimum value for x axis */
    protected int minX;
    /** The maximum value for x axis */
    protected int maxX;
    /** The minimum value for y axis */
    protected int minY;
    /** The maximum value for y axis */
    protected int maxY;
    /** The granularity defines the pixel size of each grid cell */
    protected int granularity;

    /** X-axis position of the base station */
    protected int baseX;
    /** Y-axis position of the base station */
    protected int baseY;

    /*********************************************
     * 
     * Constructors
     * 
     ********************************************/

    /**
     * Constructor with (0,0) being one of the extreme points
     * of the simulation area
     * @param gra the instance of graph
     * @param maxX the maximum value of x axis
     * @param maxY the maximum value of y axis
     * @param granu the granularity 
     */
    public RectangularSimulationArea(DefaultGraph gra, int maxX, int maxY, int granu) {
	this.minX = 0;
	this.maxX = maxX;
	this.minY = 0;
	this.maxY = maxY;
	this.g = gra;
	this.granularity = granu;
    }

    /**
     * Constructor with all the details
     * @param gra the instance of graph
     * @param minX the minimum value of x axis
     * @param maxX the maximum value of x axis
     * @param minY the minimum value of y axis
     * @param maxY the maximum value of y axis
     * @param granu the granularity
     */
    public RectangularSimulationArea(DefaultGraph gra, int minX, int maxX, int minY, int maxY,int granu) {
	this.minX = minX;
	this.maxX = maxX;
	this.minY = minY;
	this.maxY = maxY;
	this.g = gra;
	this.granularity = granu;
    }



    /*********************************************
     * 
     * Simulation area component generation
     * 
     ********************************************/

    /**
     * The main method to generate the simulation space
     * reprensentation
     */
    @Override
    public void generateEnvironment() {
	generateGrid();
	generateBaseStation();
	generateStatsNodes();
	generateBorders();
    }

    /**
     * Generation of the grid 
     * Each grid cell is a node
     */
    public void generateGrid(){
	double stepX = (maxX-minX)/granularity;
	double stepY = (maxY-minY)/granularity;
	for (int i=0; i<stepX; i++){
	    for (int j=0; j<stepY; j++){
		Node n = g.addNode(i+"-"+j);
		n.setAttribute("x", i*granularity);
		n.setAttribute("y", j*granularity);
		n.setAttribute("ui.class", "grid");
		n.setAttribute("pheromone", 0.0);				
	    }
	}
    }

    /**
     * Generation of the base station
     */
    public void generateBaseStation(){
	Node bs = g.addNode("base");
	bs.setAttribute("x", (double)baseX);
	bs.setAttribute("y", (double)baseY);
	bs.setAttribute("ui.class", "base");
    }

    /**
     * Nodes used to display some statistics
     * Their label contain the statistics
     */
    public void generateStatsNodes(){
	Node stat1 = g.addNode("stat1");
	stat1.setAttribute("x", maxX+100);
	stat1.setAttribute("y", maxY+100);
	stat1.setAttribute("ui.class", "stat");

	Node stat2 = g.addNode("stat2");
	stat2.setAttribute("x", maxX+100);
	stat2.setAttribute("y", maxY+70);
	stat2.setAttribute("ui.class", "stat");
    }

    /**
     * Generation of the borders;
     * 	- An inner border to create a box
     * 	- An outer border composed of invisible nodes, placed
     * a bit further the extreme grid point in order to avoid 
     * the re-zooming issues in graphstream.
     */
    public void generateBorders(){

	// Outter border to avoid changing zoom
	Node corner = g.addNode("UpRight");
	corner.setAttribute("x", maxX+200);
	corner.setAttribute("y", maxY+200);
	corner.setAttribute("ui.class", "corner");

	corner = g.addNode("DownRight");
	corner.setAttribute("x", maxX+200);
	corner.setAttribute("y", minY-200);
	corner.setAttribute("ui.class", "corner");

	corner = g.addNode("UpLeft");
	corner.setAttribute("x", minX-200);
	corner.setAttribute("y", maxY+200);
	corner.setAttribute("ui.class", "corner");

	corner = g.addNode("DownLeft");
	corner.setAttribute("x", minX-200);
	corner.setAttribute("y", minY-200
		);
	corner.setAttribute("ui.class", "corner");

	// Inner border to delimitate the area to search
	corner = g.addNode("UpRightIn");
	corner.setAttribute("x", maxX);
	corner.setAttribute("y", maxY);
	corner.setAttribute("ui.class", "innercorner");

	corner = g.addNode("DownRightIn");
	corner.setAttribute("x", maxX);
	corner.setAttribute("y", minY-granularity);
	corner.setAttribute("ui.class", "innercorner");

	corner = g.addNode("UpLeftIn");
	corner.setAttribute("x", minX-granularity);
	corner.setAttribute("y", maxY);
	corner.setAttribute("ui.class", "innercorner");

	corner = g.addNode("DownLeftIn");
	corner.setAttribute("x", minX-granularity);
	corner.setAttribute("y", minY-granularity);
	corner.setAttribute("ui.class", "innercorner");

	Edge e = g.addEdge("InBorderUpEdge", "UpRightIn", "UpLeftIn", false);
	e.setAttribute("ui.class", "innerborder");
	e = g.addEdge("InBorderLeftEdge", "DownLeftIn", "UpLeftIn", false);
	e.setAttribute("ui.class", "innerborder");
	e = g.addEdge("InBorderDownEdge", "DownLeftIn", "DownRightIn", false);
	e.setAttribute("ui.class", "innerborder");
	e = g.addEdge("InBorderRightEdge", "DownRightIn", "UpRightIn", false);
	e.setAttribute("ui.class", "innerborder");
    }



    /*********************************************
     * 
     * Simulation area display 
     * 
     ********************************************/

    /**
     * In this implementation, the color of a grid cell depends
     * on the intensity of the pheromone. 
     * The assumption about the pheromone values are the same as in
     * the CentralizedACO class.
     * 	- If the value is between 0 and 1, then the color will also
     * be between firstColor and secondColor
     * 	- If the value is lower than zero, the color will be firstColor
     */
    @Override
    public void refreshDisplay() {
	Iterator<Node> it = g.getNodeIterator();
	while (it.hasNext()){
	    Node n = it.next();
	    if (n.hasAttribute("pheromone")){
		double phCount = n.getAttribute("pheromone");
		if (phCount < 0)
		    n.setAttribute("ui.color", 0);
		else
		    n.setAttribute("ui.color", phCount);
	    }
	}
    }


    /**
     * This method test if a position is in the area
     * @param p the position to test
     * @return true if the position is out of the area, false otherwise
     */
    @Override
    public boolean isOutOfArea(Position p) {
	return ( (p.getX() < this.minX) ||
		(p.getX() > this.maxX) ||
		(p.getY() < this.minY) ||
		(p.getY() > this.maxY) );
    }

    /**
     * This method test if a position is in the area
     * @param x the x axis coordinate of the position to test
     * @param y the y axis coordinate of the position to test
     * @return true if out of the area, false otherwise
     */
    @Override
    public boolean isOutOfArea(int x, int y) {
	return ( (x < this.minX) ||
		(x >= this.maxX) ||
		(y < this.minY) ||
		(y >= this.maxY) );
    }

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    public int getGranularity() {
	return granularity;
    }

    public void setGranularity(int granularity) {
	this.granularity = granularity;
    }

    /**
     * @return the minX
     */
    public int getMinX() {
	return minX;
    }

    /**
     * @return the maxX
     */
    public int getMaxX() {
	return maxX;
    }

    /**
     * @return the minY
     */
    public int getMinY() {
	return minY;
    }

    /**
     * @return the maxY
     */
    public int getMaxY() {
	return maxY;
    }

    /**
     * @param minX the minX to set
     */
    public void setMinX(int minX) {
	this.minX = minX;
    }

    /**
     * @param maxX the maxX to set
     */
    public void setMaxX(int maxX) {
	this.maxX = maxX;
    }

    /**
     * @param minY the minY to set
     */
    public void setMinY(int minY) {
	this.minY = minY;
    }

    /**
     * @param maxY the maxY to set
     */
    public void setMaxY(int maxY) {
	this.maxY = maxY;
    }

    /**
     * @return the baseX
     */
    public int getBaseX() {
	return baseX;
    }

    /**
     * @return the baseY
     */
    public int getBaseY() {
	return baseY;
    }

    /**
     * @param baseX the baseX to set
     */
    public void setBaseX(int baseX) {
	this.baseX = baseX;
    }

    /**
     * @param baseY the baseY to set
     */
    public void setBaseY(int baseY) {
	this.baseY = baseY;
    }

}
