import Algorithm.DPAlgorithm;
import Entity.Graph;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;


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
        //@Fancy
        DPAlgorithm algorithm = new DPAlgorithm();
        Path completePath = algorithm.execute(graph, pathSet);

        //TODO: print outputs
        completePath.print();
    }
}
