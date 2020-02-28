import Algorithm.*;
import Entity.Graph;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SmallTest {

    private int testBegin = 10018;
    private int testCount = 1;

    @Test
    public void test32Items() throws FileNotFoundException {
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph(Main.xlsFileName);
        PrintWriter writer = new PrintWriter("src/test/resources/test-data-calculated-tmp.csv");
        writer.println("index, path1, result");

        for (int testIndex = testBegin; testIndex < testBegin + testCount; testIndex++) {
            //read one path
            System.out.println("\nCase " + testIndex + ":");
            PathSet pathSet = new PathSet();
            pathSet.readAll2Path(graph, testIndex, writer, false);

            //execute algorithm
            DPAlgorithm algorithm = new DPAlgorithm();
            pathSet.paths.get(0).print("原始路径");
            assert(pathSet.paths.size() == 1);
            Path recoverPath = algorithm.execute(graph, pathSet.paths.get(0));


            //print output
            if (recoverPath != null) {
                PathSet recoveredPathSet = new PathSet();
                recoveredPathSet.paths.add(recoverPath);
                recoveredPathSet.paths.get(0).print("算法恢复的路径");
                recoveredPathSet.compareAndPrint(graph, testIndex, writer, pathSet, false);
                recoveredPathSet.finalPathInCard.print("合并的路径");
            }
            else {
                System.err.println("recoverPath is null");
                System.exit(1);
            }

            writer.println();
        }
        writer.close();
    }
}
