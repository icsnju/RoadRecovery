package nju.ics.Algorithm;

import static nju.ics.Entity.NodeSource.*;

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
        double addCost = configs.get(1); //0.1
        double deleteCost = configs.get(2); //10
        double deleteEndCost = configs.get(3); //graph.nodes.size() + 10

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

        double answer = -1;
        Path answerPath = new Path();

        boolean not_delete_first = originalPath.nodeList.get(0).type == NodeType.TOLLSTATION;
        boolean not_delete_last =
            originalPath.nodeList.get(originalPathLength).type == NodeType.TOLLSTATION;

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
                        double distance = Double.max(shortestPath.getLength() - 1, 0);
                        // update method 1: delete node j+1 to i-1
                        if (dp[j][flagJ] != -1) {
                            double result = dp[j][flagJ]
                                // transformation 1: mutual node, cost alpha
                                + modifyCost * flagI
                                // transformation 3: delete node j+1 to i-1, cost gamma
                                + deleteCost * (i - j - 1)
                                // transformation 4: complement path, cost delta * distance
                                + addCost * distance;
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
                            double result = modifyCost * (flagJ + flagI) + deleteEndCost * j + deleteCost * (i - j - 1)
                                + addCost * distance;
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
}
