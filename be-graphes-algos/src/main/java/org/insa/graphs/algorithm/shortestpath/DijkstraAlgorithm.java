package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.And;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.Label;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    

    /**
     * Crée et initialise le tableau de labels
     * @param data Input data de l'algorithm
     * @return Tableau {@code Label[]} contenant toutes les nodes
     */
    protected Label[] initLabels(ShortestPathData data) {
        Label labels[]; /* Tableau référençant toutes les nodes */
        labels = new Label[data.getGraph().size()];

        List<Node> listNodes = data.getGraph().getNodes();

        // Ajout des labels initialisés au tas
        for (Node node : listNodes) {
            Label label = new Label(node);
            if (node.equals(data.getOrigin())) {
                label.setCout((double) 0.0);
                label.setPereArc(null);
            }
            labels[node.getId()] = label; // Ajout des labels au tableau labels, qui référence les labels à partir
                                            // de la Node à laquelle ils sont rattachés
        }
        return labels;
    }


    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        if (data.getOrigin() == data.getDestination()) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {

            // Création du tas de labels
            BinaryHeap<Label> heap = new BinaryHeap<Label>();

            Label[] labels = this.initLabels(data);
            heap.insert(labels[data.getOrigin().getId()]);

            boolean found = false; 

            // Itérations de l'algorithme
            while (!heap.isEmpty() && !found) {
                Label x = heap.deleteMin();
                x.setMarque(true);
                this.notifyNodeMarked(x.getSommetNode());
                found = x.getSommetNode().getId() == data.getDestination().getId();
                for (Arc arc : x.getSommetNode().getSuccessors()) {
                    if (data.isAllowed(arc)) {
                        Node courantNode = arc.getDestination(); // Node courante de l'itération
                        this.notifyNodeReached(courantNode);
                        Label courantLabel = labels[courantNode.getId()]; /* Label courant correspondant à la Node de l'itération */

                            if (courantLabel.getCout() > x.getCout() + data.getCost(arc) && courantLabel.getMarque() == false) {
                                if (Double.isInfinite(courantLabel.getCout())) {

                                    courantLabel.setCout(x.getCout() + data.getCost(arc));
                                    courantLabel.setPereArc(arc);
                                    heap.insert(courantLabel); /* Insertion de courantLabel s'il n'avait pas été inséré avant */

                                } else {
                                    heap.remove(courantLabel);

                                    courantLabel.setCout(x.getCout() + data.getCost(arc));
                                    courantLabel.setPereArc(arc);
                                    
                                    heap.insert(courantLabel);
                                }
                            }
                    }
                }
            }

            List<Node> nodes = new ArrayList<Node>();
            Node node = data.getDestination();
            nodes.add(node);
            while (labels[node.getId()].getPere() != null) {
                node = labels[node.getId()].getPere().getOrigin();
                nodes.add(node);
            }
            Collections.reverse(nodes);
            if (found) {
                solution = new ShortestPathSolution(data, Status.OPTIMAL,
                    Path.createShortestPathFromNodes(data.getGraph(), nodes));
            }
            else {
                solution = new ShortestPathSolution(data, Status.INFEASIBLE,
                    Path.createShortestPathFromNodes(data.getGraph(), nodes));
            }
        }
        return solution;

    }

}
