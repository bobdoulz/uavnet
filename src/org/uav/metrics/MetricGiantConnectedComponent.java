package org.uav.metrics;

import org.uav.simulation.Simulation;

/**
 * A class to compute the size (in perc) of the biggest connected component
 * in the UAV graph
 * @author Julien Schleich
 *
 */
public class MetricGiantConnectedComponent 
extends AbstractConnectedComponentsMetric {

    public MetricGiantConnectedComponent(Simulation simu) {
	super("GiantConnectedComponent", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	this.updateConnectedComponents();
	return Math.round(
		((double)this.cc.getGiantComponent().size() / 
			(double)(simu.uavGraph.getUavGraph().getNodeCount())) *
			100 * 100)/100.0;
    }

}
