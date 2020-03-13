package nju.ics.Main;


import nju.ics.Tool.ReadExcel;
import nju.ics.Algorithm.Algorithm;
import nju.ics.Algorithm.DPAlgorithm;
import nju.ics.Entity.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PathRestoration {

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

        //add the start and end node into original path
        Path originalPath = new Path();

        Node startNode = getNode(graph, enStationId, false);
        if (startNode != null) {
            startNode.source = NodeSource.IDENTIFY;
            originalPath.nodeList.add(startNode);
        }

        //FIXME: I need a runtime node, {node, timestamp}
        if (gantryGroup.length() > 0) {
            for (String gantry : gantryList) {
                System.out.println(gantry);
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

        originalPath.print("input path");
        Algorithm algorithm = new DPAlgorithm();
        Path recoveredPath = algorithm.execute(graph, originalPath, configs);
        recoveredPath.print("算法恢复的路径");

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

            originalPath.print("原始路径的修订版");
            pathSet.finalPathInCard.print("合并的路径");

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
}
