package Entity;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PathSet {

    public List<Path> paths = new ArrayList<Path>();
    public Path oraclePath = new Path();
    String testFileName = "src/main/resources/test-data.xls";
    int SheetHead = 4;

    /*
    FIXME: I don't where is test oracle.
    read two kinds of paths, combined into a path set.
     */
    public void readAllPath(int testIndex) {
        System.out.println("testIndex=" + testIndex);
        try {
            /*
            get steam info and test oracle.
             */
            Workbook workbook = WorkbookFactory.create(new File(testFileName));

            //get steam info
            Sheet sheet = workbook.getSheetAt(testIndex*2-1);
            extractOnePath(sheet, 0); // first path:  simplified path
            extractOnePath(sheet, 2); // second path: complete path

            //get test oracle (a complete path)
            /*
            sheet = workbook.getSheetAt(testIndex*2);
            rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() < SHEETHEAD) continue;
                System.out.println(row.getRowNum()+1);
                if (row.getCell(2) == null ||
                        row.getCell(3) == null || row.getCell(5) == null) continue;
                Node node = new Node();
                node.index = row.getCell(2).getStringCellValue();
                node.name = row.getCell(3).getStringCellValue();
                String portalSource = row.getCell(5).getStringCellValue();
                if (portalSource.equals("收费站")) {
                    node.identifyOrRecover = IdentifyOrRecover.TOLLSTATION;
                }
                else if (portalSource.equals("标识出的门架")) {
                    node.identifyOrRecover = IdentifyOrRecover.IDENTIFY;
                }
                else if (portalSource.equals("还原出的门架")) {
                    node.identifyOrRecover = IdentifyOrRecover.RECOVER;
                }
                //node.type can be completed with graph information
                oraclePath.nodeList.add(node);
            }
             */

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void extractOnePath(Sheet sheet, int columnBase) {
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
            path.nodeList.add(node);
        }
        paths.add(path); //testcase has only one path.
    }
}
