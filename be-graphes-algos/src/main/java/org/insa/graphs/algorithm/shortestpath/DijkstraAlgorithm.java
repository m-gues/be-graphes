package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;

import java.util.concurrent.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	//Tableau contenant les étiquettes des noeuds
	protected Label tabLabel[];

	public DijkstraAlgorithm(ShortestPathData data) {
		super(data);
		tabLabel = new Label[data.getGraph().size()];
	}
	
	protected void initTabLabel() {
		// Initialisation du tableau d'étiquettes
		Node nodeI;
		final ShortestPathData data = getInputData();
		for (int i=0; i<tabLabel.length;i++) {
			nodeI=data.getGraph().getNodes().get(i);
			if (nodeI==data.getOrigin()) {
				tabLabel[i]=new Label(nodeI, false, 0, null);
			}
			else tabLabel[i]=new Label(nodeI, false, Double.POSITIVE_INFINITY, null);
		}
	}


	@Override
	protected ShortestPathSolution doRun() {
		long lStartTime = System.nanoTime();
		initTabLabel();
		final ShortestPathData data = getInputData();
		ShortestPathSolution solution = null;
		Node nodeI; //Variables temporaires de type Node, qui seront utilisées pour l'initialisation du tableau et pendant les itérations, afin d'éviter d'aller chercher une même node plusieurs fois
		Label labelI,labelJ; //Labels temporaires utilisés dans les itérations
		double nouvCout; //Entier temporaire où l'on stockera le cout nouvellement calculé lors des itérations 
		int nbIterations = 0;
		boolean destinationMarquee = false;

		//Création du tas des sommets en cours de traitement
		BinaryHeap<Label> tasTraitement=new BinaryHeap<Label>(); 
		

		//Initialisation du tas des sommets en traitement
		tasTraitement.insert(tabLabel[data.getOrigin().getId()]); 


		//Itérations
		while (!destinationMarquee&&nbIterations<tabLabel.length) { //Note : tabLabel.length est égal au nombre de noeuds dans le graphe, on l'utilise car il est moins "difficile" à retrouver que la taille du graphe en lui-même)
			
			//Trouver le sommet de coût minimum, mettre sa node en NodeI et l'enlever du tas des labels en traitement avec remove()
			if (tasTraitement.isEmpty()) {
				break ; //Ce break est utilisé à titre exceptionnel uniquement : je ne veux pas capturer l'exception de file vide par risque de capturer le diagnostic d'une autre erreur de code.
			}

			labelI = tasTraitement.deleteMin();
			nodeI = labelI.getSommetCourant();

			labelI.setMarque(true);
			notifyNodeMarked(nodeI);			
			if (nodeI==data.getDestination()) destinationMarquee = true;
			if (nodeI==data.getOrigin()) notifyOriginProcessed(data.getOrigin());

			for (Arc arcIJ: nodeI.getSuccessors()) {
				labelJ=tabLabel[arcIJ.getDestination().getId()];
				if (data.isAllowed(arcIJ)&&!labelJ.getMarque()) {

					//Si on cherche le shortest path
					if (data.getMode()==Mode.LENGTH) nouvCout = labelI.getCout()+arcIJ.getLength();
					//Si on cherche le fastest path
					else nouvCout = labelI.getCout()+arcIJ.getMinimumTravelTime();

					//Mise à jour du sommet (et du tas) si nécéssaire
					if (nouvCout<labelJ.getCout()) {
						//Insertion dans le tas des sommets en traitement si c'est la première maj du sommet
						if (Double.isInfinite(labelJ.getCout()) && Double.isFinite(nouvCout)) {
							labelJ.setCout(nouvCout);
							labelJ.setPere(arcIJ);
							tasTraitement.insert(labelJ);
							notifyNodeReached(arcIJ.getDestination());
						}
						else {
							tasTraitement.remove(labelJ);
							labelJ.setCout(nouvCout);
							labelJ.setPere(arcIJ);
							tasTraitement.insert(labelJ);
						}
					}
				}
			}
			nbIterations++;
		}

				
		
		//Construction de la solution
		if (!tabLabel[data.getDestination().getId()].getMarque()) {
			nodeI = null;
			solution = new ShortestPathSolution(data, Status.INFEASIBLE, new Path(data.getGraph(), nodeI));
		}
		else {

			//La destination a été atteinte
			notifyDestinationReached(data.getDestination());

			// Cree le chemin à partir des informations des labels
			ArrayList<Arc> arcs = new ArrayList<>();
			Arc arc = tabLabel[data.getDestination().getId()].getPere();
			while (arc != null) {
				arcs.add(arc);
				arc = tabLabel[arc.getOrigin().getId()].getPere();
			}

			// Reverse the path...
			Collections.reverse(arcs);

			// Create the final solution.
			if (arcs.isEmpty()) solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), data.getOrigin()));
			else solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
		}
		long lEndTime = System.nanoTime();
		System.out.println("Temps de calcul (ms) : "+(lEndTime-lStartTime)/1000000);
		
		return solution;
	}
}
