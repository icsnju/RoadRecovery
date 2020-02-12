import Entity.Graph;
import Entity.Node;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //TODO: get graph
        Graph graph = new Graph();
        ReadExcel readExcel = new ReadExcel();
        readExcel.buildGraph();

        //TODO: get a broken path(s)
        PathSet pathSet = new PathSet();
        pathSet.readAllPath();

        //TODO: execute the algorithm
        Algorithm algorithm = new Algorithm();
        Path completePath = algorithm.execute(graph, pathSet);

        //TODO: print outputs
        completePath.print();
    }
}
