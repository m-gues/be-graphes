package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {
	
	//Demander pour la deuxième méthode + si le coût des label doit tjr ê croissant

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        
        Node nodeI;
        double coutD; //Cout pour aller à la destination
        for (int i=0; i<tabLabel.length;i++) {
			nodeI=data.getGraph().getNodes().get(i);
			
			//Si on cherche le shortest path
			if (data.getMode()==Mode.LENGTH) coutD = nodeI.getPoint().distanceTo(data.getDestination().getPoint());
			//Si on cherche le fastest path
			else {
				if (data.getGraph().getGraphInformation().hasMaximumSpeed()) {
					/*if () coutD =;
					else*/ coutD=1/(data.getGraph().getGraphInformation().getMaximumSpeed()/nodeI.getPoint().distanceTo(data.getDestination().getPoint())/3.6);
				}
				else {
					/*if () coutD=;
					else*/ coutD=1/(130/nodeI.getPoint().distanceTo(data.getDestination().getPoint())/3.6);
				}
				
			}
			
			if (nodeI==data.getOrigin()) {
				tabLabel[i]=new LabelStar(nodeI, false, 0, null, coutD);
			}
			else tabLabel[i]=new LabelStar(nodeI, false, Double.POSITIVE_INFINITY, null, coutD);
		}
    }

}
