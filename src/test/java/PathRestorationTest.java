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
        successJsonObject.put("enStationId", "S0019370010080");
        successJsonObject.put("exStationId", "G2011370010010");
        successJsonObject.put("enTime",      "2020-01-22 11:39:03");
        successJsonObject.put("exTime",      "2020-01-22 12:06:05");

        successJsonObject.put("basicDataPath", basic_data_file_path);

        successJsonObject.put("modifyCost", 0.01);
        successJsonObject.put("addCost", 0.1);
        successJsonObject.put("deleteCost", 4000);
        successJsonObject.put("deleteCost2", 2);
        successJsonObject.put("deleteEndCost", 1000000);

        List<JSONObject> gantryIDList = new ArrayList<>();
//        addToList(gantryIDList, "3C581B", "2020-01-22 11:50:00");
//        addToList(gantryIDList, "3D5819", "");
//        addToList(gantryIDList, "3D7510", "");
//        addToList(gantryIDList, "3D750C", "2020-01-22 12:05:00");
//        successJsonObject.put("gantryIdList", gantryIDList);
//        System.out.println(gantryIDList);




        //manually curate a failure JSON data
//        successJsonObject.put("id", "9674");
//        successJsonObject.put("enStationId", "G0321370090040");
//        successJsonObject.put("exStationId", "G0321370090020");
//        successJsonObject.put("enTime", "2020-01-04 12:10:02");
//        successJsonObject.put("exTime", "2020-01-23 15:32:36");
//        addToList(gantryIDList, "3D7C0E", "1");
//        addToList(gantryIDList, "3C7C02", "2");
//        addToList(gantryIDList, "3D7C0F", "3");
//        addToList(gantryIDList, "3C7C01", "4");
//        addToList(gantryIDList, "3D7C10", "5");
//        addToList(gantryIDList, "3C7C08", "6");
//        addToList(gantryIDList, "3D7C11", "7");
//        addToList(gantryIDList, "3C7C07", "8");
//        addToList(gantryIDList, "3D7C12", "9");
//        addToList(gantryIDList, "3C7C06", "10");
//        addToList(gantryIDList, "3D3B0D", "11");
//        addToList(gantryIDList, "3D3B0C", "12");
//        addToList(gantryIDList, "3D3B0B", "13");
//        addToList(gantryIDList, "3D3B08", "14");
//        addToList(gantryIDList, "3D3B06", "15");
//        addToList(gantryIDList, "3D3B03", "16");
//        addToList(gantryIDList, "3D3B01", "17");
//        addToList(gantryIDList, "3D1C06", "18");
//        successJsonObject.put("gantryIdList", gantryIDList);

//        case 8460
//        successJsonObject.put("enStationId", "S0019370040030");
//        successJsonObject.put("exStationId", "S0019370040040");
//        successJsonObject.put("enTime", "2020-01-24 09:06:24");
//        successJsonObject.put("exTime", "2020-01-24 10:28:36");
//        addToList(gantryIDList, "3D6C08", "");
//        addToList(gantryIDList, "3D6C09", "");
//        addToList(gantryIDList, "3D6C0A", "");
//        addToList(gantryIDList, "3D7402", "");
//        addToList(gantryIDList, "3C6C01", "");
//        addToList(gantryIDList, "3C6C02", "");
//        addToList(gantryIDList, "3C6C03", "");
//        addToList(gantryIDList, "3C6C04", "");
//        addToList(gantryIDList, "3C6C05", "");
//        successJsonObject.put("gantryIdList", gantryIDList);

        successJsonObject.put("enStationId", "G0015370080030");
        successJsonObject.put("exStationId", "G0015370080010");
        successJsonObject.put("enTime", "2020-01-22 12:33:16");
        successJsonObject.put("exTime", "2020-01-22 16:59:40");
        addToList(gantryIDList, "3D250E", "");
        addToList(gantryIDList, "3D250F", "");
        addToList(gantryIDList, "3D2510", "");
        addToList(gantryIDList, "3D4F04", "");
        addToList(gantryIDList, "3D4F05", "");
        addToList(gantryIDList, "3D4F06", "");
        addToList(gantryIDList, "3D4E03", "");
        addToList(gantryIDList, "3D4E04", "");
        addToList(gantryIDList, "3C4E02", "");
        addToList(gantryIDList, "3C4F01", "");
        addToList(gantryIDList, "3C4F02", "");
        addToList(gantryIDList, "3C4F03", "");
        addToList(gantryIDList, "3C2501", "");
        successJsonObject.put("gantryIdList", gantryIDList);

        System.out.println(successJsonObject.toString());
    }

    private void addToList(List<JSONObject> list, String gantryHex,
                           String transTime) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gantryHex", gantryHex);
        jsonObject.put("transTime", transTime);
        list.add(jsonObject);
    }
}
