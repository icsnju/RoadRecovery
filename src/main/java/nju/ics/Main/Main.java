package nju.ics.Main;


import nju.ics.Entity.Graph;
import nju.ics.Entity.Path;
import nju.ics.Entity.PathSet;
import nju.ics.Tool.ReadExcel;
import nju.ics.Algorithm.Algorithm;
import nju.ics.Algorithm.DPAlgorithm;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

    private static int TestCasesBegin = 1;
    private static int TestCasesCount = (int) 10;

    public static String xlsFileName = "src/test/resources/basic-data.xls";

    public static void main(String[] args) throws FileNotFoundException {
        //get graph
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph(xlsFileName);

        /*
         test for each case
         */
        PrintWriter writer = new PrintWriter("src/test/resources/test-data-calculated-tmp.csv");
        writer.println("index, path1, path2, result");
        for (int testIndex = TestCasesBegin; testIndex < TestCasesBegin + TestCasesCount; testIndex++) {
            System.out.println("\nCase " + testIndex + ":");
            //get a broken path(s)
            PathSet pathSet = new PathSet();
//            pathSet.readAllPath(graph, testIndex);
            boolean success = true;
            if (pathSet.readAll2Path(graph, testIndex, writer, true,
                    "src/main/resources/test-data-10000-1.txt",
                    "src/test/resources/test-data-10000-2.txt")
            ) {
                //execute the algorithm
//                Algorithm algorithm = new NullAlgorithm();
                Algorithm algorithm = new DPAlgorithm();
                PathSet recoveredPathSet = new PathSet();
//                Path path = pathSet.paths.get(1);
                for (Path path : pathSet.paths) {
                    Path recoveredPath = algorithm.execute(graph, path, null);
                    if (recoveredPath == null) {
                        success = false;
                    } else {
//                        recoveredPath.print();
                        recoveredPathSet.paths.add(recoveredPath);
                    }
                }
                //print outputs
                if (success) {
                    recoveredPathSet.paths.get(0).print("算法恢复的路径1");
                    recoveredPathSet.paths.get(1).print("算法恢复的路径2");
                    recoveredPathSet.compareAndPrint(testIndex, writer, pathSet, true);
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
