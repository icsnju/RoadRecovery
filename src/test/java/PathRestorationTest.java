import nju.ics.Main.PathRestoration;
import org.json.JSONObject;
import org.junit.Test;

public class PathRestorationTest {

    static JSONObject successJsonObject = new JSONObject();
    static JSONObject failureJsonObject = new JSONObject();
    static String basic_data_file_path = "src/test/resources/basic-data.xls";

    static {
        getInput();
    }

    @Test
    public void testPathRestorationMethod() {

        //execute the method under test
        PathRestoration pathRestoration = new PathRestoration();

        String returnString;

        String testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0015370080070\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G0025370030010\",\"enTime\":\"2020-01-22 07:20:00\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"\",\"deleteCost\":0.6,\"gantryGroup\":\"3D2509|3C2401|3C2402|3D240E|3C2404|3D240C|3C2407|3C2408|3C2B01|3C2B02|3C7001|3C7002|3C7003|3C7101|3C7102|3C7103|3C7207|3C7209|3C720B|3C7301|3C4004|3C4005|3C4006|3C3F01|3C3F02|3C3F03|3C3F04|3C3F0B|3D3E08|3C3E02|3D510C\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);
//
        returnString = pathRestoration.pathRestorationMethod(successJsonObject.toString());
        System.out.println(returnString);

        returnString = pathRestoration.pathRestorationMethod(failureJsonObject.toString());
        System.out.println(returnString);

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

        System.out.println(successJsonObject.toString());

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
