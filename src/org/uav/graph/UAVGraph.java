/**
 * 
 */
package org.uav.graph;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.uav.AbstractUAV;
import org.uav.simulation.Simulation;

/**
 * @author Julien Schleich
 * This class manages a graph where the nodes are the UAVs
 * and the edges are the connections based on the big graph 
 * of the simulation
 */
public class UAVGraph {

    /** The simulation instance */
    protected Simulation s;

    /** The communication graph of the UAVs */
    protected DefaultGraph uavGraph;

    /**
     * The constructor
     * @param g the instance of {@link Simulation}
     */
    public UAVGraph(Simulation s){
	this.s = s;
	this.initGraph();
    }

    public void initGraph(){
	this.uavGraph = new DefaultGraph("uavGraph");
	Node n = this.uavGraph.addNode("base");
	n.setAttribute("x", s.getBaseX());
	n.setAttribute("y", s.getBaseY());
    }

    /**
     * A method to add a new UAV
     * @param id the identifier of the UAV
     */
    public Node addNode(AbstractUAV u){
	Node n = uavGraph.addNode("uav"+u.getUavId());
	n.setAttribute("uavinstance", u);
	return n;
    }

    /**
     * A method to remove a UAV 
     * @param id the identifier of the UAV
     */
    public void removeNode(String id){
	uavGraph.removeNode(id);
    }

    /**
     * A method to add an edge
     * @param id the identifier 
     * @param n1 the first node
     * @param n2 the second node
     */
    public void addEdge(String id, Node n1, Node n2){
	uavGraph.addEdge(id, n1, n2);
    }

    /**
     * A method to remove an edge
     * @param n1 the first node
     * @param n2 the second node
     */
    public void removeEdge(Node n1, Node n2){
	uavGraph.removeEdge(n1, n2);
    }

    /**
     * @return the s
     */
    public Simulation getS() {
	return s;
    }

    /**
     * @return the uavGraph
     */
    public DefaultGraph getUavGraph() {
	return uavGraph;
    }

    /**
     * @param s the s to set
     */
    public void setS(Simulation s) {
	this.s = s;
    }

    /**
     * @param uavGraph the uavGraph to set
     */
    public void setUavGraph(DefaultGraph uavGraph) {
	this.uavGraph = uavGraph;
    }


}
