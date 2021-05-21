package org.insa.graphs.algorithm.shortestpath;

import java.util.List;

import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected Label[] initLabels(ShortestPathData data) {
        Label labels[]; /* Tableau référençant toutes les nodes */
        labels = new LabelStar[data.getGraph().size()];

        List<Node> listNodes = data.getGraph().getNodes();

        // Ajout des labels initialisés au tas
        for (Node node : listNodes) {
            LabelStar label = new LabelStar(node, data.getDestination());
            if (node.equals(data.getOrigin())) {
                label.setCout((double) 0.0);
                label.setPereArc(null);
            }
            labels[node.getId()] = label; // Ajout des labels au tableau labels, qui référence les labels à partir
                                            // de la Node à laquelle ils sont rattachés
        }
        return labels;
    }

}
