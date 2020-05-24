package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {
	

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
   protected void initTabLabel() {
    	final ShortestPathData data = getInputData();
        Node nodeI;
        double coutD; //Cout pour aller à la destination
        for (int i=0; i<tabLabel.length;i++) {
			nodeI=data.getGraph().getNodes().get(i);
			double coutMethode1, coutMethode2;
			
			//Si on cherche le shortest path
			if (data.getMode()==Mode.LENGTH) coutD = nodeI.getPoint().distanceTo(data.getDestination().getPoint());
			//Si on cherche le fastest path
			else {
				if (data.getGraph().getGraphInformation().hasMaximumSpeed()) {
					if (data.getMaximumSpeed()!=-1) {
						if((coutMethode1=nodeI.getPoint().distanceTo(data.getDestination().getPoint())*3.6/data.getGraph().getGraphInformation().getMaximumSpeed())<(coutMethode2=nodeI.getPoint().distanceTo(data.getDestination().getPoint())*3.6/130)) {
							coutD=coutMethode1;
						}
						else coutD=coutMethode2;						
					}
					else coutD=nodeI.getPoint().distanceTo(data.getDestination().getPoint())*3.6/data.getGraph().getGraphInformation().getMaximumSpeed();
				}
				else {
					if (data.getMaximumSpeed()!=-1) coutD=nodeI.getPoint().distanceTo(data.getDestination().getPoint())*3.6/data.getMaximumSpeed();
					else coutD=nodeI.getPoint().distanceTo(data.getDestination().getPoint())*3.6/130;
				}
				
			}
			
			if (nodeI==data.getOrigin()) {
				tabLabel[i]=new LabelStar(nodeI, false, 0, null, coutD);
			}
			else tabLabel[i]=new LabelStar(nodeI, false, Double.POSITIVE_INFINITY, null, coutD);
		}
	   
   }

}
