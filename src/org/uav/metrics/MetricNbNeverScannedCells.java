package org.uav.metrics;

import java.util.ArrayList;

import org.graphstream.graph.Node;
import org.uav.area.RectangularSimulationArea;
import org.uav.area.RectangularSimulationAreaIterator;
import org.uav.simulation.Simulation;

/**
 * A class to compute the percentage of cells of the simulation area
 * that have never been scanned yet
 * @author Julien Schleich
 *
 */
public class MetricNbNeverScannedCells extends AbstractAreaMetric {

    private int nbNeverScanned;

    public MetricNbNeverScannedCells(Simulation simu) {
	super("NbNeverScannedCells", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	this.nbNeverScanned = 0;
	RectangularSimulationAreaIterator it = 
		new RectangularSimulationAreaIterator(
			(RectangularSimulationArea) area);
	while (it.hasNext()){
	    Node n = it.next();
	    ArrayList<Integer> list = 
		    (ArrayList<Integer>)n.getAttribute("scanTime");
	    if (list.isEmpty())
		this.nbNeverScanned++;
	}
	return Math.round(((double)this.nbNeverScanned / 
		(double)this.nbCells) * 100 * 100) /100.0;
    }

}
