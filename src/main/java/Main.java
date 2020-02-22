import Algorithm.*;
import Entity.Graph;
import Entity.Node;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;

public class Main {

    private static int TestCasesBegin = 1;
    private static int TestCasesCount = 100;

    public static void main(String[] args) {
        //get graph
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph();

        /*
         test for each case
         */
        for (int testIndex = TestCasesBegin; testIndex < TestCasesBegin + TestCasesCount; testIndex++) {
            System.out.println("Case " + testIndex + ":");
            //get a broken path(s)
            PathSet pathSet = new PathSet();
//            pathSet.readAllPath(graph, testIndex);
            boolean success = true;
            if (pathSet.readAll2Path(graph, testIndex)) {
                //execute the algorithm
//            Algorithm algorithm = new NullAlgorithm();
                Algorithm algorithm = new DPAlgorithm();
                PathSet recoveredPathSet = new PathSet();
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
                    recoveredPathSet.compareAndPrint(graph, pathSet.oraclePath, testIndex);
                } else {
                    // example:
                    // case 5186: 3C7906 与自己互为对向
                    // case 5369, 5544: 出省
                    System.out.println("Failure: Recover failed.");
                    return;
                }
            } else {
                System.out.println("Failure: Node not found.");
            }
        }

    }
}
