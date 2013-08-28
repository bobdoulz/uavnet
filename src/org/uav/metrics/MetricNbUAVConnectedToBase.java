package org.uav.metrics;

import java.util.Iterator;

import org.graphstream.algorithm.ConnectedComponents.ConnectedComponent;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Node;

import org.uav.simulation.Simulation;

/**
 * A class to compute the percentage of UAVs connected to the base station,
 * i.e. the UAVs in the connected component containing the base station.
 * @author Julien Schleich
 *
 */
public class MetricNbUAVConnectedToBase 
extends AbstractConnectedComponentsMetric {

    public MetricNbUAVConnectedToBase(Simulation simu) {
	super("nbUAVConnectedToBase", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	Node baseNode = simu.uavGraph.getUavGraph().getNode("base");
	BreadthFirstIterator<Node> it = 
		new BreadthFirstIterator<Node>(baseNode);
	int cpt = 0;
	while (it.hasNext()){
	    cpt ++;
	    it.next();
	}

	return Math.round((double)(cpt-1) / 
		(double)(simu.uavGraph.getUavGraph().getNodeCount()-1) * 
		100 * 100) / 100.0;
    }

}
