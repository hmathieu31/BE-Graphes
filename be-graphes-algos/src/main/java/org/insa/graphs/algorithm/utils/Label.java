package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {

    /** Sommet (Node) associé au label */ 
    private Node sommet_courant;

    /** Bool, vrai lorsque le coût min de ce sommet est définitivement connu par l'algo */
    private boolean marque;

    /**
     * Valeur courante du plus court chemin de l'originie jusqu'au sommet
    */ 
    private double cout;

    /**
     * sommet précédent sur le chemin correspondant au plus court chemin courant
     */
    private Arc pereArc;

    /**
     * Insecurite courante jusqu'au chemin (en nombre de voies ouvertes aux voitures)
     */
    private int insecurite;


    public Label(Node sommetNode) {
        this.sommet_courant = sommetNode;
        this.marque = false;
        this.cout = Double.POSITIVE_INFINITY;
        this.insecurite = Integer.MAX_VALUE;
        this.pereArc = null;
    }

    /**
     * Getter insecurite label
     * @return insecurite
     */
    public int getInsecurite() {
        return insecurite;
    }

    /**
     * Setter de l'insecurite du label
     * @param insecurite
     */
    public void setInsecurite(int insecurite) {
        this.insecurite = insecurite;
    }

    /**
     * Getter du cout du label
     * @return Cout du label
     */
    public double getCout() {
        return this.cout;
    }

    /**
     * @hidden
     * @return getCout
     */
    public double getTotalCout() {
        return this.getCout();
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
    public void setCout(double coutArg) {
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
        int comp = Integer.compare(this.insecurite, o.insecurite);
        if (comp == 0) {
            comp = Double.compare(this.getTotalCout(), o.getTotalCout());
        }
        return comp;
    }




}
