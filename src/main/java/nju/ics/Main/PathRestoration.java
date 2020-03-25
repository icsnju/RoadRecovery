package nju.ics.Main;


import nju.ics.Tool.ReadExcel;
import nju.ics.Algorithm.Algorithm;
import nju.ics.Algorithm.DPAlgorithm;
import nju.ics.Entity.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathRestoration {

    private boolean testing = false;
    private boolean debugging = false;

    public static Graph graph = null;
    /**
     * input key
     */
    String enStationId, exStationId;
    String enTime, exTime;
    int testIndex;
    String basicDataPath;

    double addCost, deleteCost, deleteCost2, modifyCost, deleteEndCost;

    StringBuilder description = new StringBuilder("Unknown gantry: ");
    int desCount = 0;

    RuntimePath originalPath = new RuntimePath();
    RuntimePath recoveredPath = null;
    RuntimePath manualPath = null;
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
        exStationId = jsonObj.getString("exStationId");
        enTime      = jsonObj.getString("enTime");
        if (enTime.length() == 0) enTime = null;
        exTime      = jsonObj.getString("exTime");
        if (exTime.length() == 0) exTime = null;

        //new interface params, from JSONArray to List<Map<String, String>>
        JSONArray json_arr = jsonObj.getJSONArray("gantryIdList");
        List<String> gantryList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        for (int i = 0; i < json_arr.length(); i++) {
            JSONObject jsonArrayObj = json_arr.getJSONObject(i);
            String gantryHex = jsonArrayObj.getString("gantryHex");
            String transTime = jsonArrayObj.getString("transTime");
            if (transTime.length() == 0) transTime = null;

            gantryList.add(gantryHex);
            timeList.add(transTime);
        }

        basicDataPath = jsonObj.getString("basicDataPath");

        //configuration parameter for DP
        modifyCost    = jsonObj.getDouble("modifyCost");
        addCost       = jsonObj.getDouble("addCost");
        deleteCost    = jsonObj.getDouble("deleteCost");
        deleteCost2   = jsonObj.getDouble("deleteCost2");
        deleteEndCost = jsonObj.getDouble("deleteEndCost");

        List<Double> configs = new ArrayList<>();
        configs.add(modifyCost);
        configs.add(addCost);
        configs.add(deleteCost);
        configs.add(deleteCost2);
        configs.add(deleteEndCost);

        //build the graph
        //specify the excel path
        if (graph == null) {
            ReadExcel readExcel = new ReadExcel();
            graph = readExcel.buildGraph(basicDataPath);
        }

        if (testing) {
        try {
            manualPath = new RuntimePath();
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
                    manualPath.runtimeNodeList.add(new RuntimeNode(completeNode, null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}

        //add the start and end node into original path
        Node startNode = getNode(graph, enStationId, false);
        if (startNode != null) {
            startNode.source = NodeSource.IDENTIFY;
            originalPath.runtimeNodeList.add(new RuntimeNode(startNode, enTime));
//            System.out.println(enTime);
        }

        //I need a runtime node, {node, timestamp}
        int count = 0;
        if (gantryList.size() > 0) {
            for (String gantry : gantryList) {
//                System.out.println(gantry);
                Node completeNode = getNode(graph, gantry, true);
                if (completeNode != null) {
                    completeNode.source = NodeSource.IDENTIFY;
                    originalPath.runtimeNodeList.add(new RuntimeNode(completeNode, timeList.get(count)));
                }
                count++;
            }
        }

        Node endNode = getNode(graph, exStationId, false);
        if (endNode != null) {
            endNode.source = NodeSource.IDENTIFY;
            originalPath.runtimeNodeList.add(new RuntimeNode(endNode, exTime));
        }

        //If exist unknown gantry, then return with failure.
        if (desCount > 0)
            return getReturnedJsonObject(
                    originalPath,
                    null,
                    description.toString()
            ).toString();

        //If only exist one node, then return with failure.
        if (originalPath.runtimeNodeList.size() == 0) {
            return getReturnedJsonObject(
                    originalPath,
                    null,
                    "No identified node"
            ).toString();
        }

        if (originalPath.runtimeNodeList.size() == 1) {
            return getReturnedJsonObject(
                    originalPath,
                    null,
                    "Exist only one node "+originalPath.runtimeNodeList.get(0).node.index
            ).toString();
        }

//        originalPath.print("input path");
        System.out.println("原路径长度=" + originalPath.runtimeNodeList.size());
        Algorithm algorithm = new DPAlgorithm();
        recoveredPath = algorithm.execute(graph, originalPath, configs);
        if (debugging) recoveredPath.print("算法恢复的路径");

        //generate JSON data for return
        JSONObject returnJsonObj = getReturnedJsonObject(originalPath, recoveredPath, "Unknown reason");

        return returnJsonObj.toString();
    }

    private JSONObject getReturnedJsonObject(RuntimePath originalPath, RuntimePath recoveredPath, String description) {
        JSONObject returnJsonObj = new JSONObject();
        if (recoveredPath != null) {
            recoveredPath.print("恢复出的路径");

            returnJsonObj.put("code", "1");
            returnJsonObj.put("description", "Success");

            //directly obtain the recoveredPath info.
            StringBuilder gantryHexGroup  = new StringBuilder();
            StringBuilder gantryFlagGroup = new StringBuilder();

            //fix missing time information
            StringBuilder transTimeGroup  = new StringBuilder();
            String lastTime = "";

            //reverse order
            for (int i = recoveredPath.runtimeNodeList.size()-1; i >= 0 ; i--) {
                RuntimeNode runtimeNode = recoveredPath.runtimeNodeList.get(i);
                if (runtimeNode.transTime != null) lastTime = runtimeNode.transTime;
                if (runtimeNode.node.index.length() >= 10) continue;
                if (runtimeNode.transTime == null) runtimeNode.transTime = lastTime;
            }

            //delete the entry & exit toll station
            int count = 0;
            for (RuntimeNode runtimeNode: recoveredPath.runtimeNodeList
                 ) {
                if (runtimeNode.node.index.length() >= 10) continue;

                if (count > 0) {
                    gantryHexGroup.append("|");
                    gantryFlagGroup.append("|");
                    transTimeGroup.append("|");
                }
                count++;

                gantryHexGroup.append(runtimeNode.node.index);

                if (runtimeNode.node.source == NodeSource.IDENTIFY) gantryFlagGroup.append("1");
                if (runtimeNode.node.source == NodeSource.MODIFY)   gantryFlagGroup.append("2");
                if (runtimeNode.node.source == NodeSource.ADD)      gantryFlagGroup.append("2");

                transTimeGroup.append(runtimeNode.transTime);
            }

            returnJsonObj.put("gantryHexGroup",  gantryHexGroup.toString());
            returnJsonObj.put("gantryFlagGroup", gantryFlagGroup.toString());
            returnJsonObj.put("transTimeGroup",  transTimeGroup.toString());

//            //mark the original node as one of {1:identify, 2:modify, 3:delete}
//            PathSet pathSet = new PathSet();
////            originalPath.print("原始路径");
//            pathSet.finalPathInCard = pathSet.addDeleteAndModifyTag(recoveredPath.runtimeNodeList, originalPath.runtimeNodeList);
//            if (testIndex != 0)
//                pathSet.dumpIntoExcel(testIndex, false, true);
//
//            if (debugging) originalPath.print("原始路径的修订版");
//            if (debugging) pathSet.finalPathInCard.print("合并的路径");
//
//            StringBuilder useType = new StringBuilder();
//            StringBuilder updateGantry = new StringBuilder();
//
//            count = 0;
//            int updateCount = 0;
//            for (RuntimeNode runtimeNode: originalPath.runtimeNodeList
//                 ) {
//                if (count > 0) {
//                    useType.append("|");
//                }
//                count++;
//
//                if (runtimeNode.node.source == NodeSource.IDENTIFY) useType.append("1");
//                if (runtimeNode.node.source == NodeSource.MODIFY) {
//                    if (updateCount > 0)
//                        updateGantry.append("|");
//                    updateCount++;
//
//                    useType.append("2");
//                    updateGantry.append(runtimeNode.node.index).append("-").append(runtimeNode.node.mutualNode.index);
//                }
//                if (runtimeNode.node.source == NodeSource.DELETE) useType.append("3");
//            }

        } else {
            handleFailure(returnJsonObj, description);
        }
        return returnJsonObj;
    }

    private void handleFailure(JSONObject returnJsonObj, String description) {
        //A 5-element tuple
        returnJsonObj.put("code",            "2");
        returnJsonObj.put("description",     "Failure cause: "+description);
        returnJsonObj.put("gantryHexGroup",  "");
        returnJsonObj.put("gantryFlagGroup", "");
        returnJsonObj.put("transTimeGroup",  "");
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

            for (int i = 0; i < recoveredPath.runtimeNodeList.size(); i++) {
                RuntimeNode recoveredNode = recoveredPath.runtimeNodeList.get(i);
                if (manualPath.runtimeNodeList.size() > i) {
                    RuntimeNode manualNode = manualPath.runtimeNodeList.get(i);
                    if (!recoveredNode.node.index.equals(manualNode.node.index)) {
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
