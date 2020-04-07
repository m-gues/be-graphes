package org.insa.graphs.algorithm.shortestpath;

import java.util.Arrays;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;

// Voir pour les fonctions en Notify() pour l'affichage dès qu'on a une version naïve

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	//Tableau contenant les étiquettes des noeuds
	private Label tabLabel[];

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        tabLabel = new Label[data.getGraph().size()-1];
    }
    

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Node nodeI; //Variables temporaires de type Node, qui seront utilisées pour l'initialisation du tableau et pendant les itérations, afin d'éviter d'aller chercher une même node plusieurs fois
        Arc arcIJ;  // Arc temporaire utilisé pendant les itérations
        Label labelI,labelJ; //Labels temporaires utilisés dans les itérations
       double nouvCout; //Entier temporaire où l'on stockera le cout nouvellement calculé lors des itérations 
        
        
        // Initialisation du tableau d'étiquettes
        for (int i=0; i<tabLabel.length;i++) {
        	nodeI=data.getGraph().getNodes().get(i);
        	if (nodeI==data.getOrigin()) tabLabel[i]=new Label(nodeI, false, 0, null);
        	else tabLabel[i]=new Label(nodeI, false, Double.POSITIVE_INFINITY, null);
        }
        
        //Création et initialisation du tas des sommets en cours de traitement
        BinaryHeap<Node> tasTraitement=new BinaryHeap<Node>();
        tasTraitement.insert(data.getOrigin());
        
        //Itérations
        nodeI=data.getOrigin();
        while (nodeI!=data.getDestination()) {
        	labelI = tabLabel[data.getGraph().getNodes().indexOf(nodeI)];
        	for (int j=0; j<nodeI.getNumberOfSuccessors(); j++) {
        		arcIJ=nodeI.getSuccessors().get(j);
        		labelJ=tabLabel[data.getGraph().getNodes().indexOf(arcIJ.getDestination())];
        		if (!labelJ.getMarque()) {
	        		nouvCout = labelI.getCost()+arcIJ.getLength();
	        		
	        		//Insertion dans le tas des sommets en traitement si c'est la première maj du sommet
	        		if (Double.isInfinite(labelJ.getCost()) && Double.isFinite(nouvCout)) tasTraitement.insert(arcIJ.getDestination());
	        		
	        		//Maj du sommet si nécéssaire
	        		if (nouvCout<labelJ.getCost()) {
	        			labelJ.setCout(nouvCout);
	        			labelJ.setPere(arcIJ);
	        		}
	        		
        		}
        	}
        	labelI.setMarque(true);
        	//Trouver le sommet de coût minimum, le mettre en nodeI et l'enlever avec remove()
        }
        //Construction de la solution : voir Bellman-ford (/!\ Cas où pas de chemins !!!!)
        return solution;
    }

}
