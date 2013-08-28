package org.uav.autopilot;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.uav.status.Position;

/**
 * @author Julien Schleich
 * A basic container class for dealing with destinations 
 */
public class Destination {
    /** The current destination */
    private Position currentDestination;
    /** The graph node reprensenting the destination */
    private Node destinationRepresentation;
    /** An unique identifier*/
    private static int id=0;

    public Destination(double x, double y, Graph g){
	currentDestination = new Position(x,y);
	destinationRepresentation = g.addNode("d"+id++);
	destinationRepresentation.setAttribute("x", x);
	destinationRepresentation.setAttribute("y", y);
	destinationRepresentation.setAttribute("ui.class", "dest");
    }

    public Destination(Destination toCopy) {
	this.currentDestination = toCopy.currentDestination;
	this.destinationRepresentation = toCopy.destinationRepresentation;
    }

    public void updateDestination(double x, double y){
	currentDestination.setX(x);
	currentDestination.setY(y);
	destinationRepresentation.setAttribute("x", x);
	destinationRepresentation.setAttribute("y", y);
    }

    public void updateDestination(Position p){
	currentDestination.setX(p.getX());
	currentDestination.setY(p.getY());
	destinationRepresentation.setAttribute("x", p.getX());
	destinationRepresentation.setAttribute("y", p.getY());
    }

    /**
     * @return the currentDestination
     */
    public Position getCurrentDestination() {
	return currentDestination;
    }
    /**
     * @param currentDestination the currentDestination to set
     */
    public void setCurrentDestination(Position currentDestination) {
	this.currentDestination = currentDestination;
	this.getDestinationRepresentation().setAttribute("x", 
		currentDestination.getX());
	this.getDestinationRepresentation().setAttribute("y", 
		currentDestination.getY());
    }
    /**
     * @return the destinationRepresentation
     */
    public Node getDestinationRepresentation() {
	return destinationRepresentation;
    }
    /**
     * @param destinationRepresentation the destinationRepresentation to set
     */
    public void setDestinationRepresentation(Node destinationRepresentation) {
	this.destinationRepresentation = destinationRepresentation;
    }


}
