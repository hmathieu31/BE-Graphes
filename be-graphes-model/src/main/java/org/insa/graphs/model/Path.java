package org.insa.graphs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        if (nodes.size() == 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < nodes.size()-1; i++) {
            List<Arc> arcSuccessors = nodes.get(i).getSuccessors();
            List<Arc> validSuccessors = new ArrayList<Arc>();
            for (Arc arc : arcSuccessors) {
                if (arc.getDestination() == nodes.get(i+1)) {
                    validSuccessors.add(arc);
                }
            }
            if (validSuccessors.isEmpty()) {
                throw new IllegalArgumentException();
            }
            Arc minArc = validSuccessors.get(0);
            double minimumTravelTime = minArc.getMinimumTravelTime();
            for (Arc arc : validSuccessors) {
                if (arc.getLength() < minimumTravelTime) {
                    minimumTravelTime = arc.getMinimumTravelTime();
                    minArc = arc;
                }
            }
            arcs.add(minArc);
        }
        return new Path(graph, arcs);
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        if (nodes.size() == 1) {
            return new Path(graph, nodes.get(0));
        }
        for (int i = 0; i < nodes.size()-1; i++) {
            List<Arc> arcSuccessors = nodes.get(i).getSuccessors();
            List<Arc> validSuccessors = new ArrayList<Arc>();
            for (Arc arc : arcSuccessors) {
                if (arc.getDestination() == nodes.get(i+1)) {
                    validSuccessors.add(arc);
                }
            }
            if (validSuccessors.isEmpty()) {
                throw new IllegalArgumentException();
            }
            Arc minArc = validSuccessors.get(0);
            float minLength = minArc.getLength();
            for (Arc arc : validSuccessors) {
                if (arc.getLength() < minLength) {
                    minLength = arc.getLength();
                    minArc = arc;
                }
            }
            arcs.add(minArc);
        }
        return new Path(graph, arcs);
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     */
    public boolean isValid() {
        boolean valid = false;
        if (this.isEmpty()) { // Vérification de la condition 1 suffisante de validité du Path
            valid = true;
            return valid;
        }
        if (this.arcs.size() == 0) { // Vérification de la condition 2 suffisante de validité du Path
            valid = true;
            return valid;
        }
        boolean validOrigin = this.arcs.get(0).getOrigin().equals(this.origin); // Vérification de la ss-condition 3.1 nécessaire de validité du Path
        boolean validStarts = true;
        for (int i = 0; i < this.arcs.size()-1; i++) { // Test de la ss-condition 3.2 nécessaire de validité de Path
            validStarts = validStarts && this.arcs.get(i).getDestination() == this.arcs.get(i+1).getOrigin();
        }
        if (validStarts && validOrigin) { // Test des deux ss-conditions nécessaires 3.1 et 3.2 pour valider la condition 3 suffisante à la validité du Path
            valid = true;
        }        
        return valid;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     * 
     */
    public float getLength() {
        float len = 0;
        for (int i = 0; i < this.getArcs().size(); i++) {
            len = len + this.getArcs().get(i).getLength();
        }
        return len;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     * 
     */
    public double getTravelTime(double speed) {
        float travelTime = 0;
        for (Arc arc : arcs) {
            travelTime += arc.getTravelTime(speed);
        }
        return travelTime;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
     */
    public double getMinimumTravelTime() {
        float minimumTravelTime = 0;
        for (Arc arc : arcs) {
            minimumTravelTime += arc.getMinimumTravelTime();
        }
        return minimumTravelTime;
    }

}
