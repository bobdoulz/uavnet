/**
 * 
 */
package org.uav;

import org.uav.autopilot.AbstractAutopilot;

/**
 * @author Julien Schleich
 * This class implement a very basic type of behavior in which
 * a UAV randomly selects a new random destination every 
 *
 */
public class UAVRandomDestination extends AbstractUAV {

    protected boolean goToDestinationBeforeUpdating = false;

    /**
     * Constructor
     * @param auto The instance of {@link AbstractAutopilot}
     */
    public UAVRandomDestination(AbstractAutopilot auto, int id) {
	super(auto, id);
    }

    public boolean isUAVAtDestination(){
	boolean atDestination = false;

	return atDestination;
    }

    /**
     * The new destination is chosen at the decision frequency
     */
    @Override
    protected void localActionBeforeMove() {
	if (getTime() == getDecisionFrequency())
	    if (!goToDestinationBeforeUpdating){
		updateDestination(auto.newRandomDestination());
	    }
	    else {
		if (isUAVAtDestination()){
		    updateDestination(auto.newRandomDestination());
		}
	    }

    }

    @Override
    protected void localActionAfterMove() {}


}
