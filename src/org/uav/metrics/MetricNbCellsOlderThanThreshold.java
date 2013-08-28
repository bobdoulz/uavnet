package org.uav.metrics;

import org.graphstream.graph.Node;
import org.uav.area.RectangularSimulationArea;
import org.uav.area.RectangularSimulationAreaIterator;
import org.uav.simulation.Simulation;

/**
 * A class to compute the percentage of cells that the UAV did not scanned
 * frequently enough given a provided threshold
 * @author Julien Schleich
 *
 */
public class MetricNbCellsOlderThanThreshold extends AbstractAreaMetric {

    private int nbCellsOlderThanThreshold;

    public MetricNbCellsOlderThanThreshold(Simulation simu) {
	super("NbCellsOlderThanThreshold", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	this.nbCellsOlderThanThreshold = 0;
	RectangularSimulationAreaIterator it = 
		new RectangularSimulationAreaIterator(
			(RectangularSimulationArea) area);
	while (it.hasNext()){
	    Node n = it.next();
	    Double time = n.getAttribute("pheromone");
	    /** Too old cells */
	    if ((time < 0) && (Math.abs(time) >= simu.getTooOld()))
		this.nbCellsOlderThanThreshold++;
	}
	return Math.round(
		((double)this.nbCellsOlderThanThreshold / 
			(double)this.nbCells) * 100 * 100) / 100.0;
    }

}
