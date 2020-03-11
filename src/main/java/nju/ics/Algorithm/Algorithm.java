package nju.ics.Algorithm;

import nju.ics.Entity.Graph;
import nju.ics.Entity.Path;

import java.util.List;

public interface Algorithm {

    public Path execute(Graph graph, Path path, List<Double> configs);
}
