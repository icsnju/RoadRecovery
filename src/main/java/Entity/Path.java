package Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path {

    public List<Node> nodeList = new ArrayList<Node>();
    String outDirectory = "/Users/lind/Desktop/RoadRecovery/src/main/resources/outputs/";

    public void compareAndPrint(Path oraclePath, int testIndex) {
        /*
         if equal, print "Successful recovery"
         else, print "Failed recovery"

         And print completed path for saving.
         */
        //compare two paths
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

        //save result
        //TODO: correctly output chinese characters
        try {
            String filename = outDirectory + testIndex + ".csv";
            File file = new File(filename);
            if (!file.exists()) file.createNewFile();
            Writer fileWriter =  new OutputStreamWriter(
                    new FileOutputStream(file), "utf-8");
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
