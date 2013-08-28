package org.uav.simulation;

import java.io.IOException;



/**
 * @author Julien Schleich
 * When the Simulation class is done, this class should permit to easily 
 * execute batches of simulation instances and aggregate results. For now it 
 * is still a bit messy but it works
 */
public class SimulationBatch {


    public static void main(String[] args) throws InterruptedException, IOException {

	System.setProperty("org.graphstream.ui.renderer", 
		"org.graphstream.ui.j2dviewer.J2DGraphRenderer");

	Simulation simu = new Simulation();
	simu.setNbUAV(15);
	simu.setSeed(1);
	simu.setNbTimestepsInTheFuture(30);

	simu.setLocalDecisionFrequency(30);

	simu.setMaxTimesteps(10000);
	simu.setWarmingSteps(500);
	simu.setWarmingSteps(0);
	simu.setDisplay(true);

	//simu.uavType = "UAVBasicPheromoneMovement";
	//simu.uavType = "UAVDistributedPheromoneMovement";
	//simu.uavType = "UAVBasicPheromonewithrepulsion";
	//simu.uavType = "UAVRandomMovement";
	//simu.uavType = "UAVRandomDestination";
	simu.uavType = "UAVBasicConnectedCoverage";
	//simu.uavType="UAVRandomMovementwithrepulsion";

	simu.statsFile = simu.uavType+"-"+20+"-"+1;
	System.out.println("Processing: "+simu.statsFile);
	try {
	    simu.runSimulation();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }



}


