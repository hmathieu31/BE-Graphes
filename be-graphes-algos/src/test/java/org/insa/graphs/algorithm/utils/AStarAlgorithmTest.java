package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import java.util.*;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.junit.Test;

public class AStarAlgorithmTest extends DijkstraAlgorithmTest{

    /**
	 * Calculates the solution of data using A* Algorithm
	 * @param data Input data of A* Algorithm
	 * @return Solution of data
	 */
	@Override
	protected ShortestPathSolution calculateSolution(ShortestPathData data) {
		AStarAlgorithm AStarData = new AStarAlgorithm(data);
		return AStarData.run();
	}

	@Test
	public void testLengthComparisonDijkstra() {
		ShortestPathData data;
		Random random=new Random();
		int origin=0;
		int dest=0;
		int max=graphToulouse.size();
		
		for (int i = 0 ; i<10 ; i++) {
			origin = random.nextInt(max);
			dest = random.nextInt(max);
			data=new ShortestPathData(graphToulouse,graphToulouse.get(origin), graphToulouse.get(dest), ArcInspectorFactory.getAllFilters().get(0));
			DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
			ShortestPathSolution solutionDijkstra = dijkstra.run();
			ShortestPathSolution solutionAStar = calculateSolution(data);
			
			if (solutionDijkstra.isFeasible() && solutionAStar.isFeasible()) {
				assertEquals(solutionDijkstra.getPath().getLength(), solutionAStar.getPath().getLength(),1e-5);
			} else {
				assertFalse(solutionDijkstra.isFeasible());
				assertFalse(solutionAStar.isFeasible());
			}	
		}
	}
}
