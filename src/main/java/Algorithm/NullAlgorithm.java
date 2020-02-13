package Algorithm;

import Entity.Graph;
import Entity.Path;
import Entity.PathSet;

public class NullAlgorithm implements Algorithm {

    public Path execute(Graph graph, PathSet paths) {
        //do nothing but return oracle path
        return paths.oraclePath;
    }
}
