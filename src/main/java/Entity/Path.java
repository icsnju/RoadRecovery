package Entity;

import static Entity.NodeSource.IDENTIFY;

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


    public int getLength() {
        return nodeList.size() - 1;
    }

    public void add(Path path2) {
        if (!nodeList.isEmpty() && !path2.nodeList.isEmpty() && nodeList.get(nodeList.size() - 1)
            .equals(path2.nodeList.get(0))) {
            if (path2.nodeList.get(0).source == IDENTIFY) {
                nodeList.get(nodeList.size() - 1).source = IDENTIFY;
            }
            nodeList.addAll(path2.nodeList.subList(1, path2.nodeList.size()));
        } else {
            nodeList.addAll(path2.nodeList);
        }
    }

    public void print() {
        for (Node node : nodeList) {
            System.out.println(node.index + " " + node.name + " " + node.source);
        }
        System.out.println("---path end---");
    }


}
