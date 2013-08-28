/**
 * 
 */
package org.uav;

import org.uav.autopilot.AbstractAutopilot;
import org.uav.autopilot.AutopilotFixedWingOutOfSimulationAreaStrategy;

/**
 * @author Julien Schleich
 * This class implement a very basic type of behavior in which
 * a UAV randomly selects if it turns right, left of stay straight.
 * The probability for each behavior have been set according to the
 * work of ... 
 */
public class UAVRandomMovement extends AbstractUAV {

    /**
     * Constructor
     * @param auto the instance au {@link AbstractAutopilot}
     */
    public UAVRandomMovement(AbstractAutopilot auto, int id) {
	super(auto, id);
    }

    /**
     * The random choice (left, center or right)
     */
    public void newRandomChoice(){
	AutopilotFixedWingOutOfSimulationAreaStrategy auto = 
		(AutopilotFixedWingOutOfSimulationAreaStrategy)getAuto();
	double random = rand.nextDouble();
	if (random < 0.2)
	    this.updateDestination(auto.getLeftPoint());
	if ((random >= 0.2) && (random < 0.8))
	    this.updateDestination(auto.getFrontPoint());
	if (random >= 0.8)
	    this.updateDestination(auto.getRightPoint());
    }

    @Override
    protected void localActionAfterMove() {}

    /**
     * The random choice is taken at the decision frequency
     */
    @Override
    protected void localActionBeforeMove() {
	if (getTime() == getDecisionFrequency())
	    newRandomChoice();
    }

}
