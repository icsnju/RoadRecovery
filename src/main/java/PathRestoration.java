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
        //TODO: build the graph
        ReadExcel readExcel = new ReadExcel();
        Graph graph = readExcel.buildGraph();

        //TODO: analyze JSON data
        JSONObject jsonObj = new JSONObject(jsonData);

        enStationId = jsonObj.getString("enStationId");
        exStationId = jsonObj.getString("exStationId");
        enTime      = jsonObj.getString("enTime");
        exTime      = jsonObj.getString("exTime");

        gantryGroup = jsonObj.getString("gantryGroup");
        typeGroup   = jsonObj.getString("typeGroup");
        timeGroup   = jsonObj.getString("timeGroup");

        String[] gantryList = gantryGroup.split("\\|");
//        String[] typeList = typeGroup.split("\\|");
        String[] timeList = timeGroup.split("\\|");

        Path originalPath = new Path();

        //FIXME: I need a runtime node, {node, timestamp}
        for (String gantry : gantryList) {
            Node completeNode = getNode(graph, gantry);
            originalPath.nodeList.add(completeNode);
        }

        Algorithm algorithm = new DPAlgorithm();
        Path recoveredPath = algorithm.execute(graph, originalPath);

        //TODO: generate JSON data for return
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

                if (node.source == NodeSource.IDENTIFY) flagGroup.append("0");
                if (node.source == NodeSource.ADD) flagGroup.append("1");
                if (node.source == NodeSource.MODIFY) flagGroup.append("2");
            }

            returnJsonObj.put("pathInfo", pathInfo.toString());
            returnJsonObj.put("typeGroup", typeGroup.toString());
            returnJsonObj.put("flagGroup", flagGroup.toString());

            //TODO: mark the original node as one of {0:identify, 2:modify, 3:delete}
            PathSet pathSet = new PathSet();
            pathSet.addDeleteAndModifyTag(recoveredPath.nodeList, originalPath.nodeList);


            for (:
                 ) {

            }

            returnJsonObj.put("gantryGroup", gantryGroup);
            returnJsonObj.put("useType", "");
            //there is a typo: updateGrantry -> updateGantry
            returnJsonObj.put("updateGantry", "");
        } else {
            returnJsonObj.put("code", "2");
            //FIXME: @fancy: describe why restoration fails.
            returnJsonObj.put("description", "还原失败的原因：FIXME");
            returnJsonObj.put("pathInfo", "0");
            returnJsonObj.put("typeGroup", "0");
            returnJsonObj.put("flagGroup", "0");
            returnJsonObj.put("gantryGroup", gantryGroup);
            returnJsonObj.put("useType", "0");
            //there is a typo: updateGrantry -> updateGantry
            returnJsonObj.put("updateGantry", "0");
        }

        return returnJsonObj.toString();
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
