package org.uav.metrics;

import org.graphstream.algorithm.ConnectedComponents;
import org.uav.simulation.Simulation;

/** 
 * An abstract class for metrics requiring a ConnectedComponent instance 
 * @author Julien Schleich
 *
 */
public abstract class AbstractConnectedComponentsMetric 
extends AbstractUAVGraphMetric {

    /** A Graphstream ConnectedComponent object */
    protected ConnectedComponents cc;

    /** We will compute the cc on the UAV Graph */
    public AbstractConnectedComponentsMetric(String name, Simulation simu) {
	super(name, simu);
	this.cc = new ConnectedComponents(simu.uavGraph.getUavGraph());
    }

    /** Updating the cc object */
    void updateConnectedComponents(){
	this.cc.compute();
    }

}
