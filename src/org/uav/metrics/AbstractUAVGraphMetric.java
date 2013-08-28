package org.uav.metrics;

import org.uav.simulation.Simulation;

/**
 * An abstract class for all metrics requiring access to the simulation 
 * in general and to its UAV graph in particular
 * @author Julien Schleich
 *
 */
public abstract class AbstractUAVGraphMetric extends AbstractMetric {

    public AbstractUAVGraphMetric(String name, Simulation simu) {
	super(name, simu);
    }

}
