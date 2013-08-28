package org.uav.metrics;

import org.uav.simulation.Simulation;

/**
 * This class computes the number of connected components of the UAV graph
 * @author Julien Schleich
 *
 */
public class MetricNbConnectedComponents 
extends AbstractConnectedComponentsMetric {

    public MetricNbConnectedComponents(Simulation simu) {
	super("nbConnectedComponents", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	this.updateConnectedComponents();
	return this.cc.getConnectedComponentsCount();
    }

}
