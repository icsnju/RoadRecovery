package Entity;

public class Edge {
    public Node inNode;
    public Node outNode;

    //override
    public boolean equals(Edge edge) {
        if (inNode == null || outNode == null || edge.inNode == null || edge.outNode == null)
            return false;
        return inNode.equals(edge.inNode) && outNode.equals(edge.outNode);
    }
}
