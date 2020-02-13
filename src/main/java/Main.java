import Algorithm.Algorithm;
import Algorithm.NullAlgorithm;
import Entity.Graph;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;

public class Main {

    public static void main(String[] args) {
        //get graph
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph();

        /*
         test for each case
         */
        for (int testIndex = 1; testIndex <= 5; testIndex++) {
            //get a broken path(s)
            PathSet pathSet = new PathSet();
            pathSet.readAllPath(testIndex);

            //TODO: execute the algorithm
            //@Fancy
            Algorithm algorithm = new NullAlgorithm();
            Path completePath = algorithm.execute(graph, pathSet);

            //print outputs
            completePath.compareAndPrint(pathSet.oraclePath, testIndex);
        }

    }
}
