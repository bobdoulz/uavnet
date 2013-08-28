package org.uav;

/**
 * A simple exception used for the connected coverage classes 
 * @author Julien Schleich
 *
 */
public class NoConnectedSolutionException extends Exception {

	private static final long serialVersionUID = 3404818249772171411L;

	public NoConnectedSolutionException() {}  

	public NoConnectedSolutionException(String message) {  
		super(message); 
	}  

	public NoConnectedSolutionException(Throwable cause) {  
		super(cause); 
	}  

	public NoConnectedSolutionException(String message, Throwable cause) {  
		super(message, cause); 
	} 
}
