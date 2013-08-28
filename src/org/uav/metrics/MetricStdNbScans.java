package org.uav.metrics;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Node;
import org.uav.area.RectangularSimulationArea;
import org.uav.area.RectangularSimulationAreaIterator;
import org.uav.simulation.Simulation;

/**
 * A class to compute the std of the number of scans
 * @author Julien Schleich
 *
 */
public class MetricStdNbScans extends AbstractAreaMetric {

    private double stdScans;

    public MetricStdNbScans(Simulation simu) {
	super("stdNbScans", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	this.stdScans = 0;	
	RectangularSimulationAreaIterator it = 
		new RectangularSimulationAreaIterator(
			(RectangularSimulationArea) area);
	List<Integer> valuesList = new ArrayList<Integer>();
	while (it.hasNext()){
	    Node n = it.next();
	    ArrayList<Integer> list = 
		    (ArrayList<Integer>)n.getAttribute("scanTime");
	    valuesList.add(list.size());
	}
	this.stdScans = Math.round(ListsStats.std(valuesList) *100) /100.0;
	return this.stdScans;
    }

}
