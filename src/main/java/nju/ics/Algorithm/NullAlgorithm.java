package nju.ics.Algorithm;

import nju.ics.Entity.Graph;
import nju.ics.Entity.Path;

import java.util.List;

public class NullAlgorithm implements Algorithm {
    public static int cnt = 0;
    public Path execute(Graph graph, Path path, List<Double> configs) {
        //do nothing but return oracle path
        return path;
    }
}
