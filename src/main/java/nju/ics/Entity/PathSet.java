package nju.ics.Entity;

import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class PathSet {

    public static HashMap<String, HashMap<Integer, List<String>>> inputsMap = new HashMap<>();

    public String outDirectory = "src/test/resources/outputs/";

    public void printRecoveredPath(Sheet sheet, RuntimePath path, int baseColumnIndex) {
        int rowIndex = 3;

        for (RuntimeNode runtimeNode: path.runtimeNodeList
        ) {
            //if row exists, do not create.
            Row row = sheet.getRow(rowIndex);
            if (row == null)
                row = sheet.createRow(rowIndex);
            rowIndex++;
            row.createCell(baseColumnIndex).setCellValue(runtimeNode.node.index);
            row.createCell(baseColumnIndex+1).setCellValue(runtimeNode.node.name);
            //(if needed): print node.type
            if (runtimeNode.node.source == NodeSource.IDENTIFY)
                row.createCell(baseColumnIndex+2).setCellValue("标记点");
            else if (runtimeNode.node.source == NodeSource.ADD)
                row.createCell(baseColumnIndex+2).setCellValue("增加点");
            else if (runtimeNode.node.source == NodeSource.MODIFY)
                row.createCell(baseColumnIndex+2).setCellValue("对向点");
            else if (runtimeNode.node.source == NodeSource.DELETE)
                row.createCell(baseColumnIndex+2).setCellValue("删除点");
            else
                row.createCell(baseColumnIndex+2).setCellValue("不明出处");
        }
    }

}


