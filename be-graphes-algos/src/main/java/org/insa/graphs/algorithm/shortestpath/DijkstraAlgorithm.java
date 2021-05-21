package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.And;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected class Label implements Comparable<Label> {

        /** Sommet (Node) associé au label */ 
        private Node sommet_courant;
    
        /** Bool, vrai lorsque le coût min de ce sommet est définitivement connu par l'algo */
        private boolean marque;
    
        /**
         * Valeur courante du plus court chemin de l'originie jusqu'au sommet
        */ 
        private float cout;
    
        /**
         * sommet précédent sur le chemin correspondant au plus court chemin courant
         */
        private Arc pereArc;
    
    
        public Label(Node sommetNode) {
            this.sommet_courant = sommetNode;
            this.marque = false;
            this.cout = 1.0f/0.0f;
            this.pereArc = null;
        }
    
        /**
         * Getter du cout du label
         * @return Cout du label
         */
        public float getCout() {
            return this.cout;
        }
    
        public float getTotalCout() {
            return this.cout;
        }
    
        /**
         * Getter de la marque du label
         * @return Boolean de marque du label
         */
        public boolean getMarque() {
            return this.marque;
        }
    
        /**
         * Getter du Pere (sous forme d'arc)
         * @return pereArc
         */
        public Arc getPere() {
            return this.pereArc;
        }
    
        public Node getSommetNode() {
            return this.sommet_courant;
        }
    
        /**
         * Setter de marque
         * @param marqueBool
         */
        public void setMarque(boolean marqueBool) {
            this.marque = marqueBool;
        }
    
        /**
         * Setter de cout
         * @param coutArg
         */
        public void setCout(float coutArg) {
            this.cout = coutArg;
        }
    
        /**
         * Setter de pereArc
         * @param pereArcArg
         */
        public void setPereArc(Arc pereArcArg) {
            this.pereArc = pereArcArg;
        }
    
        /**
         * Compare the cost of this Label with the cost of the given Label
         * @param o Label to compare this label with
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Label o) {
            return Float.compare(getTotalCout(), o.getCout());
        }
    }

    
    private class LabelStar extends Label{

        private float estimCout;
    
        public LabelStar(Node departNode, Node arrivNode) {
            super(departNode);
            this.estimCout = (float) Math.sqrt(Math.pow((departNode.getPoint().getLatitude() - arrivNode.getPoint().getLatitude()), 2) + Math.pow((departNode.getPoint().getLongitude() - arrivNode.getPoint().getLongitude()), 2));
        }
    
        /**
         * Getter de TotalCout
         */
        public float getTotalCout() {
            return this.getCout() + this.estimCout;
        }
    
    
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

            boolean found = false;
            // Itérations de l'algorithme
            while (!heap.isEmpty() && !found) {
                Label x = heap.deleteMin();
                x.setMarque(true);
                System.out.println("Node supprimée : " + x.getCout());
                this.notifyNodeMarked(x.getSommetNode());
                found = x.getSommetNode().getId() == data.getDestination().getId();
                for (Arc arc : x.getSommetNode().getSuccessors()) {
                    if (data.isAllowed(arc)) {
                        Node courantNode = arc.getDestination(); // Node courante de l'itération
                        this.notifyNodeReached(courantNode);
                        Label courantLabel = labels[courantNode.getId()]; /* Label courant correspondant à la Node de l'itération */

//                            System.out.println(found);


                            if (courantLabel.getCout() > x.getCout() + arc.getLength()) {
                                if (Double.isInfinite(courantLabel.getCout())) {
//                                    System.out.println("Node courante ajout : " + courantLabel.getSommetNode().getId());
//                                    System.out.println("Node courante ajout nd : " + courantLabel.getSommetNode());
//                                    System.out.println("Label associe ajout : " + courantLabel);

                                    courantLabel.setCout(x.getCout() + arc.getLength());
                                    courantLabel.setPereArc(arc);
                                    heap.insert(courantLabel); /* Insertion de courantLabel s'il n'avait pas été inséré avant */
                                } else {
//                                    System.out.println("Node courante : " + courantLabel.getSommetNode().getId());
//                                    System.out.println("Node courante nd : " + courantLabel.getSommetNode());
//                                    System.out.println("Label associé : " + courantLabel);
//                                    System.out.println("Cout : " + courantLabel.getCout());
//                                    System.out.println("Marque : " + courantLabel.getMarque());

                                    heap.remove(courantLabel);

                                    courantLabel.setCout(x.getCout() + arc.getLength());
                                    courantLabel.setPereArc(arc);

//                                    System.out.println("passed");
                                    /* 
                                        Pour exemple, sur un trajet entre le point 67 et le point 11111, l'algorithme plante systématiquement sur suppression du label associé à la Node 4941
                                    */
                                    

                                    heap.insert(courantLabel);
                                }
                            }
                    }
                }
            }

            System.out.println("Fin algo");
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
