package Entity;

import java.util.Set;
import java.util.HashSet;

public class Graph {
    //attributes
    public Set<Node> nodeSet = new HashSet<Node>();
    public Set<Edge> edgeSet = new HashSet<Edge>();
    public Set<Edge> mutualSet = new HashSet<Edge>();


    //operations
    //TODO:
    //If inNode equals to outNode, then return only one node
    //If can not find path, then return null
    public Path getShortestPath(Node inNode, Node outNode) {
        return null;
    }


}
