package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.model.*;
import org.insa.graphs.model.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.util.*;


import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {

    /* List of Graphs to be used in the tests */
	protected static Graph graphGreatBritain = null ;
	protected static Graph graphToulouse = null ;  
	protected static Graph graphCarre = null ;
	protected static Graph graphHauteGaronne = null ;


	/**
	 * Initialization of 4 graphs
	 * @throws IOException If an exception occurs while reading the graph
	 * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
	 */
	@BeforeClass
	public static void initAll() throws IOException  {
		final String mapGreatBritain = "D:\\Users\\Hugo\\Mes documents\\Code\\BE-Graphes\\Maps\\british-isles.mapgr";
		final String mapCarre = "D:\\Users\\Hugo\\Mes documents\\Code\\BE-Graphes\\Maps\\carre.mapgr";
		final String mapHauteGaronne = "D:\\Users\\Hugo\\Mes documents\\Code\\BE-Graphes\\Maps\\haute-garonne.mapgr";
		final String mapToulouse = "D:\\Users\\Hugo\\Mes documents\\Code\\BE-Graphes\\Maps\\toulouse.mapgr";
		
		final GraphReader readerGreatBritain = new BinaryGraphReader(
	            new DataInputStream(new BufferedInputStream(new FileInputStream(mapGreatBritain))));
		final GraphReader readerCarre = new BinaryGraphReader(
	            new DataInputStream(new BufferedInputStream(new FileInputStream(mapCarre))));
		final GraphReader readerHauteGaronne = new BinaryGraphReader(
	            new DataInputStream(new BufferedInputStream(new FileInputStream(mapHauteGaronne))));
		final GraphReader readerToulouse = new BinaryGraphReader(
				new DataInputStream(new BufferedInputStream(new FileInputStream(mapToulouse))));
		
		DijkstraAlgorithmTest.graphGreatBritain = readerGreatBritain.read();
		DijkstraAlgorithmTest.graphCarre = readerCarre.read();
		DijkstraAlgorithmTest.graphHauteGaronne = readerHauteGaronne.read();
		DijkstraAlgorithmTest.graphToulouse = readerToulouse.read();
	}


	/**
	 * Calculates the solution of data using Dijktra Algorithm
	 * @param data Input data of Dijkstra Algorithm
	 * @return Solution of data
	 */
	protected ShortestPathSolution calculateSolution(ShortestPathData data) {
		DijkstraAlgorithm DijkstraData = new DijkstraAlgorithm(data);
		return DijkstraData.run();
	}
	

	/* Data corresponding to valid Pathes */
	private ShortestPathData dataGreatBritain = new ShortestPathData(graphGreatBritain,graphGreatBritain.get(180645), graphGreatBritain.get(4893028), ArcInspectorFactory.getAllFilters().get(0));
	private ShortestPathData dataToulouse = new ShortestPathData(graphToulouse,graphToulouse.get(1234), graphToulouse.get(4223), ArcInspectorFactory.getAllFilters().get(0));
	private ShortestPathData dataCarre = new ShortestPathData(graphCarre,graphCarre.get(13), graphCarre.get(1), ArcInspectorFactory.getAllFilters().get(0) );
	private ShortestPathData dataHauteGaronne = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(121), graphHauteGaronne.get(318), ArcInspectorFactory.getAllFilters().get(0) );

	/**
	 * Verifies the {@code Path} returned by the algorithm with connected components is valid
	 */
	@Test
	public void testIsValid() {
		ShortestPathSolution solutionGreatBritain = this.calculateSolution(dataGreatBritain);
		assertTrue(solutionGreatBritain.getPath().isValid());
		
		ShortestPathSolution solutionToulouse = calculateSolution(dataToulouse);
		assertTrue(solutionToulouse.getPath().isValid());
		
		ShortestPathSolution solutionCarre = calculateSolution(dataCarre);
		assertTrue(solutionCarre.getPath().isValid());;
		
		ShortestPathSolution solutionHauteGaronne = calculateSolution(dataHauteGaronne);
		assertTrue(solutionHauteGaronne.getPath().isValid());
	}
	

	private ShortestPathData invalidData = new ShortestPathData(
		graphGreatBritain, graphGreatBritain.get(1318338), graphGreatBritain.get(1014719), ArcInspectorFactory.getAllFilters().get(0));
	private ShortestPathData validData = new ShortestPathData(
		graphGreatBritain, graphGreatBritain.get(1652428), graphGreatBritain.get(6640405), ArcInspectorFactory.getAllFilters().get(0));

	/**
	 * Verifies the Solution returned by the algorithm between 2 discontinued components is Not Feasable using GB map
	 * Verifies the Solution returned by the algorithm between 2 connected component is Feasable 
	 */
	@Test
	public void testNoPath() {
		ShortestPathSolution invalidSolution = calculateSolution(invalidData);
		assertFalse(invalidSolution.isFeasible());
		
		ShortestPathSolution validSolution = calculateSolution(validData);
		assertTrue(validSolution.isFeasible());
	}

	
	private ShortestPathData nullPathData = new ShortestPathData(
		graphHauteGaronne, graphHauteGaronne.get(24), graphHauteGaronne.get(24), ArcInspectorFactory.getAllFilters().get(0)) ;

	/**
	 * Verifies the Solution returned by the algorithm between an identical origin and destination is Not Feasable
	 */
	@Test
	public void testNullPath() {
		ShortestPathSolution solutionHauteGaronne = calculateSolution(nullPathData);
		assertFalse(solutionHauteGaronne.isFeasible());
	}


	/**
	 * Comparison of the algorithm and Bellman-Ford algorithm for 10 seperate Paths in "Shortest path, all roads allowed" mode
	 * Tolerance of Length 1e-5 between the two
	 */
	@Test
	public void testLengthComparisonBellmanFord() {
		ShortestPathData data;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphToulouse.size();
		
		for (int i = 0 ; i<10 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			data=new ShortestPathData(graphToulouse,graphToulouse.get(origin), graphToulouse.get(dest), ArcInspectorFactory.getAllFilters().get(0));
			BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
			ShortestPathSolution solutionBellman = Bellman.run();
			ShortestPathSolution solutionDijkstra = calculateSolution(data);
			
			if (solutionDijkstra.isFeasible() && solutionBellman.isFeasible()) {
				assertEquals(solutionDijkstra.getPath().getLength(), solutionBellman.getPath().getLength(),1e-5);
			} else {
				assertFalse(solutionDijkstra.isFeasible());
				assertFalse(solutionBellman.isFeasible());
			}	
		}
	}


	/**
	 * Comparison of the algorithm and Bellman-Ford algorithm for 10 seperate Paths in "Fastest path, all roads allowed" mode
	 * Tolerance of Time 1e-5 between the two
	 */
	@Test
	public void testTimeComparisonBellmanFord() {
		ShortestPathData data ;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphToulouse.size();

		for (int i = 0 ; i<10 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			data=new ShortestPathData(graphToulouse,graphToulouse.get(origin), graphToulouse.get(dest), ArcInspectorFactory.getAllFilters().get(2) );
			BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
			ShortestPathSolution solutionBellman = Bellman.run();
			ShortestPathSolution solutionDijkstra = calculateSolution(data);
			
			if (solutionDijkstra.isFeasible() && solutionBellman.isFeasible()) {
				assertEquals(solutionDijkstra.getPath().getMinimumTravelTime(), solutionBellman.getPath().getMinimumTravelTime(),1e-5);
			} else {
				assertFalse(solutionDijkstra.isFeasible());
				assertFalse(solutionBellman.isFeasible());
			}
		}
	}

	
	/**
	 * Comparison of the algorithm and Bellman-Ford algorithm for 10 seperate Paths in "Shortest path, only roads open for cars" mode
	 * Tolerance of Length 1e-5 between the two
	 */
	@Test
	public void testLengthComparisonBellmanFordCarsOnly() {
		ShortestPathData data ;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphToulouse.size();

		for (int i = 0 ; i<10 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			data=new ShortestPathData(graphToulouse,graphToulouse.get(origin), graphToulouse.get(dest), ArcInspectorFactory.getAllFilters().get(1) );
			BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
			ShortestPathSolution solutionBellman = Bellman.run();
			ShortestPathSolution solutionDijkstra = calculateSolution(data);

			if (solutionDijkstra.isFeasible() && solutionBellman.isFeasible()) {
				assertEquals(solutionDijkstra.getPath().getLength(), solutionBellman.getPath().getLength(),1e-5);
			} else {
				assertFalse(solutionDijkstra.isFeasible());
				assertFalse(solutionBellman.isFeasible());
			}
		}
	}
	

	/**
	 * Comparison of the algorithm and Bellman-Ford algorithm for 10 seperate Paths in "Fastest path, only roads open for cars" mode
	 * Tolerance of Time 1e-5 between the two
	 */
	@Test
	public void testTimeComparisonBellmanFordCarsOnly() {
		ShortestPathData data ;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphToulouse.size();

		for (int i = 0 ; i<10 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			data=new ShortestPathData(graphToulouse,graphToulouse.get(origin), graphToulouse.get(dest), ArcInspectorFactory.getAllFilters().get(3) );
			BellmanFordAlgorithm Bellman = new BellmanFordAlgorithm(data);
			ShortestPathSolution solutionBellman = Bellman.run();
			ShortestPathSolution solutionDijkstra = calculateSolution(data);
			
			if (solutionDijkstra.isFeasible() && solutionBellman.isFeasible()) {
				assertEquals(solutionDijkstra.getPath().getMinimumTravelTime(), solutionBellman.getPath().getMinimumTravelTime(),1e-5);
			} else {
				assertFalse(solutionDijkstra.isFeasible());
				assertFalse(solutionBellman.isFeasible());
			}
		}	
	}
	


	@Test
	public void testCostPathEqualsDijkstraLength() {
		ShortestPathSolution solutionGreatBritain = calculateSolution(dataGreatBritain);

		List <Node> nodes= new ArrayList <Node>();
		nodes.add(dataGreatBritain.getOrigin());

		List<Arc> arcs = solutionGreatBritain.getPath().getArcs();
		for (Arc arc : arcs) {
			nodes.add(arc.getDestination());
		}

		assertEquals(solutionGreatBritain.getPath().getLength(), Path.createShortestPathFromNodes(graphGreatBritain, nodes).getLength(),1e-15);
	}
	

	/**
	 * Verifies if the cost in TIME calculated by Dijktra is equal to the cost calculated by the class {@code Path}
	 * Tolerance of 1e-15 on the equality
	 */
	@Test
	public void testCostPathEqualsDijkstraTime() {
		ShortestPathSolution solutionGreatBritain = calculateSolution(dataGreatBritain);

		List <Node> nodes= new ArrayList <Node>();
		nodes.add(dataGreatBritain.getOrigin());

		List<Arc> arcs = solutionGreatBritain.getPath().getArcs();
		for (Arc arc : arcs) {
			nodes.add(arc.getDestination());
		}

		assertEquals(solutionGreatBritain.getPath().getMinimumTravelTime(), Path.createFastestPathFromNodes(graphGreatBritain, nodes).getMinimumTravelTime(),1e-15);
	}
}
