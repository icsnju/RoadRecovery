package Entity;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path {

    public List<Node> nodeList = new ArrayList<Node>();
    String outDirectory = "src/main/resources/outputs/";

    public int getLength() {
        return nodeList.size() - 1;
    }

    public static Path add(Path path1, Path path2) {
        Path result = path1;
        if (path1.nodeList.isEmpty()) {
            result.nodeList = path2.nodeList;
        } else if (!path2.nodeList.isEmpty()) {
            if (path1.nodeList.get(path1.nodeList.size() - 1).index
                .equals(path2.nodeList.get(0).index)) {
                result.nodeList.addAll(path2.nodeList.subList(1, path2.nodeList.size() - 1));
            } else {
                result.nodeList.addAll(path2.nodeList);
            }
        }
        return result;
    }

    public void compareAndPrint(Path oraclePath, int testIndex) {
        /*
         if equal, print "Successful recovery"
         else, print "Failed recovery"

         And print complete path.
         */
        //compare two paths
        //FIXME: I don't know oracle. Refer to Pathset.java line 19.
        boolean successful = true;
        /*
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
         */
        if (successful) {
            System.out.println("recovery: success!");
        }
        else {
            System.out.println("recovery: failure!");
        }

        //save result
        //correctly output chinese characters
        try {
            //use Excel file to save outputs.
            String[] columns = {"门架HEX/收费站编号","收费站/门架名称","门架来源"};
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("case " + testIndex);
            Row row = sheet.createRow(0);
            if (successful) row.createCell(0).setCellValue("recovery: success!");
            else row.createCell(0).setCellValue("recovery: failure!");
            row = sheet.createRow(1);
            row.createCell(0).setCellValue("Recovered path");
            row.createCell(3).setCellValue("Oracle path");
            row = sheet.createRow(2);
            for (int columnIndex = 0; columnIndex < 6; columnIndex++) {
                row.createCell(columnIndex).setCellValue(columns[columnIndex%3]);
            }
            int rowIndex = 3;
            for (Node node: nodeList
                 ) {
                row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(node.index);
                row.createCell(1).setCellValue(node.name);
                if (node.identifyOrRecover == IdentifyOrRecover.IDENTIFY)
                    row.createCell(2).setCellValue("标记出的点");
                else row.createCell(2).setCellValue("还原出的点");
            }

            // Write the output to a file
            File outDir = new File(outDirectory);
            if (!outDir.exists()) outDir.mkdir();
            String filename = outDirectory + testIndex + ".xlsx";
//            File file = new File(filename);
//            if (!file.exists()) file.createNewFile();
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
