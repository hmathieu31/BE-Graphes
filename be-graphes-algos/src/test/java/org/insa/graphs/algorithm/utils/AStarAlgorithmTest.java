package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.*;

public class AStarAlgorithmTest extends DijkstraAlgorithmTest{

    /**
	 * Calculates the solution of data using A* Algorithm
	 * @param data Input data of A* Algorithm
	 * @return Solution of data
	 */
	protected ShortestPathSolution calculateSolution(ShortestPathData data) {
		AStarAlgorithm AStarData = new AStarAlgorithm(data);
		return AStarData.run();
	}
}
