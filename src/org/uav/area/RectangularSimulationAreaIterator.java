package org.uav.area;

import java.util.Iterator;
import java.util.LinkedList;

import org.graphstream.graph.Node;

/**
 * A Iterator to ease parsing throughout a simulation area
 * @author Julien Schleich
 *
 */
public class RectangularSimulationAreaIterator implements Iterator<Node> {
    private RectangularSimulationArea area;

    private int curX;
    private int curY;
    private int stepX;
    private int stepY;


    private RectangularSimulationAreaIterator(){}

    public RectangularSimulationAreaIterator(RectangularSimulationArea area){
	this.area = area;
	this.curX = 0;
	this.curY = 0;
	this.stepX = this.area.maxX / this.area.granularity;
	this.stepY = this.area.maxY / this.area.granularity;
    }

    public boolean hasNext() {
	return (curX < stepX -1) || ((curX == stepX -1) && (curY < stepY-1));
    }

    public Node next() {
	Node res = null;
	if (hasNext()){
	    if (curY < stepY - 1){
		curY++;
	    }
	    else {
		curX++;
		curY=0;
	    }
	    res = area.getG().getNode(curX+"-"+curY);
	}
	return res;
    }

    public void remove() {


    }

}
