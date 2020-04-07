package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label {
	
	private Node sommetCourant;
	private boolean marque;
	private double cout;
	private Arc pere;
	
	public Label(Node sommetCourant, boolean marque, double cout, Arc pere) {
		this.sommetCourant=sommetCourant;
		this.marque=marque;
		this.cout=cout;
		this.pere=pere;
	}
	
	//Accesseurs	
	public Node getSommetCourant() {
		return sommetCourant;
	}
	
	public boolean getMarque() {
		return marque;
	}
	
	public double getCost() {
		return cout;
	}
	
	public Arc getPere() {
		return pere;
	}
	
	//Mutateurs	
	public void setSommetCourant(Node nouvSomC) {
		this.sommetCourant=nouvSomC;
	}
	
	public void setMarque(boolean nouvMarque) {
		marque=nouvMarque;
	}
	
	public void setCout(double nouvCout) {
		cout=nouvCout;
	}
	
	public void setPere(Arc nouvPere) {
		pere=nouvPere;
	}
	

}
