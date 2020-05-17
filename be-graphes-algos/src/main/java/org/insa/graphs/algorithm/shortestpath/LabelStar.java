package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
	
	double coutPourDestination;

	public LabelStar(Node sommetCourant, boolean marque, double cout, Arc pere, double coutD) {
		super(sommetCourant, marque, cout, pere);
		coutPourDestination = coutD;
	}
	
	//Mutateur
	public double getCoutPourDestination() {
		return this.coutPourDestination;
	}
	
	@Override
	public double getTotalCost() {
		return this.getCout()+this.coutPourDestination;
	}
	
	

}
