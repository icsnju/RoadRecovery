import nju.ics.Main.PathRestoration;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;

public class PathRestorationTest {

    static JSONObject successJsonObject = new JSONObject();
    static JSONObject failureJsonObject = new JSONObject();
    static String basic_data_file_path = "src/test/resources/inputs/basic" +
            "-data.xls";
    static String test_data_file_path = "src/test/resources/inputs/test-data" +
            "-20200317-02.txt";

    static {
        getInput();
    }

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
        //execute the method under test
        PathRestoration pathRestoration = new PathRestoration();

//        returnString = pathRestoration.pathRestorationMethod(successJsonObject.toString());
//        System.out.println(returnString);
//
//        returnString = pathRestoration.pathRestorationMethod(failureJsonObject.toString());
//        System.out.println(returnString);

        //assert some properties
    }

    private static void getInput() {
        //manually curate a successful JSON data
        //case 10026
        successJsonObject.put("enStationId", "S0033370020050");
        successJsonObject.put("exStationId", "S0033370010030");
        successJsonObject.put("enTime",      "2020-02-26 08:01:15");
        successJsonObject.put("exTime",      "2020-02-26 08:58:19");
        successJsonObject.put("gantryGroup", "3D6105|3C610A|3D6104|3D6103|3C610D|3D6101|3D5311");
        successJsonObject.put("typeGroup",   "0|0|0|0|0|0|0");
        successJsonObject.put("timeGroup",   "");
        successJsonObject.put("basicDataPath", basic_data_file_path);
        successJsonObject.put("testIndex", 0);

        successJsonObject.put("modifyCost", 0.01);
        successJsonObject.put("addCost", 0.1);
        successJsonObject.put("deleteCost", 10);
        successJsonObject.put("deleteEndCost", 10000);

//        System.out.println(successJsonObject.toString());

        //manually curate a failure JSON data
        failureJsonObject.put("enStationId", "");
        failureJsonObject.put("exStationId", "S0033370010030");
        failureJsonObject.put("enTime",      "2020-02-26 08:01:15");
        failureJsonObject.put("exTime",      "2020-02-26 08:58:19");
        failureJsonObject.put("gantryGroup", "");
        failureJsonObject.put("typeGroup",   "");
        failureJsonObject.put("timeGroup",   "");
        failureJsonObject.put("basicDataPath", basic_data_file_path);
        failureJsonObject.put("testIndex", 0);

        failureJsonObject.put("modifyCost", 0.01);
        failureJsonObject.put("addCost", 0.1);
        failureJsonObject.put("deleteCost", 10);
        failureJsonObject.put("deleteEndCost", 10000);

//        System.out.println(failureJsonObject.toString());
    }
}
