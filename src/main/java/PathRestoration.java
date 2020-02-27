import Algorithm.*;
import Entity.*;
import Tool.ReadExcel;
import org.json.JSONObject;

public class PathRestoration {
    /**
     * input key
     */
    String enStationId, exStationId;
    String enTime, exTime;
    String gantryGroup, typeGroup, timeGroup;

    /** TODO:
     * output key
     */


    /**
     * interface method for external call
     *
     * @param jsonData each element of an input path in JSON format
     * @return
     */
    public String pathRestorationMethod(String jsonData, String basicDataPath)  {
        //build the graph
        //TODO: specify the excel path
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph(basicDataPath);

        //analyze JSON data
        JSONObject jsonObj = new JSONObject(jsonData);

        enStationId = jsonObj.getString("enStationId");
        exStationId = jsonObj.getString("exStationId");
        enTime      = jsonObj.getString("enTime");
        exTime      = jsonObj.getString("exTime");

        gantryGroup = jsonObj.getString("gantryGroup");
        typeGroup   = jsonObj.getString("typeGroup");
        timeGroup   = jsonObj.getString("timeGroup");

        String[] gantryList = gantryGroup.split("\\|");
        //FIXME: time information isn't used right now.
        String[] timeList = timeGroup.split("\\|");


        //add the start and end node into original path
        Path originalPath = new Path();

        try {
            try {
                Node startNode = getNode(graph, enStationId);
                startNode.source = NodeSource.IDENTIFY;
                originalPath.nodeList.add(startNode);
            } catch (NodeUnknownException e) {
                //do nothing
            }

            //FIXME: I need a runtime node, {node, timestamp}
            for (String gantry : gantryList) {
                Node completeNode = getNode(graph, gantry);
                completeNode.source = NodeSource.IDENTIFY;
                originalPath.nodeList.add(completeNode);
            }

            try {
                Node endNode = getNode(graph, exStationId);
                endNode.source = NodeSource.IDENTIFY;
                originalPath.nodeList.add(endNode);
            } catch (NodeUnknownException e) {
                //do nothing
            }

        }  catch (NodeUnknownException e) {
            return getReturnedJsonObject(originalPath, null, "存在未知门架").toString();
        }

        Algorithm algorithm = new DPAlgorithm();
        Path recoveredPath = algorithm.execute(graph, originalPath);

        //generate JSON data for return
        JSONObject returnJsonObj = getReturnedJsonObject(originalPath, recoveredPath, "奇怪的错误");

        return returnJsonObj.toString();
    }

    private JSONObject getReturnedJsonObject(Path originalPath, Path recoveredPath, String description) {
        JSONObject returnJsonObj = new JSONObject();
        if (recoveredPath != null) {
            returnJsonObj.put("code", "1");
            returnJsonObj.put("description", "路径还原成功");

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
            originalPath.print();
            pathSet.addDeleteAndModifyTag(recoveredPath.nodeList, originalPath.nodeList);
            originalPath.print();

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
        //TODO: exception handling
        returnJsonObj.put("code", "2");
        //FIXME: @fancy: describe why restoration fails.
        returnJsonObj.put("description", "还原失败的原因："+description);
        returnJsonObj.put("pathInfo", "0");
        returnJsonObj.put("typeGroup", "0");
        returnJsonObj.put("flagGroup", "0");
        returnJsonObj.put("gantryGroup", gantryGroup);
        returnJsonObj.put("useType", "0");
        //there is a typo: updateGrantry -> updateGantry
        returnJsonObj.put("updateGantry", "0");
    }

    private Node getNode(Graph graph, String gantry) throws NodeUnknownException {
        Node node = new Node();
        node.index = gantry;
        if (graph.nodes.indexOf(node) == -1) {
            System.err.println("[Error] 未知门架/收费站 " + gantry + " 存在.");
            throw new NodeUnknownException();
        }
        return graph.nodes.get(graph.nodes.indexOf(node));
    }

    private class NodeUnknownException extends Throwable {

    }
}
