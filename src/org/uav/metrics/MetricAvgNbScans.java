package org.uav.metrics;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Node;
import org.uav.area.RectangularSimulationArea;
import org.uav.area.RectangularSimulationAreaIterator;
import org.uav.simulation.Simulation;

/**
 * A class to compute the average number of scans for each cells of the total
 * simulation area
 * @author Julien Schleich
 *
 */
public class MetricAvgNbScans extends AbstractAreaMetric {

    private double avgScans;

    public MetricAvgNbScans(Simulation simu) {
	super("avgNbScans", simu);
    }

    @Override
    void initializeMetric() {
    }

    @Override
    Object getMetricValue() {
	double avgScans = 0;	
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
	avgScans = Math.round(ListsStats.mean(valuesList) *100) /100.0;
	return avgScans;
    }

}
