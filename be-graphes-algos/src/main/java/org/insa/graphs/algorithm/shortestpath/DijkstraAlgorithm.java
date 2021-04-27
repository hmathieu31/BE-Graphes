package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.And;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import jdk.nashorn.internal.ir.Labels;

import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        if (data.getOrigin() == data.getDestination()) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {

            Label labels[]; /* Tableau référençant toutes les nodes */
            labels = new Label[data.getGraph().size()];

            List<Node> listNodes = data.getGraph().getNodes();

            // Création du tas de labels
            BinaryHeap<Label> heap = new BinaryHeap<Label>();

            // Ajout des labels initialisés au tas
            for (Node node : listNodes) {
                Label label = new Label(node);
                if (node.equals(data.getOrigin())) {
                    label.setCout((float) 0.0);
                    label.setPereArc(null);
                    heap.insert(label);
                }
                labels[node.getId()] = label; // Ajout des labels au tableau labels, qui référence les labels à partir
                                              // de la Node à laquelle ils sont rattachés
            }

            // Itérations de l'algorithme
            while (!heap.isEmpty()) {
                Label x = heap.findMin();
                x.setMarque(true);
                for (Arc arc : x.getSommetNode().getSuccessors()) {
                    Node courantNode = arc.getDestination(); // Node courante de l'itération
                    Label courantLabel = labels[courantNode
                            .getId()]; /* Label courant correspondant à la Node de l'itération */
                    if (courantLabel.getMarque() == false) {
                        if (courantLabel.getCout() > x.getCout() + arc.getLength()) {
                            if (courantLabel.getCout() > 100000000000000.0) {
                                courantLabel.setCout(x.getCout() + arc.getLength());
                                courantLabel.setPereArc(arc);
                                heap.insert(
                                        courantLabel); /* Insertion de courantLabel s'il n'avait pas été inséré avant */
                            } else {
                                courantLabel.setCout(x.getCout() + arc.getLength());
                                courantLabel.setPereArc(arc);
                                heap.remove(courantLabel);
                                heap.insert(courantLabel);
                            }
                        }
                    }
                }
                heap.remove(x);
            }

            List<Node> nodes = new ArrayList<Node>();
            Node node = data.getDestination();
            nodes.add(node);
            while (labels[node.getId()].getPere() != null) {
                node = labels[node.getId()].getPere().getOrigin();
                nodes.add(node);
            }
            Collections.reverse(nodes);
            solution = new ShortestPathSolution(data, Status.OPTIMAL,
                    Path.createShortestPathFromNodes(data.getGraph(), nodes));
        }
        return solution;

    }

}
