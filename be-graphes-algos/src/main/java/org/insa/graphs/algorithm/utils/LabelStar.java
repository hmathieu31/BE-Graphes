package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{

    /**
     * Temps estimé entre la Node et la Node destination
     */
    private double estimCout;

    public LabelStar(Node departNode, Node arrivNode, ShortestPathData data) {
        super(departNode);
        double distance = departNode.getPoint().distanceTo(arrivNode.getPoint());
        switch (data.getMode()) {
            case LENGTH:
                this.estimCout = distance;
                System.out.println("Cout estimé en distance : " + this.estimCout);
                break;

            case TIME:
                this.estimCout = distance * 3600.0 / (data.getGraph().getGraphInformation().getMaximumSpeed() * 1000.0);
                
            default:
                break;
        }
       
        
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
     * @param o {@code LabelStar} to compare this label with
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(LabelStar o) {
        int comp = Double.compare(this.getTotalCout(), o.getTotalCout()) ;
        if (comp == 0) { /* En cas d'égalité entre 2 sommets, on compare leurs distances estimées */
            comp = Double.compare(this.estimCout, o.estimCout);
        }
        return comp;
    }


}
