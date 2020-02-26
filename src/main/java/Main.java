import Algorithm.*;
import Entity.Graph;
import Entity.Node;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Main {

    private static int TestCasesBegin = 23;
    private static int TestCasesCount = (int) 1;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        //get graph
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph();

        /*
         test for each case
         */
        PrintWriter writer = new PrintWriter("src/main/resources/test-data-calculated-tmp.csv");
        writer.println("index, path1, path2, result");
        for (int testIndex = TestCasesBegin; testIndex < TestCasesBegin + TestCasesCount; testIndex++) {
            System.out.println("\nCase " + testIndex + ":");
            //get a broken path(s)
            PathSet pathSet = new PathSet();
//            pathSet.readAllPath(graph, testIndex);
            boolean success = true;
            if (pathSet.readAll2Path(graph, testIndex, writer)) {
                //execute the algorithm
//                Algorithm algorithm = new NullAlgorithm();
                Algorithm algorithm = new DPAlgorithm();
                PathSet recoveredPathSet = new PathSet();
//                Path path = pathSet.paths.get(1);
                for (Path path : pathSet.paths) {
                    Path recoveredPath = algorithm.execute(graph, path);
                    if (recoveredPath == null) {
                        success = false;
                    } else {
//                        recoveredPath.print();
                        recoveredPathSet.paths.add(recoveredPath);
                    }
                }
                //print outputs
                if (success) {
                    recoveredPathSet.paths.get(0).print();
                    recoveredPathSet.paths.get(1).print();
                    recoveredPathSet.compareAndPrint(graph, testIndex, writer, pathSet);
                } else {
                    // example:
                    // case 5369, 5544: 出省
                    System.out.println("Failure: Recover failed.");
                    writer.print("Failure: Recover failed.");
//                    return;
                }
            } else {
                System.out.println("Failure: Node not found.");
                writer.print("Failure: Node not found.");
            }
            writer.println();
        }

        writer.close();

    }
}
