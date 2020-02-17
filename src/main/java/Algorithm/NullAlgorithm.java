package Algorithm;

import Entity.Graph;
import Entity.Path;
import Entity.PathSet;

public class NullAlgorithm implements Algorithm {
    public static int cnt = 0;
    public Path execute(Graph graph, PathSet paths) {
        //do nothing but return oracle path
        return paths.paths.get(cnt++%2);
    }
}