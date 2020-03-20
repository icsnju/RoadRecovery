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

    String test_data_file_path = "src/test/resources/inputs/test-data" +
            "-20200317-02.txt";

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
            jsonObject.getString("id");
            System.out.print("id = " + jsonObject.getString("id") + ": ");
            jsonObject.getString("passid");
//            jsonObject.getString("pathinfo");
            jsonObject.getString("truePath");

            PathRestoration pathRestoration = new PathRestoration();
            jsonObject.put("basicDataPath", basic_data_file_path);
            String returnedJSONString = pathRestoration.pathRestorationMethod(jsonObject.toString());
            JSONObject returnedJSONObject = new JSONObject(returnedJSONString);

            //compare path info and returnedJSONString
            boolean same = pathRestoration.compare(Integer.parseInt(jsonObject.getString("id")));
            if (same) {
                System.out.println("相同");
            }
            else {
                System.out.println("不同");
            }

//            if (count > 0) break;
        }

        //Close the input stream
        fileInputStream.close();
    }

    @Test
    public void testPathRestorationMethod() {
        getInput();

        PathRestoration pathRestoration = new PathRestoration();

        String returnString = pathRestoration.pathRestorationMethod(successJsonObject.toString());
        System.out.println(returnString);

//        returnString = pathRestoration.pathRestorationMethod(failureJsonObject.toString());
//        System.out.println(returnString);

        //assert some properties
    }

    @Test
    public void getInput() {
        /***{
        // "deleteEndCost":10000,
        // "modifyCost":0.01,
        // "addCost":0.1,
        // "deleteCost":0.6,

        // "enStationId":"S0019370010080",
        // "exStationId":"G2011370010010",
        // "enTime":"2020-01-22 11:39:03",
        // "exTime":"2020-01-22 12:06:05",

        // "gantryGroup":"3C581B|3D5819|3D7510|3D750C",
        // "truePath":"城阳|城阳-城阳南虚收费门架|城阳南虚-夏庄虚收费门架|夏庄虚-李村虚收费门架|李村虚-青岛东收费门架|青岛东"}
         ***/
        //manually curate a successful JSON data
        //case 10026
        successJsonObject.put("enStationId", "");
        successJsonObject.put("exStationId", "G2011370010010");
        successJsonObject.put("enTime",      "");
        successJsonObject.put("exTime",      "2020-01-22 12:06:05");

        successJsonObject.put("basicDataPath", basic_data_file_path);

        successJsonObject.put("modifyCost", 0.01);
        successJsonObject.put("addCost", 0.1);
        successJsonObject.put("deleteCost", 0.6);
        successJsonObject.put("deleteEndCost", 10000);

        List<Map<String, String>> gantryIDList = new ArrayList<>();
        addToList(gantryIDList, "3C581B", "2020-01-22 11:50:00");
        addToList(gantryIDList, "3D5819", "");
        addToList(gantryIDList, "3D7510", "");
        addToList(gantryIDList, "3D750C", "2020-01-22 12:05:00");
        successJsonObject.put("gantryIdList", gantryIDList);
        System.out.println(gantryIDList);

        System.out.println(successJsonObject.toString());

        //manually curate a failure JSON data
    }

    private void addToList(List<Map<String, String>> list, String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        list.add(map);
    }
}
