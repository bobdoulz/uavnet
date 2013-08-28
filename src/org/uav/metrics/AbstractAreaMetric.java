package org.uav.metrics;

import org.uav.area.AbstractSimulationArea;
import org.uav.simulation.Simulation;

/**
 * An abstract class for metrics requiring the whole simulation are.
 * @author Julien Schleich
 *
 */
public abstract class AbstractAreaMetric extends AbstractMetric {
    /** The number of cells of the area */
    protected int nbCells;
    /** The number of steps on the X axis in the area */
    protected int stepX;
    /** The number of steps on the Y axis in the area */
    protected int stepY;

    protected AbstractSimulationArea area;
    public AbstractAreaMetric(String name, Simulation simu) {
	super(name, simu);
	this.area = simu.getArea();
	this.stepX = (simu.getMaxX())/simu.getGranularity();
	this.stepY = (simu.getMaxY())/simu.getGranularity();
	this.nbCells = stepX * stepY;
    }


}
