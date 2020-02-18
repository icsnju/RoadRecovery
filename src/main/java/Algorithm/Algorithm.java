package Algorithm;

import Entity.Graph;
import Entity.Path;
import Entity.PathSet;

public interface Algorithm {

    public Path execute(Graph graph, Path path);
}
