package Entity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path {

    public List<Node> nodeList = new ArrayList<Node>();
    String outDirectory = "~/Desktop/RoadRecovery/src/main/resources/outputs/";

    public void compareAndPrint(Path oraclePath, int testIndex) {
        /*
         if equal, print "Successful recovery"
         else, print "Failed recovery"

         And print completed path for saving.
         */
        //TODO: compare two paths
        boolean successful = true;
        Iterator<Node> ourNodeIterator = nodeList.iterator();
        Iterator<Node> oracleIterator = oraclePath.nodeList.iterator();
        while (ourNodeIterator.hasNext()) {
            Node completedNode = ourNodeIterator.next();
            if (!oracleIterator.hasNext()) {
                successful = false;
                break;
            }
            Node oracleNode = oracleIterator.next();
            if (!completedNode.index.equals(oracleNode.index) ||
                !completedNode.name.equals(oracleNode.name) ||
                completedNode.identifyOrRecover!=oracleNode.identifyOrRecover) {
                successful = false;
                break;
            }
        }
        if (successful) {
            System.out.printf("Successful recovery!");
        }
        else {
            System.out.printf("Failed recovery!");
        }

        //TODO: save result
        try {
            FileWriter fileWriter = new FileWriter(
                    outDirectory + testIndex + ".csv");
            if (successful) fileWriter.write("successful\n");
            else fileWriter.write("failed\n");

            fileWriter.write("门架HEX/收费站编号,收费站/门架名称,门架来源," +
                    "门架HEX/收费站编号,收费站/门架名称,门架来源\n");
            ourNodeIterator = nodeList.iterator();
            oracleIterator = oraclePath.nodeList.iterator();
            while (ourNodeIterator.hasNext()) {
                Node completedNode = ourNodeIterator.next();
                fileWriter.write(completedNode.index+","+
                        completedNode.name+","+completedNode.identifyOrRecover);

                if (!oracleIterator.hasNext()) {
                    fileWriter.write("\n");
                    continue;
                }
                Node oracleNode = oracleIterator.next();
                fileWriter.write(","+oracleNode.index+","+
                        oracleNode.name+","+oracleNode.identifyOrRecover+"\n");

            }
            while (oracleIterator.hasNext()) {
                Node oracleNode = oracleIterator.next();
                fileWriter.write(" , , ,"+oracleNode.index+","+
                        oracleNode.name+","+oracleNode.identifyOrRecover+"\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
