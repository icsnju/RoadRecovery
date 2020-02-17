package Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
    //attributes
    public List<Node> nodes = new ArrayList<Node>();
    public Set<Edge> edgeSet = new HashSet<Edge>();
//    public Set<Edge> mutualSet = new HashSet<Edge>();
    public int[][] dist; // for floyd
    private ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>(); // for Dijkstra

    private static class NodeDijkstra {

        private int dis;
        private int index, pre_node;

        NodeDijkstra(int index, int dis, int pre_node) {
            this.index = index;
            this.dis = dis;
            this.pre_node = pre_node;
        }
    }

    //If inNode equals to outNode, then return only one node
    //If can not find path, then return null
    //TODO: recursively trace the complete shortest path
    public Path getShortestPath(Node inNode, Node outNode) {

        //Dijkstra algorithm
        int from = -1, to = -1;
        for (int i = 0; i < nodes.size(); ++i) {
            if (nodes.get(i).index.equals(inNode.index))
                from = i;
            if (nodes.get(i).index.equals(outNode.index))
                to = i;
        }
        int[] dis = new int[nodes.size()];
        int[] pre_node = new int[nodes.size()];
        Arrays.fill(dis, Integer.MAX_VALUE / 2);
        Arrays.fill(pre_node, -1);
        PriorityQueue<NodeDijkstra> q = new PriorityQueue<>(Comparator.comparingInt(x -> x.dis));

        q.add(new NodeDijkstra(from, 0, 0));
        while (!q.isEmpty()) {
            NodeDijkstra x = q.poll();
            if (dis[x.index] >= x.dis) {
                dis[x.index] = x.dis;
                pre_node[x.index] = x.pre_node;
                if (x.index == to) {
                    break;
                }
                for (int y : edges.get(x.index)) {
                    q.add(new NodeDijkstra(y, dis[y] = x.dis + 1, x.index));
                }
            }
        }

        if (dis[to] == Integer.MAX_VALUE / 2) {
            return null;
        }
        Path path = new Path();
        for (int x = to; x != from && x != -1; x = pre_node[x]) {
            path.nodeList.add(nodes.get(x));
        }
        path.nodeList.add(nodes.get(from));
        Collections.reverse(path.nodeList);
        assert (path.getLength() == dis[to]);
        return path;

        // floyd
        /*int inIndex = nodes.indexOf(inNode);
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
            //fix a bug
            if (inIndex != i && outIndex != i &&
                    dist[inIndex][outIndex] == dist[inIndex][i] + dist[i][outIndex]) {
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
        return partialPath;*/
    }

    //floyd algorithm
    public void buildAllShortestPathByFloyd() {
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

    public void buildAllShortestPathByDijkstra() {
        for (int i = 0; i < nodes.size(); ++i) {
            System.out.println(nodes.get(i).index + nodes.get(i).name);
            edges.add(new ArrayList<Integer>());
        }
        for (Edge edge : edgeSet) {
            edges.get(nodes.indexOf(edge.inNode)).add(nodes.indexOf(edge.outNode));
        }
    }
}
