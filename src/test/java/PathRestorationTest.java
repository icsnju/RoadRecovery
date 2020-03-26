import nju.ics.Main.PathRestoration;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathRestorationTest {

    JSONObject successJsonObject = new JSONObject();
    JSONObject failureJsonObject = new JSONObject();

    String basic_data_file_path = "src/test/resources/inputs/basic" +
            "-data-20200319.xls";

    String test_data_file_path = "src/test/resources/inputs/single-test-case" +
            ".txt";

    @Test
    public void testPathRestorationWithNewCases() throws IOException {
//        PathRestoration pathRestoration = new PathRestoration();

        // Open the file
        FileInputStream fileInputStream = new FileInputStream(test_data_file_path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

        String strLine;

        //Read File Line By Line
        int count = 0;
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            System.out.print( (++count) + ": ");
            JSONObject jsonObject = new JSONObject(strLine);

            PathRestoration pathRestoration = new PathRestoration();
            jsonObject.put("basicDataPath", basic_data_file_path);
            String returnedJSONString = pathRestoration.pathRestorationMethod(jsonObject.toString());
            JSONObject returnedJSONObject = new JSONObject(returnedJSONString);
            System.out.println(returnedJSONObject);
            //compare path info and returnedJSONString
//            boolean same = pathRestoration.compare(Integer.parseInt(jsonObject.getString("id")));
//            if (same) {
//                System.out.println("相同");
//            }
//            else {
//                System.out.println("不同");
//            }

//            if (count > 0) break;
        }

        //Close the input stream
        fileInputStream.close();
    }

    @Test
    public void testPathRestorationMethod() {
        getInput();

        PathRestoration pathRestoration = new PathRestoration();
        String returnString;

        System.out.println(successJsonObject);
        returnString = pathRestoration.pathRestorationMethod(successJsonObject.toString());
        System.out.println(returnString);
        System.out.println();

//        System.out.println(failureJsonObject);
//        returnString = pathRestoration.pathRestorationMethod(failureJsonObject.toString());
//        System.out.println(returnString);

        //assert some properties
    }

    @Test
    public void getInput() {
        // "deleteEndCost":10000,
        // "modifyCost":0.01,
        // "addCost":0.1,
        // "deleteCost":0.6,

        //manually curate a successful JSON data
        successJsonObject.put("basicDataPath", basic_data_file_path);

        successJsonObject.put("modifyCost", 0.01);
        successJsonObject.put("addCost", 0.1);
        successJsonObject.put("deleteCost", 4000);
        successJsonObject.put("deleteCost2", 2);
        successJsonObject.put("deleteEndCost", 1000000);

        List<JSONObject> gantryIDList = new ArrayList<>();
        successJsonObject.put("enStationId", "");
        successJsonObject.put("exStationId", "");
        successJsonObject.put("enTime", "2020-01-23 16:30:31");
        successJsonObject.put("exTime", "2020-01-23 18:40:20");
        //{"transTime":"","gantryHex":"3F5A0A"},
        // {"transTime":"","gantryHex":"3D5A0C"},
        // {"transTime":"","gantryHex":"3D5A0E"},
        // {"transTime":"","gantryHex":"3D5A0F"},
        // {"transTime":"","gantryHex":"3D5A10"},
        // {"transTime":"","gantryHex":"3D5A11"},
        // {"transTime":"","gantryHex":"3D5A12"},
        // {"transTime":"","gantryHex":"3D5F06"},
        // {"transTime":"","gantryHex":"3D5F07"},
        // {"transTime":"","gantryHex":"3D5F08"},
        // {"transTime":"","gantryHex":"3C4A04"},
        // {"transTime":"","gantryHex":"3E4A05"}
        addToList(gantryIDList, "3F5A0A", "");
        addToList(gantryIDList, "3D5A0C", "");
        addToList(gantryIDList, "3D5A0E", "");
        addToList(gantryIDList, "3D5A0F", "");
        addToList(gantryIDList, "3D5A10", "");
        addToList(gantryIDList, "3D5A11", "");
        addToList(gantryIDList, "3D5A12", "");
        addToList(gantryIDList, "3D5F06", "");
        addToList(gantryIDList, "3D5F07", "");
        addToList(gantryIDList, "3D5F08", "");
        addToList(gantryIDList, "3C4A04", "");
        addToList(gantryIDList, "3E4A05", "");

        successJsonObject.put("gantryIdList", gantryIDList);


        //manually curate a failure JSON data
        //"gantryIdList":[],"exStationId":"","enTime":"2020-01-24 00:33:23",
                //"deleteEndCost":10000,"basicDataPath":"H:\\basic-data.xls",
                //"modifyCost":0.01,"exTime":"2020-01-24 09:32:48","addCost":
        // 0.1,"enStationId":"","deleteCost":0.6,"deleteCost2":2}
        failureJsonObject.put("enStationId", "");
        failureJsonObject.put("exStationId", "");
        failureJsonObject.put("enTime",      "2020-01-22 11:39:03");
        failureJsonObject.put("exTime",      "2020-01-22 12:06:05");

        failureJsonObject.put("basicDataPath", basic_data_file_path);

        failureJsonObject.put("modifyCost", 0.01);
        failureJsonObject.put("addCost", 0.1);
        failureJsonObject.put("deleteCost", 4000);
        failureJsonObject.put("deleteCost2", 2);
        failureJsonObject.put("deleteEndCost", 1000000);

        List<JSONObject> failList = new ArrayList<>();
        failureJsonObject.put("gantryIdList", failList);
    }

    private void addToList(List<JSONObject> list, String gantryHex,
                           String transTime) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gantryHex", gantryHex);
        jsonObject.put("transTime", transTime);
        list.add(jsonObject);
    }
}
