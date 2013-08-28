package org.uav;

import java.util.Iterator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.uav.autopilot.AbstractAutopilot;
import org.uav.graph.UAVGraph;

/**
 * @author Julien Schleich
 * This abstract class provides additional methods for a UAV to manage
 * with its local neighborhood.
 */
abstract public class AbstractUAVCentralisedNeighbourhood extends AbstractUAV {

    /** The complete UAV graph */
    protected UAVGraph uavGraph; 
    /** The local graph */
    protected DefaultGraph localGraph;

    /**
     * Constructor
     * @param auto The instance of {@link AbstractAutopilot}
     */
    public AbstractUAVCentralisedNeighbourhood(AbstractAutopilot auto, int id,
	    UAVGraph uavGraph) {
	super(auto, id);
	this.uavGraph = uavGraph;
	this.localGraph = new DefaultGraph("local"+this.getUavId());
    }	

    /**
     * Get the node representing the instance of UAV
     * @return the node instance representing the UAV
     */
    protected Node getMyGraphNode(){
	Node node = null;
	for(Node n : uavGraph.getUavGraph().getEachNode()){
	    if ((n.getId()).compareTo("uav"+this.getUavId()) == 0){
		node = n;
		break;
	    }
	}
	if (node == null){
	    System.out.println("We are uav "+this.getUavId());
	    for(Node n : uavGraph.getUavGraph().getEachNode()){
		System.out.println(n.getId());
	    }
	}
	return node;
    }

    /**
     * This method provides a sub-graph limited by a maximum distance 
     * nbHop to the considered UAV 
     *
     * @param nbHop
     * @return a subgraph 'centered' on the UAV and with up to its nbHop 
     * neighbours.
     */
    public DefaultGraph getLocalGraph(int nbHop){
	DefaultGraph local = new DefaultGraph("local"+this.getUavId());
	Node me = getMyGraphNode();

	/** Adding Nodes */
	local.addNode(me.getId());
	BreadthFirstIterator<Node> bfi = new BreadthFirstIterator<Node>(me);
	while (bfi.hasNext()){
	    Node n = bfi.next();
	    System.out.println(n.getId());
	    if (n.getId() != "uav"+this.getUavId()) { /** If not me */
		if (bfi.getDepthOf(n) <= nbHop){ /** If nb-hop neighbor */
		    local.addNode(n.getId());
		    /** Adding edges */
		    Iterator<Edge> edges = n.getEachEdge().iterator();
		    while (edges.hasNext()){
			Edge curEdge = edges.next();
			Node n1 = curEdge.getSourceNode();
			Node n2 = curEdge.getTargetNode();
			/** This test avoids edges between nbHop away nodes */
			if ( 	((bfi.getDepthOf(n1) <= nbHop) && 
				(bfi.getDepthOf(n2) < nbHop)) ||
				((bfi.getDepthOf(n1) < nbHop) && 
					(bfi.getDepthOf(n2) <= nbHop)) )
			    local.addEdge(curEdge.getId(), n1, n2);
		    }
		}
	    }
	}
	return local;
    }

}
