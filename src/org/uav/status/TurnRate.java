package org.uav.status;
/**
 * @author Julien Schleich
 * Basic turn rate implementation, speed is not influenced by turn rate change
 * maxTurnRateChange can be set to Integer.MAX_VALUE to obtain an even more 
 * unrealistic model
 * 
 * !!! This class is not in use for now !!! 
 */
public class TurnRate {
    /** The maximum turning rate (in rad) */
    private int maxTurnRate; 
    /** The current turning rate (in rad) */
    private int currentTurnRate; 
    /** The maximum turning rate change (in rad / timestep) */
    private int maxTurnRateChange; // in rad / timestep

    public TurnRate() {
	super();
	this.currentTurnRate = 0;
	this.maxTurnRate = Integer.MAX_VALUE;
	this.maxTurnRateChange = Integer.MAX_VALUE;
    }

    public TurnRate(int maxTurnRate) {
	super();
	this.currentTurnRate = 0;
	this.maxTurnRate = maxTurnRate;
	this.maxTurnRateChange = Integer.MAX_VALUE;
    }

    public TurnRate(int maxTurnRate, int currentTurnRate) {
	super();
	this.currentTurnRate = currentTurnRate;
	this.maxTurnRate = maxTurnRate;
	this.maxTurnRateChange = Integer.MAX_VALUE;
    }

    public TurnRate(int maxTurnRate, int currentTurnRate, 
	    int maxTurnRateChange) {
	super();
	this.currentTurnRate = currentTurnRate;
	this.maxTurnRate = maxTurnRate;
	this.maxTurnRateChange = maxTurnRateChange;
    }

    public TurnRate(TurnRate turn){
	this(turn.maxTurnRate, turn.currentTurnRate, turn.maxTurnRateChange);
    }

    public int getCurrentTurnRate() {
	return currentTurnRate;
    }
    public void setCurrentTurnRate(int targetTurnRate) {
	this.currentTurnRate = targetTurnRate;
    }
    public int getMaxTurnRate() {
	return maxTurnRate;
    }
    public void setMaxTurnRate(int maxTurnRate) {
	this.maxTurnRate = maxTurnRate;
    }
    public int getMaxTurnRateChange() {
	return maxTurnRateChange;
    }
    public void setMaxTurnRateChange(int maxTurnRateChange) {
	this.maxTurnRateChange = maxTurnRateChange;
    }

}
