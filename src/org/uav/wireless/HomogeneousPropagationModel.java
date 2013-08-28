/**
 * 
 */
package org.uav.wireless;

import org.graphstream.graph.Node;
import org.uav.status.Position;

/**
 * @author Julien Schleich
 * A simplistic wireless propagation model based solely on
 * a maximum transmission range
 */
public class HomogeneousPropagationModel extends AbstractPropagationModel {

    /**
     * The maximum distance for which a wireless communication can occur
     */
    public static double maxDistance = 400;

    /**
     * The main method to detect if a wireless communication can occur between 
     * two UAVs
     * @param n1 the first node representing the first UAV
     * @param n2 the second node representing the second UAV
     * @return true if the n1 and n2 are close enough, false otherwise
     */
    public static boolean areCloseEnoughToDecode(Node n1, Node n2){
	double deltaX = getDeltaX(n1,n2);
	double deltaY = getDeltaY(n1,n2);

	/// First raw check for distance
	if ( (deltaX > HomogeneousPropagationModel.maxDistance) ||
		(deltaY > HomogeneousPropagationModel.maxDistance) )
	    return false;

	/// More precise check if useful
	if (getDistance(n1,n2)>HomogeneousPropagationModel.maxDistance)
	    return false;
	else
	    return true;
    }

    /**
     * The main method to detect if a wireless communication can occur between 
     * two UAVs
     * @param n1 the first node representing the first UAV
     * @param n2 the second node representing the second UAV
     * @return true if the n1 and n2 are close enough, false otherwise
     */
    public static boolean areCloseEnoughToDecode(Position p1, Position p2){
	double deltaX = getDeltaX(p1,p2);
	double deltaY = getDeltaY(p1,p2);

	/// First raw check for distance
	if ( (deltaX > HomogeneousPropagationModel.maxDistance) ||
		(deltaY > HomogeneousPropagationModel.maxDistance) )
	    return false;

	/// More precise check if useful
	if (getDistance(p1,p2)>HomogeneousPropagationModel.maxDistance)
	    return false;
	else
	    return true;
    }

}
