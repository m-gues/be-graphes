package org.insa.graphs.algorithm.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;

public class AStarAlgorithmTest extends DijkstraAlgorithmTest{
	
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
    longueurNulle_d = new AStarAlgorithm(longueurNulleData_d);
    standard_carre_d = new AStarAlgorithm(standard_carreData_d);
    impossible_d = new AStarAlgorithm(impossibleData_d);
    standard_d = new AStarAlgorithm(standard_hg_Data_d);
    //Routes autorisées pour les voitures seulement
    longueurNulle_vd = new AStarAlgorithm(longueurNulleData_cl);
    standard_carre_vd = new AStarAlgorithm(standard_carreData_cl);
    impossible_vd = new AStarAlgorithm(impossibleData_cl);
    standard_vd = new AStarAlgorithm(standard_hg_Data_cl);
    
    //Jeu de tests pour le chemin le plus rapide
    longueurNulle_t = new AStarAlgorithm(longueurNulleData_t);
    standard_carre_t = new AStarAlgorithm(standard_carreData_t);
    impossible_t = new AStarAlgorithm(impossibleData_t);
    standard_t = new AStarAlgorithm(standard_hg_Data_t);   
	}
}
