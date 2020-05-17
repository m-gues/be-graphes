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

public class DijkstraAlgorithmTest{
	
	protected static String mapNameCarre, mapNameHG;
	protected static DijkstraAlgorithm longueurNulle_d, standard_carre_d, standard_d, impossible_d, longueurNulle_t, standard_carre_t, standard_t, impossible_t, longueurNulle_vd, standard_carre_vd, standard_vd, impossible_vd;

	
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
    final ArcInspector nofilter_length = ArcInspectorFactory.getAllFilters().get(0);
    final ArcInspector nofilter_time = ArcInspectorFactory.getAllFilters().get(2);
    final ArcInspector cars_length = ArcInspectorFactory.getAllFilters().get(1);
    
    
    //Définitions des différents scénarios
    //Avec la carte non routière
    //Chemin de longueur nulle
    final ShortestPathData longueurNulleData_d = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(22), nofilter_length);
    final ShortestPathData longueurNulleData_t = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(22), nofilter_time);
    final ShortestPathData longueurNulleData_cl = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(22), cars_length);
    
    //Chemin standard
    final ShortestPathData standard_carreData_d = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(15), nofilter_length);
    final ShortestPathData standard_carreData_t = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(15), nofilter_time);
    final ShortestPathData standard_carreData_cl = new ShortestPathData(carre, carre.getNodes().get(22), hauteGaronne.getNodes().get(15), cars_length);
   
    
    //Avec la carte routière (Haute-Garonne)
    //Chemin impossible
    final ShortestPathData impossibleData_d = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(66593), hauteGaronne.getNodes().get(121378), nofilter_length);
    final ShortestPathData impossibleData_t = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(66593), hauteGaronne.getNodes().get(121378), nofilter_time);
    final ShortestPathData impossibleData_cl = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(66593), hauteGaronne.getNodes().get(121378), cars_length);
    
    //Chemin standard en distance
    final ShortestPathData standard_hg_Data_d = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(97448), hauteGaronne.getNodes().get(67544), nofilter_length);
    final ShortestPathData standard_hg_Data_t = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(97448), hauteGaronne.getNodes().get(67544), nofilter_time);
    final ShortestPathData standard_hg_Data_cl = new ShortestPathData(hauteGaronne, hauteGaronne.getNodes().get(97448), hauteGaronne.getNodes().get(67544), cars_length);
    

    //Initialisation des DijkstraAlgorithm
    
    //Jeu de tests pour le chemin le plus court
    //Toutes routes
    longueurNulle_d = new DijkstraAlgorithm(longueurNulleData_d);
    standard_carre_d = new DijkstraAlgorithm(standard_carreData_d);
    impossible_d = new DijkstraAlgorithm(impossibleData_d);
    standard_d = new DijkstraAlgorithm(standard_hg_Data_d);
    //Routes autorisées pour les voitures seulement
    longueurNulle_vd = new DijkstraAlgorithm(longueurNulleData_cl);
    standard_carre_vd = new DijkstraAlgorithm(standard_carreData_cl);
    impossible_vd = new DijkstraAlgorithm(impossibleData_cl);
    standard_vd = new DijkstraAlgorithm(standard_hg_Data_cl);
    
    //Jeu de tests pour le chemin le plus rapide
    longueurNulle_t = new DijkstraAlgorithm(longueurNulleData_t);
    standard_carre_t = new DijkstraAlgorithm(standard_carreData_t);
    impossible_t = new DijkstraAlgorithm(impossibleData_t);
    standard_t = new DijkstraAlgorithm(standard_hg_Data_t);
    
    
	}
	
	@Test
	public void testSolutionIsValid() {
		//Plus court chemin, toutes routes
		assertTrue(longueurNulle_d.run().getPath().isValid());
		assertTrue(standard_carre_d.run().getPath().isValid());
		assertTrue(impossible_d.run().getPath().isValid()); //Au premier essai erreur ici : doRun() renvoyait null si jamais la solution était introuvable, ce qui est incohérent avec le reste des classes. On a par conséquent modifié doRun() afin qu'il renvoie un path vide si aucune solution n'est trouvée
		assertTrue(standard_d.run().getPath().isValid());
		
		//Plus court chemin, voitures autorisées
		assertTrue(longueurNulle_vd.run().getPath().isValid());
		assertTrue(standard_carre_vd.run().getPath().isValid());
		assertTrue(impossible_vd.run().getPath().isValid()); 
		assertTrue(standard_vd.run().getPath().isValid());
		
		//Chemin le plus rapide, toutes routes
		assertTrue(longueurNulle_t.run().getPath().isValid());
		assertTrue(standard_carre_t.run().getPath().isValid());
		assertTrue(impossible_t.run().getPath().isValid()); 
		assertTrue(standard_t.run().getPath().isValid());
	}
	
	public void testLongueurSolution(DijkstraAlgorithm D) throws IllegalArgumentException{
		ShortestPathSolution solution = D.run();
		
		//Construction de la liste des nodes de la solution
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (Arc a : solution.getPath().getArcs()) {
			nodes.add(a.getOrigin());
		}
		nodes.add(solution.getPath().getDestination());
		if (D.getInputData().getMode()==Mode.LENGTH) assertEquals(solution.getPath().getLength(), Path.createShortestPathFromNodes(D.getInputData().getGraph(), nodes).getLength(), 0);
		else assertEquals(solution.getPath().getMinimumTravelTime(), Path.createFastestPathFromNodes(D.getInputData().getGraph(), nodes).getMinimumTravelTime(), 0);
	}
	
	//Note : on ne teste pas la taille ni l'optimalité de la solution vide et de celle de longueur nulle, celles-ci ont leurs propres tests plus précis
	//@Test
	public void testLongueurSolution() {
		//Plus court chemin, toutes routes
		testLongueurSolution(standard_carre_d);
		testLongueurSolution(standard_d);
		
		//Plus court chemin, voitures autorisée
		testLongueurSolution(standard_carre_vd);
		testLongueurSolution(standard_vd);
		
		//Chemin le plus rapide, toutes routes
		testLongueurSolution(standard_carre_t);
		testLongueurSolution(standard_t);
	}
	
	@Test
	public void testSolutionImpossible() {
		assertTrue(impossible_d.run().getPath().isEmpty());
		assertTrue(impossible_vd.run().getPath().isEmpty());
		assertTrue(impossible_t.run().getPath().isEmpty());
	}
	
	@Test
	public void testSolutionLongueurNulle() {
		assertEquals(longueurNulle_d.run().getPath().getLength(), 0, 0);
		assertEquals(longueurNulle_vd.run().getPath().getLength(), 0, 0);
		assertEquals(longueurNulle_t.run().getPath().getLength(), 0, 0);
	}
	
	public void testOptimaliteBellmanFord(DijkstraAlgorithm D) {
		ShortestPathSolution solutionD = D.run();
		ShortestPathSolution solutionB = new BellmanFordAlgorithm(D.getInputData()).run();
		
		if (D.getInputData().getMode()==Mode.LENGTH) assertEquals(solutionD.getPath().getLength(), solutionB.getPath().getLength(), 0);
		else assertEquals(solutionD.getPath().getMinimumTravelTime(), solutionB.getPath().getMinimumTravelTime(), 0);	
	}
	
	
	@Test 
	public void testOptimaliteBellmanFord() {
		//Plus court chemin, toutes routes
		testOptimaliteBellmanFord(standard_carre_d);
		testOptimaliteBellmanFord(standard_d);
		
		//Plus court chemin, voitures autorisées
		testOptimaliteBellmanFord(standard_carre_vd);
		testOptimaliteBellmanFord(standard_vd);
		
		//Chemin le plus rapide, toutes routes
		testOptimaliteBellmanFord(standard_carre_t);
		testOptimaliteBellmanFord(standard_t);	
	}
}
	


