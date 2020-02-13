package Entity;


public class Node {
    /* attributes, e.g., {3C0101, wu'bei-yun'shan'zhi'jia, 0}
     */
    public String index;
    public String name;
    public NodeType type;
    public IdentifyOrRecover identifyOrRecover; // for testing

    // unused attributes
    double longitude; //jing'du
    double latitude;  //wei'du

    //operations
    public Node getMutualNode() {
        //TODO:
        return null;
    }



}

