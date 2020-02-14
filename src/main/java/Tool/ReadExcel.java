package Tool;

import Entity.Edge;
import Entity.Graph;
import Entity.Node;
import Entity.NodeType;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static Entity.NodeType.*;

public class ReadExcel {

    private String xlsFileName = "src/main/resources/basic-data.xls";
    private int SHEETHEAD = 4;
    int EDGESHEET = 1;
    int MUTUALSHEET = 3;

    private Graph graph = new Graph();

    public Graph buildGraph() {
        try {
            Workbook workbook = WorkbookFactory.create(new File(xlsFileName));
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                if (sheet.getSheetName().equals("1")) { // all edge information
                    addEdgeFromSheet(sheet, EDGESHEET);
                }
                else if (sheet.getSheetName().equals("3")) { // all mutual information
                    addEdgeFromSheet(sheet, MUTUALSHEET);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    private void addEdgeFromSheet(Sheet sheet, int sheetIndex) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() < SHEETHEAD) continue;

            /*
             Add two nodes and one edge into graph
             */
            //add two nodes
            Node inNode = extractNodeFromRow(row, 1);
            Node outNode = extractNodeFromRow(row, 4);
            // exist outNode is {0, wu, wu} | {index, wu, wu}
            if (inNode == null || outNode == null) continue;
            if (!graph.nodeSet.contains(inNode)) graph.nodeSet.add(inNode);
            if (!graph.nodeSet.contains(outNode)) graph.nodeSet.add(outNode);

            //add one edge into edgeSet or mutualSet
            Edge edge = new Edge();
            edge.inNode = inNode;
            edge.outNode = outNode;
            if (sheetIndex == EDGESHEET) {
                if (!graph.edgeSet.contains(edge)) graph.edgeSet.add(edge);
            }
            else if (sheetIndex == MUTUALSHEET) {
                if (!graph.mutualSet.contains(edge)) graph.mutualSet.add(edge);
            }
        }
    }

    private Node extractNodeFromRow(Row row, int base) {
        Node node = new Node();
//        System.out.println(row.getRowNum()+1+","+base);
        // null node
        if (row.getCell(base+1).getStringCellValue().equals("无"))
            return null;

        node.index = row.getCell(base).getStringCellValue();
        node.name = row.getCell(base+1).getStringCellValue();
        switch ((int) row.getCell(base+2).getNumericCellValue()) {
            case 0: node.type = NORMALPORTAL; break;
            case 1: node.type = PROVINCIALPORTAL; break;
            case 3: node.type = TOLLSTATION; break;
        }
//        System.out.println(node.index+", "+node.name+", "+node.type);
        return node;
    }
}
