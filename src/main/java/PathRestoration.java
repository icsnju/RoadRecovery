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
    public String pathRestorationMethod(String jsonData) {
        //build the graph
        //TODO: specify the excel path
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph();

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
        Node startNode = getNode(graph, enStationId);
        originalPath.nodeList.add(startNode);

        //FIXME: I need a runtime node, {node, timestamp}
        for (String gantry : gantryList) {
            Node completeNode = getNode(graph, gantry);
            originalPath.nodeList.add(completeNode);
        }

        Node endNode = getNode(graph, exStationId);
        originalPath.nodeList.add(endNode);

        Algorithm algorithm = new DPAlgorithm();
        Path recoveredPath = algorithm.execute(graph, originalPath);

        //generate JSON data for return
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
            pathSet.addDeleteAndModifyTag(recoveredPath.nodeList, originalPath.nodeList);

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
            returnJsonObj.put("useType", "");
            //there is a typo: updateGrantry -> updateGantry
            returnJsonObj.put("updateGantry", "");
        } else {
            handleFailure(returnJsonObj, "起点到终点不可达");
        }

        return returnJsonObj.toString();
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

    private Node getNode(Graph graph, String gantry) {
        Node node = new Node();
        node.index = gantry;
        if (graph.nodes.indexOf(node) == -1) {
            System.err.println("[Error] An unknown gantry " + gantry + " exists.");
            System.exit(1);
        }
        return graph.nodes.get(graph.nodes.indexOf(node));
    }
}
