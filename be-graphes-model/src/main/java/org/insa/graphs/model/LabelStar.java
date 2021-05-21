package org.insa.graphs.model;

public class LabelStar extends Label{

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
