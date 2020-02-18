import Algorithm.*;
import Entity.Graph;
import Entity.Node;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;

public class Main {

    private static int TestCases = 5;

    public static void main(String[] args) {
        //get graph
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph();

        /*
         test for each case
         */
        for (int testIndex = 1; testIndex <= TestCases; testIndex++) {
            //get a broken path(s)
            PathSet pathSet = new PathSet();
            pathSet.readAllPath(graph, testIndex);

            //execute the algorithm
//            Algorithm algorithm = new NullAlgorithm();
            Algorithm algorithm = new DPAlgorithm();
            for (Path path : pathSet.paths) {
                Path completePath = algorithm.execute(graph, path);
                completePath.print();
            }

            //print outputs
            // TODO: compare
//            completePath.compareAndPrint(pathSet.oraclePath, testIndex);
        }

    }
}
