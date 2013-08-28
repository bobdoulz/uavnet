package org.uav.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.uav.status.Status;
/**
 * @author Julien Schleich
 * A specialization to represent a UAV with two 
 * nodes and an edge
 */
public class UAVGraphRepresentationTwoNodes extends UAVGraphRepresentation {
	private Node front;
	private Node back;
	private Edge body;
	private int id;
	private int bodyLength;
		
	public UAVGraphRepresentationTwoNodes(Graph g, int id){
		super(g,id);
		this.id = id;
		this.front = g.getNode(id+"_m");
		this.back = g.addNode(id+"_b");
		this.body = g.addEdge(front.getId()+"_"+back.getId(), front, back, false);
		this.bodyLength = 5;
		
		this.front.setAttribute("ui.class", "front");
		this.back.setAttribute("ui.class", "back");
		this.body.setAttribute("ui.class", "body");
	}

	public void refreshPositionOnGraph(Status s){
		
		double moveX = Math.cos(s.getHeadingValue()) * this.bodyLength;
		double moveY = Math.sin(s.getHeadingValue()) * this.bodyLength;
		
		front.setAttribute("x", (s.getPositionX() + moveX));
		front.setAttribute("y", (s.getPositionY() + moveY));
		
		back.setAttribute("x", (s.getPositionX() - moveX));
		back.setAttribute("y", (s.getPositionY() - moveY));
	}

	/**
	 * @return the front
	 */
	public Node getFront() {
		return front;
	}

	/**
	 * @return the back
	 */
	public Node getBack() {
		return back;
	}

	/**
	 * @return the body
	 */
	public Edge getBody() {
		return body;
	}

	/**
	 * @param front the front to set
	 */
	public void setFront(Node front) {
		this.front = front;
	}

	/**
	 * @param back the back to set
	 */
	public void setBack(Node back) {
		this.back = back;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Edge body) {
		this.body = body;
	}

	/**
	 * @return the bodyLength
	 */
	public int getBodyLength() {
		return bodyLength;
	}

	/**
	 * @param bodyLength the bodyLength to set
	 */
	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
}
