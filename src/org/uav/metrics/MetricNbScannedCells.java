package org.uav.metrics;

import org.graphstream.graph.Node;
import org.uav.area.RectangularSimulationArea;
import org.uav.area.RectangularSimulationAreaIterator;
import org.uav.simulation.Simulation;

/**
 * A class to compute the average number of scanned cells
 * @author Julien Schleich
 *
 */
public class MetricNbScannedCells extends AbstractAreaMetric {

    private int nbScannedCells;

    public MetricNbScannedCells(Simulation simu) {
	super("nbScannedCells", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	this.nbScannedCells = 0;
	RectangularSimulationAreaIterator it = 
		new RectangularSimulationAreaIterator(
			(RectangularSimulationArea) area);
	int cpt = 0;
	while (it.hasNext()){
	    Node n = it.next();
	    Double time = n.getAttribute("pheromone");
	    /** Currently scanned cells */
	    if(time >= 0.0)
		this.nbScannedCells++;
	}
	return Math.round(
		((double)this.nbScannedCells / 
			(double)this.nbCells) * 100 * 100)/100.0;
    }

}
