package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;

// Voir pour les fonctions en Notify() pour l'affichage dès qu'on a une version naïve

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	//Tableau contenant les étiquettes des noeuds
	private Label tabLabel[];

	public DijkstraAlgorithm(ShortestPathData data) {
		super(data);
		tabLabel = new Label[data.getGraph().size()];
	}


	@Override
	protected ShortestPathSolution doRun() {
		final ShortestPathData data = getInputData();
		ShortestPathSolution solution = null;
		Node nodeI; //Variables temporaires de type Node, qui seront utilisées pour l'initialisation du tableau et pendant les itérations, afin d'éviter d'aller chercher une même node plusieurs fois
		Arc arcIJ;  // Arc temporaire utilisé pendant les itérations
		Label labelI,labelJ; //Labels temporaires utilisés dans les itérations
		double nouvCout; //Entier temporaire où l'on stockera le cout nouvellement calculé lors des itérations 
		int nbIterations = 0;

		//Création du tas des sommets en cours de traitement
		BinaryHeap<Label> tasTraitement=new BinaryHeap<Label>(); 

		// Initialisation du tableau d'étiquettes
		for (int i=0; i<tabLabel.length;i++) {
			nodeI=data.getGraph().getNodes().get(i);
			if (nodeI==data.getOrigin()) {
				tabLabel[i]=new Label(nodeI, false, 0, null);
				tasTraitement.insert(tabLabel[i]); //Initialisation du tas des sommets en traitement
			}
			else tabLabel[i]=new Label(nodeI, false, Double.POSITIVE_INFINITY, null);
		}

		System.out.println("/////////////////////////////////////////////////////////");
		//Traitement de l'origine
		nodeI=data.getOrigin();
		labelI = tasTraitement.deleteMin();
		notifyOriginProcessed(data.getOrigin());
		//Itérations
		while (nodeI!=data.getDestination()&&nbIterations<tabLabel.length) { //Note : tabLabel.length est égal au nombre de noeuds dans le graphe, on l'utilise car il est moins "difficile" à retrouver que la taille du graphe en lui-même
			System.out.println("Nb successeurs : "+nodeI.getNumberOfSuccessors());
			for (int j=0; j<nodeI.getNumberOfSuccessors(); j++) {
				System.out.println(tasTraitement.size());
				arcIJ=nodeI.getSuccessors().get(j);
				labelJ=tabLabel[data.getGraph().getNodes().indexOf(arcIJ.getDestination())];
				if (data.isAllowed(arcIJ)&&!labelJ.getMarque()) {

					//Si on cherche le shortest path
					if (data.getMode()==Mode.LENGTH) nouvCout = labelI.getCout()+arcIJ.getLength();
					else nouvCout = labelI.getCout()+arcIJ.getMinimumTravelTime();

					//Insertion dans le tas des sommets en traitement si c'est la première maj du sommet
					if (Double.isInfinite(labelJ.getCout()) && Double.isFinite(nouvCout)) {
						tasTraitement.insert(labelJ);
						notifyNodeReached(arcIJ.getDestination());
					}

					//Mise à jour du sommet si nécéssaire
					if (nouvCout<labelJ.getCout()) {
						labelJ.setCout(nouvCout);
						labelJ.setPere(arcIJ);
					}
				}
			}
			labelI.setMarque(true);
			notifyNodeMarked(nodeI);
			//Trouver le sommet de coût minimum, mettre sa node en NodeI et l'enlever du tas des labels en traitement avec remove()
			if (tasTraitement.isEmpty()) {
				break ; //Ce break est utilisé à titre exceptionnel uniquement : je ne veux pas capturer l'exception de file vide par risque de capturer le diagnostic d'une autre erreur de code.
			}
			else {
				nodeI = tasTraitement.findMin().getSommetCourant();
				labelI = tabLabel[data.getGraph().getNodes().indexOf(nodeI)];
				tasTraitement.remove(labelI);
				nbIterations++;
			}
		}
		labelI.setMarque(true);
				
		
		//Construction de la solution
		if (!tabLabel[data.getGraph().getNodes().indexOf(data.getDestination())].getMarque()) {
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
		}
		else {

			//La destination a été atteinte
			notifyDestinationReached(data.getDestination());

			// Create the path from the array of predecessors...
			ArrayList<Arc> arcs = new ArrayList<>();
			Arc arc = labelI.getPere();
			while (arc != null) {
				arcs.add(arc);
				arc = tabLabel[data.getGraph().getNodes().indexOf(arc.getOrigin())].getPere();
			}

			// Reverse the path...
			Collections.reverse(arcs);

			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
		}
		return solution;
	}
}
