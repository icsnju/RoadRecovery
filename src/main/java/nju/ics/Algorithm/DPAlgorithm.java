package nju.ics.Algorithm;

import static java.lang.Math.max;
import static java.lang.Math.pow;
import static nju.ics.Entity.NodeSource.*;

import java.util.ArrayList;
import java.util.Arrays;
import nju.ics.Entity.Graph;
import nju.ics.Entity.Node;
import nju.ics.Entity.NodeType;
import nju.ics.Entity.Path;

import java.util.List;

public class DPAlgorithm implements Algorithm {

    /**
     * Road recovery DP algorithm
     * @param graph graph(G(V, E), T(V))
     * @param path input path P
     *
     * @return output path(P*), return null when failed
     */
    public Path execute(Graph graph, Path path, List<Double> configs) {

        double modifyCost = configs.get(0); //0.01
        double addCost = 0.2; //configs.get(1); //0.1
        double deleteCost = 1.5; //configs.get(2); //1
        double deleteCost2 = deleteCost * 2;
        double deleteEndCost = configs.get(3); //graph.nodes.size() + 1
        // 保证5个加<1个删 (id9710)

        boolean debug = false;

        Path originalPath = path;
//        Path originalPath = new Path();
//        for (Node node : path.nodeList) {
//            if (originalPath.nodeList.isEmpty() || node != originalPath.nodeList
//                .get(originalPath.nodeList.size() - 1)) {
//                originalPath.nodeList.add(node);
//            }
//        }
        int originalPathLength = originalPath.getLength();
        double[][] dp = new double[originalPathLength + 1][2];
        Path[][] dpPath = new Path[originalPathLength + 1][2];
        double[][] distanceFromDeletedNodesToIJ = new double[originalPathLength + 1][
            originalPathLength + 1];

        double answer = -1;
        Path answerPath = new Path();

        boolean not_delete_first = originalPath.nodeList.get(0).type == NodeType.TOLLSTATION;
        boolean not_delete_last =
            originalPath.nodeList.get(originalPathLength).type == NodeType.TOLLSTATION;

        ArrayList<Node> reasonableNodeList = new ArrayList<Node>();

        for (int i = 0; i < originalPathLength; ++i) {
            Path path1 = graph.getShortestPath(originalPath.nodeList.get(i), originalPath.nodeList.get(i + 1));
            if (path1 != null) reasonableNodeList.addAll(path1.nodeList);
        }

        for (int i = 0; i <= originalPathLength; ++i) {
            for (int j = i; j <= originalPathLength; ++j) {
                distanceFromDeletedNodesToIJ[i][j] =
                    j - i <= 1 ? 0 : distanceFromNodesToNodes(graph, originalPath.nodeList, i, j);
                if (debug) {
                    System.out.println("distance delete " + i + " " + j + ": "
                        + distanceFromDeletedNodesToIJ[i][j]);
                }
            }
        }

        for (int i = 0; i <= originalPathLength; ++i) {
            for (int flagI = 0; flagI <= 1; ++flagI) {
                // initial dp array
                dp[i][flagI] = (i == 0) ? (modifyCost * flagI) : -1;
                dpPath[i][flagI] = new Path();
                // nodeI: v_i or T(v_i) controlled by nodeI
                Node nodeI = flagI == 0 ? originalPath.nodeList.get(i)
                    : originalPath.nodeList.get(i).getMutualNode();
                if (nodeI == null) {
                    continue;
                }
                dpPath[i][flagI].nodeList.add((Node) nodeI.clone());
                dpPath[i][flagI].nodeList.get(0).source = IDENTIFY;
                for (int flagJ = 0; flagJ <= 1; ++flagJ) {
                    for (int j = i - 1; j >= 0; --j) {
                        // nodeJ: v_j or T(v_j) controlled by flagJ
                        Node nodeJ = flagJ == 0 ? originalPath.nodeList.get(j)
                            : originalPath.nodeList.get(j).getMutualNode();
                        if (nodeJ == null) {
                            continue;
                        }
                        // shortest path from nodeJ to nodeI
                        Path shortestPath = graph.getShortestPath(nodeJ, nodeI);
                        if (shortestPath == null) {
                            continue;
                        }
                        if (shortestPath.getLength() > 0 || (flagI == 1 && flagJ == 1)) { // When i == j and has one IDENTIFY, then IDENTIFY
                            if (flagJ == 1) { // 反转结点
                                shortestPath.nodeList.get(0).source = MODIFY;
                            }
                            if (flagI == 1) { // 反转结点
                                shortestPath.nodeList
                                    .get(shortestPath.nodeList.size() - 1).source = MODIFY;
                            }
                        }
                        // FIXME: find suitable cost
                        double distance = max(shortestPath.getLength() - 1, 0);

                        // update method 1: 从 (j, flagJ) 转移到 (i, flagI)，删除 j+1 到 i-1 之间的门架，补上 j 到 i 的最短路
                        if (dp[j][flagJ] != -1) {
                            double result = dp[j][flagJ]
                                + modifyCost * flagI
                                + deleteCost * pow(i - j - 1, 1.2)
                                + deleteCost2 * distanceFromDeletedNodesToIJ[j][i]
                                + addCost * pow(distance, 1.4);
//                                + addCost2 * unreasonableNodes(reasonableNodeList, shortestPath);
                            // update
                            if (dp[i][flagI] == -1 || result <= dp[i][flagI]) {
                                dp[i][flagI] = result;
                                dpPath[i][flagI].nodeList.clear();
                                dpPath[i][flagI].add(dpPath[j][flagJ]);
//                            if (flagJ == 1 && nodeI.equals(nodeJ)) {
//                                dpPath[i][flagI].nodeList
//                                    .remove(dpPath[i][flagI].nodeList.size() - 1);
//                            }
                                dpPath[i][flagI].add(shortestPath);
                                if (debug) {
                                    dpPath[i][flagI].print(
                                        "dp[" + i + "][" + flagI + "]: " + dp[i][flagI]
                                            + " (from dp[" + j + "][" + flagJ + "])");
                                }
                            }
                        }
                        // update method 2: delete node 0 to j-1, j+1 to i-1
                        if (!not_delete_first) {
                            double result = modifyCost * (flagJ + flagI)
                                + deleteEndCost * j
                                + deleteCost * pow(i - j - 1, 1.2)
                                + deleteCost2 * distanceFromDeletedNodesToIJ[j][i]
                                + addCost * pow(distance, 1.4);
                            if (dp[i][flagI] == -1 || result <= dp[i][flagI]) {
                                dp[i][flagI] = result;
                                dpPath[i][flagI].nodeList.clear();
                                dpPath[i][flagI].add(shortestPath);
                                if (debug) {
                                    dpPath[i][flagI].print(
                                        "dp[" + i + "][" + flagI + "]: " + dp[i][flagI]
                                            + " (from node[" + j + "][" + flagJ + "])");
                                }
                            }
                        }
                    }
                }
                if ((!not_delete_last || i == originalPathLength) && (answer == -1
                    || dp[i][flagI] + (originalPathLength - i) * deleteEndCost < answer)) {
                    answer = dp[i][flagI] + (originalPathLength - i) * deleteEndCost; // 相当于后面都删掉
                    answerPath = dpPath[i][flagI];
                }
            }
        }
        return answerPath; // empty when failed
    }

