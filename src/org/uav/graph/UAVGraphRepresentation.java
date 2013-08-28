/**
 * 
 */
package org.uav.graph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.uav.status.Status;

/**
 * @author Julien Schleich
 * A class to detail how to represent a UAV in the simulation
 * 
 * !!! It should not be abstract, change that !!!
 */
public abstract class UAVGraphRepresentation {

	protected Node mainNode;
	protected int id;

	/**
	 * The regular constructor
	 * @param mainNode
	 * @param id
	 */
	public UAVGraphRepresentation(Graph g, int id){
		this.id = id;
		this.mainNode = g.addNode(id+"_m");
	}
	
	/**
	 * Refresh the representation of the UAV 
	 * @param s an instance of {@link Status}
	 */
	abstract public void refreshPositionOnGraph(Status s);

	/**
	 * @return the mainNode
	 */
	public Node getMainNode() {
		return mainNode;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param mainNode the mainNode to set
	 */
	public void setMainNode(Node mainNode) {
		this.mainNode = mainNode;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
