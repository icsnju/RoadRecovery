import nju.ics.Entity.Graph;
import nju.ics.Entity.Path;
import nju.ics.Entity.PathSet;
import nju.ics.Tool.ReadExcel;
import nju.ics.Algorithm.DPAlgorithm;
import nju.ics.Main.Main;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SmallTest {

    private int testBegin = 10048;
    private int testCount = 4;

    private static PrintWriter writer = null;
    private static final Graph graph;

    static {
        ReadExcel readExcel = new ReadExcel();
        graph = readExcel.buildGraph(Main.xlsFileName);
        try {
            writer = new PrintWriter("src/test/resources/test-data-calculated-tmp.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.println("index, path1, result");
    }

    @Test
    public void testItems() {

        for (int testIndex = testBegin; testIndex < testBegin + testCount; testIndex++) {
//            if (testIndex == 10037) continue;
            //read one path
            System.out.println("\nCase " + testIndex + ":");
            PathSet pathSet = new PathSet();
            pathSet.readAll2Path(graph, testIndex, writer, false,
                    "src/test/resources/test-data-10048-10051.txt", null);

            //execute algorithm
            nju.ics.Algorithm.DPAlgorithm algorithm = new DPAlgorithm();
            assert(pathSet.paths.size() == 1);
            pathSet.paths.get(0).print("原始路径");

            List<Double> configs = new ArrayList<>();
            configs.add(0.01);
            configs.add(0.1);
            configs.add(10.0);
            configs.add(10000.0);
            Path recoverPath = algorithm.execute(graph, pathSet.paths.get(0), configs);

            //print output
            if (recoverPath != null) {
                PathSet recoveredPathSet = new PathSet();
                recoveredPathSet.paths.add(recoverPath);
                recoveredPathSet.paths.get(0).print("算法恢复的路径");
                recoveredPathSet.compareAndPrint(testIndex, writer, pathSet, false);
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