    private int distanceFromNodesToNodes(Graph graph, List<Node> nodeList, int i, int j) {
        int ret = 0;
        Node nodeI = nodeList.get(i), nodeJ = nodeList.get(j);
        for (int k = i + 1; k < j; ++k) {
            Node nodeK = nodeList.get(k);
            Path path = graph.getShortestPath(nodeI, nodeK);
            int dis = -1;
            if (path != null && (dis == -1 || path.getLength() < dis)) dis = path.getLength();
            path = graph.getShortestPath(nodeK, nodeJ);
            if (path != null && (dis == -1 || path.getLength() < dis)) dis = path.getLength();
            if (nodeK.getMutualNode() != null) {
                nodeK = nodeK.getMutualNode();
                path = graph.getShortestPath(nodeI, nodeK);
                if (path != null && (dis == -1 || path.getLength() < dis)) {
                    dis = path.getLength();
                }
                path = graph.getShortestPath(nodeK, nodeJ);
                if (path != null && (dis == -1 || path.getLength() < dis)) {
                    dis = path.getLength();
                }
            }
            ret += max(dis - 1, 0);
        }
        return ret / (j - i - 1);

//        int ret = -1;
//        for (Node node1 : list1) {
//            for (Node node2 : list2) {
//                Path path = graph.getShortestPath(node1, node2);
//                if (path != null && (ret == -1 || path.getLength() < ret)) {
//                    ret = path.getLength();
//                }
//                path = graph.getShortestPath(node2, node1);
//                if (path != null && (ret == -1 || path.getLength() < ret)) {
//                    ret = path.getLength();
//                }
//            }
//        }
//        return max(ret - 1, 0);
    }

    private int unreasonableNodes(List<Node> reasonableNodeList, Path path) {
        int ret = 0;
        for (Node node: path.nodeList) {
            if (!reasonableNodeList.contains(node)) ret++;
        }
        return ret;
    }
}
