/**
 * 
 */
package org.uav.wireless;

import org.graphstream.graph.Node;
import org.uav.status.Position;

/**
 * @author Julien Schleich
 * An abstract class dealing with the wireless propagation model 
 * 
 * !!! Should include Z-axis !!!
 */
public abstract class AbstractPropagationModel {

    /**
     * Calculating the Euclidian distances between two UAVs
     * @param n1 the first node representing the first UAV
     * @param n2 the second node representing the second UAV
     * @return the distance
     */
    public static double getDistance(Node n1, Node n2){
	double deltaX = AbstractPropagationModel.getDeltaX(n1, n2);
	double deltaY = AbstractPropagationModel.getDeltaY(n1, n2);

	if ( (deltaX == Double.MAX_VALUE) || (deltaY == Double.MAX_VALUE)){
	    return Double.MAX_VALUE;
	}
	else
	    return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    /**
     * Calculating the Euclidian distances between two UAVs
     * @param n1 the first position
     * @param n2 the second position
     * @return the distance
     */
    public static double getDistance(Position p1, Position p2){
	double deltaX = AbstractPropagationModel.getDeltaX(p1, p2);
	double deltaY = AbstractPropagationModel.getDeltaY(p1, p2);

	if ( (deltaX == Double.MAX_VALUE) || (deltaY == Double.MAX_VALUE)){
	    return Double.MAX_VALUE;
	}
	else
	    return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    /**
     * Calculating the x-axis distance between two UAVs
     * @param n1 the first node representing the first UAV
     * @param n2 the second node representing the second UAV
     * @return the distance
     */
    public static double getDeltaX(Node n1, Node n2){
	if ( (n1.hasAttribute("x")) && (n2.hasAttribute("x")) )
	    return (Double)n1.getAttribute("x") - (Double)n2.getAttribute("x");
	else
	    return Double.MAX_VALUE;
    }

    /**
     * Calculating the y-axis distance between two UAVs
     * @param n1 the first node representing the first UAV
     * @param n2 the second node representing the second UAV
     * @return the distance
     */
    public static double getDeltaY(Node n1, Node n2){
	if ( (n1.hasAttribute("y")) && (n2.hasAttribute("y")) )
	    return (Double)n1.getAttribute("y") - (Double)n2.getAttribute("y");
	else
	    return Double.MAX_VALUE;	
    }

    /**
     * Calculating the x-axis distance between two UAVs
     * @param n1 the first position
     * @param n2 the second position
     * @return the distance
     */
    public static double getDeltaX(Position p1, Position p2){
	return p1.getX() - p2.getX();
    }

    /**
     * Calculating the y-axis distance between two UAVs
     * @param n1 the first position
     * @param n2 the second position
     * @return the distance
     */
    public static double getDeltaY(Position p1, Position p2){
	return p1.getY() - p2.getY();
    }

}
