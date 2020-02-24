package Algorithm;

import static Entity.NodeSource.*;

import Entity.Graph;
import Entity.Node;
import Entity.Path;

public class DPAlgorithm implements Algorithm {

    /**
     * Road recovery DP algorithm
     * @param graph graph(G(V, E), T(V))
     * @param path input path P
     * @return output path(P*), return null when failed
     */
    public Path execute(Graph graph, Path path) {
        double alpha = 0.01;
        double beta = 5; // delete side node
        double gamma = 1; // delete mid node
        double delta = 1 / gamma;
        double inf = 1e9;

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

        double answer = inf;
        Path answerPath = null;

        for (int i = 0; i <= originalPathLength; ++i) {
            for (int flagI = 0; flagI <= 1; ++flagI) {
                // initial dp array
                dp[i][flagI] = (i == 0) ? (alpha * flagI) : inf;
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
                        } else if (flagI == 1) { // 反转结点
                            shortestPath.nodeList
                                .get(shortestPath.nodeList.size() - 1).source = MODIFY;
                        }
                        // FIXME: find suitable cost
                        double distance = Double.max(shortestPath.getLength() - 1, 0);
                        // update method 1: delete node i+1 to j-1
                        double result = dp[j][flagJ]
                            // transformation 1: mutual node, cost alpha
                            + alpha * flagI
                            // transformation 3: delete node j+1 to i-1, cost gamma
                            + gamma * (i - j - 1)
                            // transformation 4: complement path, cost delta * distance
                            + delta * distance;
                        // update
                        if (result <= dp[i][flagI]) {
                            dp[i][flagI] = result;
                            dpPath[i][flagI].nodeList.clear();
                            dpPath[i][flagI].add(dpPath[j][flagJ]);
                            dpPath[i][flagI].add(shortestPath);
//                            System.out.println("dp[" + i + "][" + flagI + "]: " + dp[i][flagI]);
//                            dpPath[i][flagI].print();
                        }
                        // update method 2: delete node 0 to i-1, i+1 to j-1
                        result = alpha * flagI + beta * j + gamma * (i - j - 1) + delta * distance;
                        if (result <= dp[i][flagI]) {
                            dp[i][flagI] = result;
                            dpPath[i][flagI].nodeList.clear();
                            dpPath[i][flagI].add(shortestPath);
//                            System.out.println("dp[" + i + "][" + flagI + "]: " + dp[i][flagI]);
//                            dpPath[i][flagI].print();
                        }
                    }
                }
                if (dp[i][flagI] + (originalPathLength - i) * beta < answer) {
                    answer = dp[i][flagI] + (originalPathLength - i) * beta; // 相当于后面都删掉
                    answerPath = dpPath[i][flagI];
                }
            }
        }
        return answerPath; // null when failed
    }
}
