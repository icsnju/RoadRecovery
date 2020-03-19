package nju.ics.Main;


import nju.ics.Tool.ReadExcel;
import nju.ics.Algorithm.Algorithm;
import nju.ics.Algorithm.DPAlgorithm;
import nju.ics.Entity.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathRestoration {

    private boolean testing = true;
    private boolean debugging = false;

    public static Graph graph = null;
    /**
     * input key
     */
    String enStationId, exStationId;
    String enTime, exTime;
    String gantryGroup, typeGroup, timeGroup;
    int testIndex;
    String basicDataPath;

    double addCost, deleteCost, modifyCost, deleteEndCost;

    StringBuilder description = new StringBuilder("Unknown gantry: ");
    int desCount = 0;

    Path originalPath = new Path();
    Path recoveredPath = null;
    Path manualPath = null;
    /**
     * interface method for external call
     *
     * @param jsonData each element of an input path in JSON format
     * @return
     */
    public String pathRestorationMethod(String jsonData)  {
        //analyze JSON data
        JSONObject jsonObj = new JSONObject(jsonData);

        enStationId = jsonObj.getString("enStationId");
//        System.out.println(enStationId);
        exStationId = jsonObj.getString("exStationId");
        enTime      = jsonObj.getString("enTime");
        exTime      = jsonObj.getString("exTime");

        gantryGroup = jsonObj.getString("gantryGroup");
        typeGroup   = jsonObj.getString("typeGroup");
        timeGroup   = jsonObj.getString("timeGroup");

        basicDataPath = jsonObj.getString("basicDataPath");
        testIndex     = jsonObj.getInt("testIndex");

        //configuration parameter for DP
        modifyCost    = jsonObj.getDouble("modifyCost");
        addCost       = jsonObj.getDouble("addCost");
        deleteCost    = jsonObj.getDouble("deleteCost");
        deleteEndCost = jsonObj.getDouble("deleteEndCost");

        List<Double> configs = new ArrayList<>();
        configs.add(modifyCost);
        configs.add(addCost);
        configs.add(deleteCost);
        configs.add(deleteEndCost);

        //build the graph
        //specify the excel path
        if (graph == null) {
            ReadExcel readExcel = new ReadExcel();
            graph = readExcel.buildGraph(basicDataPath);

        }

//        System.out.println(gantryGroup.length());
        String[] gantryList = gantryGroup.split("\\|");
        //FIXME: time information isn't used right now.
        String[] timeList = timeGroup.split("\\|");

        if (testing) {
        try {
            manualPath = new Path();
            //from Chinese gantry to gantry index
            String[] manualGantryList = jsonObj.getString("truePath").split("\\|");

            int count = 0;
            for (String gantry : manualGantryList) {
                //
                StringBuilder completeGantry = new StringBuilder(gantry);
                if (count == 0 || count == manualGantryList.length-1) {
                    completeGantry.insert(0, "山东");
                    completeGantry.append("站");
                }
                count++;
                String gantryIndex = null;
                for (Node node: graph.nodes
                     ) {
                    if (node.name.equals(completeGantry.toString())) {
                        gantryIndex = node.index;
                    }
                }

                Node completeNode = getNode(graph, gantryIndex, true);
                if (completeNode != null) {
                    completeNode.source = NodeSource.IDENTIFY;
                    manualPath.nodeList.add(completeNode);
                }
            }
        } catch (Exception e) {
            // do nothing
        }}

        //add the start and end node into original path


        Node startNode = getNode(graph, enStationId, false);
        if (startNode != null) {
            startNode.source = NodeSource.IDENTIFY;
            originalPath.nodeList.add(startNode);
        }

        //FIXME: I need a runtime node, {node, timestamp}
        if (gantryGroup.length() > 0) {
            for (String gantry : gantryList) {
//                System.out.println(gantry);
                Node completeNode = getNode(graph, gantry, true);
                if (completeNode != null) {
                    completeNode.source = NodeSource.IDENTIFY;
                    originalPath.nodeList.add(completeNode);
                }
            }
        }

        Node endNode = getNode(graph, exStationId, false);
        if (endNode != null) {
            endNode.source = NodeSource.IDENTIFY;
            originalPath.nodeList.add(endNode);
        }

        //If exist unknown gantry, then return with failure.
        if (desCount > 0)
            return getReturnedJsonObject(
                    originalPath,
                    null,
                    description.toString()
            ).toString();

        //If only exist one node, then return with failure.
        if (originalPath.nodeList.size() == 1) {
            return getReturnedJsonObject(
                    originalPath,
                    null,
                    "Exist only one node "+originalPath.nodeList.get(0).index+"."
            ).toString();
        }

        PathSet originalPathSet = new PathSet();
        originalPathSet.paths.add(originalPath);

//        originalPath.print("input path");
        Algorithm algorithm = new DPAlgorithm();
        recoveredPath = algorithm.execute(graph, originalPath, configs);
        if (debugging) recoveredPath.print("算法恢复的路径");

        PathSet recoveredPathSet = new PathSet();
        recoveredPathSet.paths.add(recoveredPath);

        //generate JSON data for return
        JSONObject returnJsonObj = getReturnedJsonObject(originalPath, recoveredPath, "Unknown reason");

        return returnJsonObj.toString();
    }

    private JSONObject getReturnedJsonObject(Path originalPath, Path recoveredPath, String description) {
        JSONObject returnJsonObj = new JSONObject();
        if (recoveredPath != null) {
            returnJsonObj.put("code", "1");
            returnJsonObj.put("description", "Success");

            //directly obtain the recoveredPath info.
            StringBuilder pathInfo = new StringBuilder();
            StringBuilder typeGroup = new StringBuilder();
            StringBuilder flagGroup = new StringBuilder();

            int count = 0;
            for (Node node: recoveredPath.nodeList
                 ) {
                if (count > 0) {
                    pathInfo.append("|");
                    typeGroup.append("|");
                    flagGroup.append("|");
                }
                count++;

                pathInfo.append(node.index);

                if (node.type == NodeType.NORMALPORTAL) typeGroup.append("0");
                if (node.type == NodeType.PROVINCIALPORTAL) typeGroup.append("1");
                if (node.type == NodeType.TOLLSTATION) typeGroup.append("3");

                if (node.source == NodeSource.IDENTIFY) flagGroup.append("1");
                if (node.source == NodeSource.MODIFY) flagGroup.append("2");
                if (node.source == NodeSource.ADD) flagGroup.append("3");

            }

            returnJsonObj.put("pathInfo", pathInfo.toString());
            returnJsonObj.put("typeGroup", typeGroup.toString());
            returnJsonObj.put("flagGroup", flagGroup.toString());

            //mark the original node as one of {1:identify, 2:modify, 3:delete}
            PathSet pathSet = new PathSet();
//            originalPath.print("原始路径");
            pathSet.finalPathInCard = pathSet.addDeleteAndModifyTag(recoveredPath.nodeList, originalPath.nodeList);
            if (testIndex != 0)
                pathSet.dumpIntoExcel(testIndex, false, true);

            if (debugging) originalPath.print("原始路径的修订版");
            if (debugging) pathSet.finalPathInCard.print("合并的路径");

            StringBuilder useType = new StringBuilder();
            StringBuilder updateGantry = new StringBuilder();

            count = 0;
            int updateCount = 0;
            for (Node node: originalPath.nodeList
                 ) {
                if (count > 0) {
                    useType.append("|");
                }
                count++;

                if (node.source == NodeSource.IDENTIFY) useType.append("1");
                if (node.source == NodeSource.MODIFY) {
                    if (updateCount > 0)
                        updateGantry.append("|");
                    updateCount++;

                    useType.append("2");
                    updateGantry.append(node.index).append("-").append(node.mutualNode.index);
                }
                if (node.source == NodeSource.DELETE) useType.append("3");
            }

            returnJsonObj.put("gantryGroup", gantryGroup);
            returnJsonObj.put("useType", useType.toString());
            returnJsonObj.put("updateGantry", updateGantry.toString());
        } else {
            handleFailure(returnJsonObj, description);
        }
        return returnJsonObj;
    }

    private void handleFailure(JSONObject returnJsonObj, String description) {
        // exception handling
        returnJsonObj.put("code", "2");
        //@fancy: describe why restoration fails.
        returnJsonObj.put("description", "Failure cause: "+description);
        returnJsonObj.put("pathInfo", "0");
        returnJsonObj.put("typeGroup", "0");
        returnJsonObj.put("flagGroup", "0");
        returnJsonObj.put("gantryGroup", gantryGroup);
        returnJsonObj.put("useType", "0");
        returnJsonObj.put("updateGantry", "0");
    }

    private Node getNode(Graph graph, String gantry, boolean isGantry) {
        Node node = new Node();
        node.index = gantry;
        if (graph.nodes.indexOf(node) == -1) {
            if (isGantry) {
                System.err.println("[Error] Unknown gantry [" + gantry + "] exists.");
                updateDescription(gantry);
            }
            return null;
        }
        return graph.nodes.get(graph.nodes.indexOf(node));
    }

    private void updateDescription(String gantry) {
        if (desCount > 0) description.append("|");
        desCount++;
        description.append(gantry);
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("please provide two args:\n" +
                    "\tString jsonData");
            System.exit(1);
        }

        PathRestoration pathRestoration = new PathRestoration();
        pathRestoration.pathRestorationMethod(args[0]);
    }

    public boolean compare(int testIndex) {
        boolean same = true;

        try {
            //use Excel file to save outputs.
            String[] columns = {"门架HEX/收费站编号","收费站/门架名称","门架来源"};
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("ID " + testIndex);
            for (int i = 0; i < 10; i++) {
                sheet.setColumnWidth(i, 8000);
            }

            for (int i = 0; i < recoveredPath.nodeList.size(); i++) {
                Node recoveredNode = recoveredPath.nodeList.get(i);
                if (manualPath.nodeList.size() > i) {
                    Node manualNode = manualPath.nodeList.get(i);
                    if (!recoveredNode.index.equals(manualNode.index)) {
                        same = false;
                        break;
                    }
                }
                else {
                    same = false;
                    break;
                }
            }

            Row row = sheet.createRow(0);
            if (same) row.createCell(0).setCellValue("相同：手工和恢复");
            else row.createCell(0).setCellValue("不相同：手工和恢复");

            row = sheet.createRow(1);
            row.createCell(0).setCellValue("输入");
            row.createCell(3).setCellValue("算法恢复");
            row.createCell(6).setCellValue("人工恢复");

            row = sheet.createRow(2);
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                row.createCell(columnIndex).setCellValue(columns[columnIndex%3]);
            }

            PathSet pathSet = new PathSet();
            pathSet.printRecoveredPath(sheet, originalPath, 0);
            pathSet.printRecoveredPath(sheet, recoveredPath, 3);
            pathSet.printRecoveredPath(sheet, manualPath, 6);

            // Write the output to a file
            File outDir = new File(pathSet.outDirectory);
            if (!outDir.exists()) outDir.mkdir();
            String filename = pathSet.outDirectory + testIndex + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return same;
    }
}
