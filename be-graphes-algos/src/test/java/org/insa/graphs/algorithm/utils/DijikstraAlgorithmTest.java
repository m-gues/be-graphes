package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijikstraAlgorithmTest{
	
	//!!!!!!!! On a dû changer les doRun en public, demander pour ça !!!!!!!!
	
	private static String mapNameCarre, mapNameHG;
	private static DijkstraAlgorithm longueurNulle, standard_carre, standard_temps, standard_distance, impossible;

	
	@BeforeClass
	public static void initAll() throws Exception {
	
	//Charge les graphes utilisés pour les tests
		
	mapNameCarre = "C:\\Users\\Marianne\\Desktop\\maps\\extras\\carre.mapgr";
	mapNameHG = "C:\\Users\\Marianne\\Desktop\\maps\\europe\\france\\haute-garonne.mapgr";

    // Création des lecteurs
    final GraphReader readerCarre = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapNameCarre))));
    
    final GraphReader readerHG = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapNameHG))));
 
    final Graph carre = readerCarre.read();
    final Graph hauteGaronne = readerHG.read();
    
    readerCarre.close();
    readerHG.close();
    
    
    //Récupération des filtres ArcInspector
    final ArcInspector nofilter = ArcInspectorFactory.getAllFilters().get(0);
    final ArcInspector cars_time = ArcInspectorFactory.getAllFilters().get(3);
    
    
    //Définitions des différents scénarios
    //Avec la carte non routière
    //Chemin de longueur nulle
    final ShortestPathData longueurNulleData = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(22), nofilter);
    
    //Chemin "standard" en distance
    final ShortestPathData standard_carreData = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(15), nofilter);
   
    
    //Avec la carte routière
    //Chemin impossible
    final ShortestPathData impossibleData = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(66593), hauteGaronne.getNodes().get(121378), nofilter);
    
    //Chemin standard en distance
    final ShortestPathData standard_distanceData = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(97448), hauteGaronne.getNodes().get(67544), nofilter);
    
    
    //Chemin standard en temps
    final ShortestPathData standard_tempsData = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(97448), hauteGaronne.getNodes().get(67544), cars_time); 

    //Initialisation des DijkstraAlgorithm
    longueurNulle = new DijkstraAlgorithm(longueurNulleData);
    standard_carre = new DijkstraAlgorithm(standard_carreData);
    impossible = new DijkstraAlgorithm(impossibleData);
    standard_distance = new DijkstraAlgorithm(standard_distanceData);
    standard_temps = new DijkstraAlgorithm(standard_tempsData);
    
    
	}
	
	//@Test
	public void testSolutionIsValid() {
		assertTrue(longueurNulle.doRun().getPath().isValid());
		assertTrue(standard_carre.doRun().getPath().isValid());
		assertTrue(impossible.doRun().getPath().isValid()); //Au premier essai erreur ici : doRun() renvoyait null si jamais la solution était introuvable, ce qui est incohérent avec le reste des classes. On a par conséquent modifié doRun() afin qu'il renvoie un path vide si aucune solution n'est trouvée
		assertTrue(standard_distance.doRun().getPath().isValid());
		assertTrue(standard_temps.doRun().getPath().isValid());
	}
	
	public void testLongueurSolution(DijkstraAlgorithm D) throws IllegalArgumentException{
		ShortestPathSolution solution = D.doRun();
		
		//Construction de la liste des nodes de la solution
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (Arc a : solution.getPath().getArcs()) {
			nodes.add(a.getOrigin());
		}
		nodes.add(solution.getPath().getDestination());
		if (D.getInputData().getMode()==Mode.LENGTH) assertEquals(solution.getPath().getLength(), Path.createShortestPathFromNodes(D.getInputData().getGraph(), nodes).getLength(), 0);
		else assertEquals(solution.getPath().getMinimumTravelTime(), Path.createFastestPathFromNodes(D.getInputData().getGraph(), nodes).getMinimumTravelTime(), 0);
	}
	
	//Note : on ne teste pas la taille de la solution vide, celle-ci a son propre test plus précis
	//@Test
	public void testLongueurSolution() {
		testLongueurSolution(longueurNulle);
		testLongueurSolution(standard_carre);
		testLongueurSolution(standard_distance);
		testLongueurSolution(standard_temps);
	}
	
	//@Test
	public void testSolutionImpossible() {
		assertTrue(impossible.doRun().getPath().isEmpty());
	}
	
	public void testOptimaliteBellmanFord(DijkstraAlgorithm D) {
		ShortestPathSolution solutionD = D.doRun();
		ShortestPathSolution solutionB = new BellmanFordAlgorithm(D.getInputData()).doRun();
		
		if (D.getInputData().getMode()==Mode.LENGTH) assertEquals(solutionD.getPath().getLength(), solutionB.getPath().getLength(), 0);
		else assertEquals(solutionD.getPath().getMinimumTravelTime(), solutionB.getPath().getMinimumTravelTime(), 0);	
	}
	
	@Test 
	public void testOptimaliteBellmanFord() {
		testOptimaliteBellmanFord(standard_carre);
		testOptimaliteBellmanFord(standard_distance);
		testOptimaliteBellmanFord(standard_temps);
	}
}
	


