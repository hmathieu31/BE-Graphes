package org.insa.graphs.model;

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


    public Label(Node sommetNode) {
        this.sommet_courant = sommetNode;
        this.marque = false;
        this.cout = Double.POSITIVE_INFINITY;
        this.pereArc = null;
    }

    /**
     * Getter du cout du label
     * @return Cout du label
     */
    public double getCout() {
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
        return Double.compare(getCout(), o.getCout());
    }




}
