package Entity;


public class Node {
    /* attributes, e.g., {3C0101, wu'bei-yun'shan'zhi'jia, 0}
     */
    public String index;
    public String name;
    public NodeType type;
    public IdentifyOrRecover identifyOrRecover; // for testing
    public Node mutualNode = null;

    // unused attributes
    double longitude; //jing'du
    double latitude;  //wei'du

    //operations
    public Node getMutualNode() {
        return mutualNode;
    }

    //override
    public boolean equals(Object object) {
        if (!(object instanceof Node)) return false;
        Node node = (Node) object;
        return this.index != null && node.index != null && this.index.equals(node.index);
    }

}
