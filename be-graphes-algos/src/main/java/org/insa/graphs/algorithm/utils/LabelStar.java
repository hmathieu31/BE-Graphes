package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{

    private double estimCout;

    public LabelStar(Node departNode, Node arrivNode, ShortestPathData data) {
        super(departNode);
        this.estimCout = departNode.getPoint().distanceTo(arrivNode.getPoint());
        
    }

    /**
     * Getter de TotalCout (Cout actuel + cout estimé)
     * Redéfinie la méthode getTotalCout de Label
     * @return Le cout total du label
     */
    public double getTotalCout() {
        return this.getCout() + this.estimCout;
    }

    /**
     * Compare the cost of this Label with the cost of the given Label
     * @param o Label to compare this label with
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(LabelStar o) {
        int comp = Double.compare(this.getTotalCout(), o.getTotalCout()) ;
        if (comp == 0) {
            comp = Double.compare(this.getTotalCout(), o.getTotalCout());
        }
        return comp;
    }


}
