package org.uav.status;

/**
 * @author Julien Schleich
 * A simple container class to deal with
 * 3-dimensional positions 
 */
public class Position {
    /** The x-axis coordinate */
    private double x;
    /** The y-axis coordinate */
    private double y;
    /** The z-axis coordinate */
    private double z;

    /**
     * The default constructor
     */
    public Position() {
	this.x = 0;
	this.y = 0;
	this.z = 0;
    }

    /**
     * A constructor with two coordinates
     * @param x the x-axis value
     * @param y the y-axis value
     */
    public Position(double x, double y) {
	super();
	this.x = x;
	this.y = y;
	this.z = 0;
    }

    /**
     * A constructor with three coordinates
     * @param x the x-axis value
     * @param y the y-axis value
     * @param z the z-axis value
     */
    public Position(double x, double y, double z) {
	super();
	this.x = x;
	this.y = y;
	this.z = z;
    }

    /**
     * Copy constructor
     * @param pos
     */
    public Position(Position pos){
	this(pos.x, pos.y, pos.z);
    }

    public double euclideanDistance(Position p2){
	return Math.sqrt(Math.pow((this.x - p2.x),2) +
		Math.pow((this.y - p2.y),2) +Math.pow((this.z - p2.z),2)); 
    }

    public Position minus(Position p2){
	return new Position(this.x-p2.x, this.y-p2.y, this.z-p2.z);
    }

    public Position minus(double value){
	return new Position(this.x-value, this.y-value, this.z-value);
    }


    public Position add(Position p2){
	return new Position(p2.x+this.x, p2.y+this.y, p2.z+this.z);
    }

    public Position times(double value){
	return new Position(this.x*value, this.y*value, this.z*value);
    }
    public boolean isEqualTo(Position p2){
	return ((this.x==p2.x) && (this.y==p2.y) && (this.z==p2.z));
    }

    public void print(){
	System.out.println("("+Math.round(this.x)+", "+
    Math.round(this.y)+", "+Math.round(this.z)+")");
    }

    /*********************************************
     * 
     * Getters / Setters
     * 
     ********************************************/

    /**
     * To get the x-axis value
     * @return the x-axis value
     */
    public double getX() {
	return x;
    }

    /**
     * To set the x-axis value
     * @param x the x-axis value
     */
    public void setX(double x) {
	this.x = x;
    }

    /**
     * To get the y-axis value
     * @return the y-axis value
     */
    public double getY() {
	return y;
    }

    /**
     * To set the y-axis value
     * @param y the y-axis value
     */
    public void setY(double y) {
	this.y = y;
    }

    /**
     * To get the z-axis value
     * @return the z-axis value
     */
    public double getZ() {
	return z;
    }

    /**
     * To set the z-axis value
     * @param z the z-axis value
     */
    public void setZ(double z) {
	this.z = z;
    }

}
