package Entity;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PathSet {

    public List<Path> paths = new ArrayList<Path>();
    public Path finalPath;
    public Path oraclePath = new Path();
    String testFileName = "src/main/resources/test-data.xls";
    String testFileName1 = "src/main/resources/test-data-10000-1.txt";
    String testFileName2 = "src/main/resources/test-data-10000-2.txt";
    int SheetHead = 3;
    String outDirectory = "src/main/resources/outputs/";

    /*
    I don't where is test oracle.
    read two kinds of paths, combined into a path set.
     */
    public void readAllPath(Graph graph, int testIndex) {
        System.out.println("testIndex=" + testIndex);
        try {
            /*
            get steam info and test oracle.
             */
            Workbook workbook = WorkbookFactory.create(new File(testFileName));

            //get steam info
            Sheet sheet = workbook.getSheetAt(testIndex*2-1);
            extractOnePath(graph, sheet, 0); // first path:  simplified path
            extractOnePath(graph, sheet, 2); // second path: complete path

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void extractOnePath(Graph graph, Sheet sheet, int columnBase) {
        Path path = new Path();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < SheetHead) continue;
//            System.out.println(row.getRowNum()+1);
            if (row.getCell(columnBase) == null || row.getCell(columnBase+1) == null) continue;
            Node node = new Node();
            node.index = row.getCell(columnBase).getStringCellValue();
            node.name = row.getCell(columnBase+1).getStringCellValue();
            //avoid to read empty cells
            if (node.index.length() !=6 && node.index.length() != 14) continue;
//            System.out.println(node.index+", "+node.name);
            //node.type can be completed with graph information
            path.nodeList.add(graph.nodes.get(graph.nodes.indexOf(node))); // node's id can be different from the node in graph
        }
        paths.add(path); //testcase has only one path.
    }

    public boolean readAll2Path(Graph graph, int testIndex, PrintWriter writer) {
        PathType flag1 = extractOnePath2(graph, testIndex, testFileName1);
        PathType flag2 = extractOnePath2(graph, testIndex, testFileName2);
        writer.print(testIndex + ", ");

        if (flag1 == PathType.Normal) writer.print("0, ");
        else if (flag1 == PathType.Missing) writer.print("1, ");
        else if (flag1 == PathType.UnknownNode) writer.print("2, ");

        if (flag2 == PathType.Normal) writer.print("0");
        else if (flag2 == PathType.Missing) writer.print("1");
        else if (flag2 == PathType.UnknownNode) writer.print("2");
        writer.println();

        return flag1 == PathType.Normal && flag2 == PathType.Normal;
    }

    /*
    testFile is a txt file.
     */
    private PathType extractOnePath2(Graph graph, int testIndex, String testFileName) {
        try {
            Path path = new Path();
            FileInputStream fs = new FileInputStream(testFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String allInfo;
            List<String> commaSeparatedList = null;
            br.readLine();
            allInfo = br.readLine();
            while (allInfo != null) {
                commaSeparatedList = Arrays.asList(allInfo.split(","));
                //find the index-th data.
                if (Integer.parseInt(commaSeparatedList.get(0)) == testIndex) {
                    System.out.println(testIndex);
                    break;
                }
                allInfo = br.readLine();
            }

            if (allInfo == null) return PathType.Missing;

            String[] indexList = commaSeparatedList.get(2).split("\\|");
            for (String index : indexList) {
                Node node = new Node();
                node.index = index;
                if (graph.nodes.indexOf(node) == -1) {
                    System.out.println("[Error] Unknown nodes exist.");
//                    System.exit(1);
                    return PathType.UnknownNode;
                }
                Node completeNode = graph.nodes.get(graph.nodes.indexOf(node));
//                node.print();
                completeNode.print();
                path.nodeList.add(completeNode);
            }
            System.out.println("Everything is normal.");
            paths.add(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return PathType.Normal;
    }

    //TODO: update judging standard: one path is a subset of another.
    public void compareAndPrint(Graph graph, Path oraclePath, int testIndex) {
        /*
         if equal, print "Successful recovery",
         else, print "Failed recovery".
         And print complete path.
         */
        //compare two paths
        //I don't know oracle. Refer to Pathset.java line 19.
        boolean successful = true;

        //if any two paths of paths are identical, final path = anyone;
        //else final path = shortest path of beginning and ending.
        for (int i = 0; i < paths.size()-1; i++) {
            for (int j = i+1; j < paths.size(); j++) {
                if (!identicalPath(paths.get(i), paths.get(j))) {
                    successful = false; break;
                }
            }
        }

        if (successful) {
            System.out.println("Success: All recovered paths are identical.");
            finalPath = paths.get(0);
        }
        else {
            System.out.println("Failure: Recovered paths are different.");
            List<Node> nodeList = paths.get(0).nodeList;
            Node beginNode = nodeList.get(0);
            Node endNode = nodeList.get(nodeList.size()-1);
            //FIXME: how to get the shortest path from built graph.
//            finalPath = graph.getShortestPath(beginNode, endNode);
            finalPath = paths.get(0);
        }

        //save result
        //correctly output chinese characters
        try {
            //use Excel file to save outputs.
            String[] columns = {"门架HEX/收费站编号","收费站/门架名称","门架来源"};
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("case " + testIndex);
            Row row = sheet.createRow(0);
            if (successful) row.createCell(0).setCellValue("Success: All recovered paths are identical.");
            else row.createCell(0).setCellValue("Failure: Recovered paths are different.");
            row = sheet.createRow(1);
            row.createCell(0).setCellValue("Recovered path");
            row.createCell(3).setCellValue("Oracle path");
            row = sheet.createRow(2);
            for (int columnIndex = 0; columnIndex < 6; columnIndex++) {
                row.createCell(columnIndex).setCellValue(columns[columnIndex%3]);
            }
            int rowIndex = 3;
            for (Node node: finalPath.nodeList
            ) {
                row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(node.index);
                row.createCell(1).setCellValue(node.name);
                if (node.source == NodeSource.IDENTIFY)
                    row.createCell(2).setCellValue("标记出的点");
                else if (node.source == NodeSource.ADD)
                    row.createCell(2).setCellValue("增加出的点");
                else if (node.source == NodeSource.MODIFY)
                    row.createCell(2).setCellValue("修改出的点");
                else if (node.source == NodeSource.DELETE)
                    row.createCell(2).setCellValue("删除的点");
                else
                    row.createCell(2).setCellValue("不明出处的点");
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

    private boolean identicalPath(Path path1, Path path2) {
        for (int i = 0; i < path1.nodeList.size(); i++) {
            Node node1 = path1.nodeList.get(i);
            Node node2 = null;
            if (path2.nodeList.size() > i)
                node2 = path2.nodeList.get(i);
            if (!(node1.equals(node2))) return false;
        }
        return true;
    }
}


