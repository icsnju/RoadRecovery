import Algorithm.*;
import Entity.Graph;
import Entity.Path;
import Entity.PathSet;
import Tool.ReadExcel;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SmallTest {

    @Test
    public void test32Items() throws FileNotFoundException {
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph();
        PrintWriter writer = new PrintWriter("src/main/resources/test-data-calculated-tmp.csv");
        writer.println("index, path1, result");

        for (int testIndex = 10001; testIndex <= 10010; testIndex++) {
            //read one path
            System.out.println("\nCase " + testIndex + ":");
            PathSet pathSet = new PathSet();
            pathSet.readAll2Path(graph, testIndex, writer, false);

            //execute algorithm
            DPAlgorithm algorithm = new DPAlgorithm();
            pathSet.paths.get(0).print();
            assert(pathSet.paths.size() == 1);
            Path recoverPath = algorithm.execute(graph, pathSet.paths.get(0));


            //print output
            if (recoverPath != null) {
                PathSet recoveredPathSet = new PathSet();
                recoveredPathSet.paths.add(recoverPath);
                recoveredPathSet.paths.get(0).print();
                recoveredPathSet.compareAndPrint(graph, testIndex, writer, pathSet, false);
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
