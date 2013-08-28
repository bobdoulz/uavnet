package org.uav.metrics;

import org.uav.simulation.Simulation;

/**
 * The root abstract class for all metrics
 * @author Julien Schleich
 *
 */
public abstract class AbstractMetric {
    /** The simulation instance */
    protected Simulation simu;
    /* The name of the metric */
    public String name;

    @SuppressWarnings("unused")
    private AbstractMetric(){}

    public AbstractMetric(String name, Simulation simu){
	this.simu = simu;
	this.name = name;
	initializeMetric();
    }

    /**
     * An abstract method to initialize the metric 
     */
    abstract void initializeMetric();
    /**
     * The abstract method to retrieve the metric value
     * @return the metric value
     */
    abstract Object getMetricValue();
}
