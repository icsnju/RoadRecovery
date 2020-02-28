package nju.ics.Entity;

import java.util.ArrayList;
import java.util.List;

public class Path {

    public List<Node> nodeList = new ArrayList<Node>();


    public int getLength() {
        return nodeList.size() - 1;
    }

    public void add(Path path2) {
        if (!nodeList.isEmpty() && !path2.nodeList.isEmpty() && nodeList.get(nodeList.size() - 1)
            .equals(path2.nodeList.get(0))) {
            nodeList.addAll(path2.nodeList.subList(1, path2.nodeList.size()));
        } else {
            nodeList.addAll(path2.nodeList);
        }
    }

    public void print(String desc) {
        System.out.println("---"+ desc +" begin---");
        System.out.println("---length = "+ nodeList.size() +" ---");
        for (Node node : nodeList) {
            System.out.println(fixedLengthString(node.index, 20) + " " +
                    fixedLengthString(node.source.toString(), 20)+ " " +
                    fixedLengthString(node.name, 20));
        }
        System.out.println("---path end---");
    }

    public static String fixedLengthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }


}
