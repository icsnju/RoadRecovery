package Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
    //attributes
    public List<Node> nodeSet = new ArrayList<Node>();
    public Set<Edge> edgeSet = new HashSet<Edge>();
//    public Set<Edge> mutualSet = new HashSet<Edge>();
    public int[][] dist;
    public Path[][] distPath;

    //If inNode equals to outNode, then return only one node
    //If can not find path, then return null
    public Path getShortestPath(Node inNode, Node outNode) {
        int inIndex = nodeSet.indexOf(inNode);
        int outIndex = nodeSet.indexOf(outNode);
        return distPath[inIndex][outIndex];
    }

    //floyd algorithm
    public void buildAllShortestPath() {
        int num = nodeSet.size();
        dist = new int[num+5][num+5];
        distPath = new Path[num+5][num+5];

        //init
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                //num is INF.
                dist[i][j] = num;
                if (i == j) {
                    dist[i][j] = 0;
                    distPath[i][j].nodeList.add(nodeSet.get(i));
                }
            }
        }
        for (Edge edge: edgeSet
             ) {
            int inIndex = nodeSet.indexOf(edge.inNode);
            int outIndex = nodeSet.indexOf(edge.outNode);
            dist[inIndex][outIndex] = 1;
            distPath[inIndex][outIndex].nodeList = listConcat(
                    distPath[inIndex][inIndex].nodeList, distPath[outIndex][outIndex].nodeList);
        }

        //update dist and distPath
        for (int k = 0; k < num; k++) {
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                   if (dist[i][j] > dist[i][k] + dist[k][j] && dist[i][k] + dist[k][j] < num) {
                       dist[i][j] = dist[i][k] + dist[k][j];
                       distPath[i][j].nodeList = listConcat(distPath[i][k].nodeList, distPath[k][j].nodeList);
                   }
                }
            }
        }
    }

    private List<Node> listConcat(List<Node> nodeList1, List<Node> nodeList2) {
        return Stream.concat(nodeList1.stream(), nodeList2.stream()).collect(Collectors.toList());
    }
}
