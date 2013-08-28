package org.uav.metrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.graphstream.graph.Node;
import org.uav.simulation.Simulation;

/** 
 * A class to manage the required metrics, get the last metric values and 
 * output everything in a buffer
 * @author Julien Schleich
 *
 */
public class MetricsManager {
    /** The collection of the required metrics */
    private Collection<AbstractMetric> metrics;
    /** The collection of the current values of the aforementioned metrics */
    private Collection<Object> currentValues;

    /** The considered simulation */
    private Simulation simu;
    /** The buffer to write the results */
    private BufferedWriter outStats;

    /** No default constructor */
    @SuppressWarnings("unused")
    private MetricsManager(){};

    /** Initialisation of the manager and the simulation area */
    public MetricsManager(Simulation simu){
	this.simu = simu;
	this.metrics = new ArrayList<AbstractMetric>();
	this.currentValues = new ArrayList<Object>();
	FileWriter fstream;
	try {
	    if (this.simu.statsFile== null)
		fstream = new FileWriter("noname.stats");
	    else
		fstream = new FileWriter(this.simu.statsFile+".csv");
	    this.outStats = new BufferedWriter(fstream);
	} catch (IOException e) {
	    System.out.println(
		    "An error occured when attempting to open the stats file");
	}

	int stepX = (simu.getMaxX())/simu.getGranularity();
	int stepY = (simu.getMaxY())/simu.getGranularity();
	for (int i=0; i<stepX; i+=1){
	    for(int j=0; j<stepY; j+=1){
		Node n = simu.getG().getNode(i+"-"+j);
		if (simu.isUseLastUAVId())
		    n.setAttribute("lastUAV", Integer.MIN_VALUE);
		if (simu.isUseScanTimes())
		    n.setAttribute("scanTime", new ArrayList<Integer>());
		n.setAttribute("pheromone", Double.valueOf(-simu.getTooOld()));
	    }
	}
    }

    public void addTitleLineToFile(){
	try {
	    this.outStats.write(this.getMetricsNames()+'\n');
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public String getMetricsNames(){
	String names = new String("time"+',');
	for (AbstractMetric curMetric : this.metrics){
	    names += curMetric.name+',';
	}
	return names.substring(0, names.length()-1);
    }

    public void updateMetricsValues(){
	this.currentValues.clear();
	for (AbstractMetric curMetric : this.metrics){
	    this.currentValues.add(curMetric.getMetricValue());
	}
    }

    public String getMetricsValuesToString(){
	updateMetricsValues();
	String str = new String();
	for (Object curValue : this.currentValues){
	    str += curValue.toString()+",";
	}
	/** Removal of the extra ',' character */
	return str.substring(0, str.length()-1);
    }

    public String appendCurrentValuesToFile(){
	String str = getMetricsValuesToString();
	str = (simu.getBigCpt()-simu.getWarmingSteps())+","+str;
	try {
	    this.outStats.append(str+"\n");
	} catch (IOException e) {
	    System.out.println("An error occured when attempting to write in the stats file");
	}
	return str;
    }

    public void closeFile(){
	try {
	    this.outStats.close();
	} catch (IOException e) {
	    System.out.println("An error occured when attempting to close the stats file");

	}
    }
    public void addMetric(AbstractMetric m){
	this.metrics.add(m);
    }

    public void removeMetric(AbstractMetric m){
	this.metrics.remove(m);
    }

    public void clearMetrics(){
	this.metrics.clear();
    }

}
