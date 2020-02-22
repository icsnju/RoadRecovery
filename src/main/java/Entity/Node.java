package Entity;


import java.sql.Timestamp;

public class Node implements Cloneable {
    /* attributes, e.g., {3C0101, wu'bei-yun'shan'zhi'jia, 0}
     */
    public String index;
    public String name;
    public NodeType type;
    public NodeSource source; // for testing
    public Node mutualNode = null;

    // unused attributes
    double longitude; //jing'du
    double latitude;  //wei'du
    Timestamp timestamp; //

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

    @Override
    public Object clone() {
        Node stu = null;
        try {
            stu = (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }

    public void print() {
        System.out.println(index + " " + name + " " + type);
    }
}

