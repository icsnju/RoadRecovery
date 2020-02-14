package Algorithm;

import Entity.Graph;
import Entity.Node;
import Entity.Path;
import Entity.PathSet;

public class DPAlgorithm implements Algorithm {

    /**
     * Road recovery DP algorithm
     * @param graph graph(G(V, E), T(V))
     * @param paths input paths(P_1, P_2, ...)
     * @return output path(P*), return null when failed
     */
    public Path execute(Graph graph, PathSet paths) {
        double alpha = 0.01;
        double beta = 0.01;
        double gamma = 1;
        double delta = 1 / gamma;
        double inf = 1e9;

        Path originalPath = paths.paths.get(0);
        int originalPathLength = originalPath.getLength();
        double[][] dp = new double[originalPathLength + 1][2];
        Path[][] dpPath = new Path[originalPathLength + 1][2];

        for (int i = 0; i <= originalPathLength; ++i) {
            for (int flagI = 0; flagI <= 1; ++flagI) {
                // initial dp array
                dp[i][flagI] = i == 0 ? 0 : inf;
                dpPath[i][flagI] = new Path();
                // nodeI: v_i or T(v_i) controlled by nodeI
                Node nodeI = flagI == 0 ? originalPath.nodeList.get(i)
                    : originalPath.nodeList.get(i).getMutualNode();
                if (nodeI == null) {
                    continue;
                }
                dpPath[i][flagI].nodeList.add(nodeI);
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
                        // TODO: find suitable cost
                        double distance = shortestPath.getLength() - 1;
                        // calculate the new cost for nodeI
                        double result = dp[j][flagJ]
                            // transformation 1: mutual node, cost alpha
                            + alpha * flagI
                            // transformation 2: delete same node i & j, cost beta
                            // TODO: 冗余结点一定去除吗？
                            + beta * (nodeI.index.equals(nodeJ.index) ? 1 : 0)
                            // transformation 3: delete node j+1 to i-1, cost gamma
                            + gamma * (i - j - 1)
                            // transformation 4: complement path, cost delta * distance
                            + delta * distance;
                        // update
                        if (result < dp[i][flagI]) {
                            dp[i][flagI] = result;
                            dpPath[i][flagI] = Path.add(dpPath[j][flagJ], shortestPath);
                        }
                    }
                }
            }
        }
        if (Double.min(dp[originalPathLength][0], dp[originalPathLength][1]) >= 1e9 - 1) { // failed
            return null;
        }
        if (dp[originalPathLength][0] < dp[originalPathLength][1]) {
            return dpPath[originalPathLength][0];
        } else {
            return dpPath[originalPathLength][1];
        }
    }
}
