package Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
    //attributes
    public List<Node> nodes = new ArrayList<Node>();
    public Set<Edge> edgeSet = new HashSet<Edge>();
//    public Set<Edge> mutualSet = new HashSet<Edge>();
    public int[][] dist;

    //If inNode equals to outNode, then return only one node
    //If can not find path, then return null
    //TODO: recursively trace the complete shortest path
    public Path getShortestPath(Node inNode, Node outNode) {
        int inIndex = nodes.indexOf(inNode);
        int outIndex = nodes.indexOf(outNode);
        Path partialPath = new Path();
        //recursion terminal condition
        if (inIndex == outIndex) {
            partialPath.nodeList.add(nodes.get(inIndex));
            partialPath.connected = true;
            return partialPath;
        }
        //if two nodes are disconnected, return {connected = false, nodelist = empty}
        if (dist[inIndex][outIndex] >= nodes.size()) return partialPath;
        for (int i = 0; i < nodes.size(); i++) {
            if (dist[inIndex][outIndex] == dist[inIndex][i] + dist[i][outIndex]) {
                Path leftPath = getShortestPath(nodes.get(inIndex), nodes.get(i));
                Path rightPath = getShortestPath(nodes.get(i), nodes.get(outIndex));
                //NOTE: leftPath and rightPath will duplicate the middle node twice.
                partialPath.nodeList = new ArrayList<>(leftPath.nodeList);
                for (int j = 1; j < rightPath.nodeList.size(); j++) {
                    partialPath.nodeList.add(rightPath.nodeList.get(j));
                }
                partialPath.connected = true;
                break;
            }
        }
        return partialPath;
    }

    //floyd algorithm
    public void buildAllShortestPath() {
        int num = nodes.size();
        dist = new int[num+5][num+5];

        //init
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                //num is INF.
                dist[i][j] = num;
                if (i == j) dist[i][j] = 0;
            }
        }
        for (Edge edge: edgeSet
             ) {
            int inIndex = nodes.indexOf(edge.inNode);
            int outIndex = nodes.indexOf(edge.outNode);
            dist[inIndex][outIndex] = 1;
        }

        //update dist and distPath
        System.out.println("NOTE");
        for (int k = 0; k < num; k++) {
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                   if (dist[i][j] > dist[i][k] + dist[k][j] && dist[i][k] + dist[k][j] < num) {
                       dist[i][j] = dist[i][k] + dist[k][j];
                   }
                }
            }
        }
    }

    private List<Node> listConcat(List<Node> nodeList1, List<Node> nodeList2) {
        return Stream.concat(nodeList1.stream(), nodeList2.stream()).collect(Collectors.toList());
    }
}
