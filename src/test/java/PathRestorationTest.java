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

//        String testString = "{\"typeGroup\":\"\",\"exStationId\":\"S0029370010010\",\"enTime\":\"2020-01-22 11:19:19\"," +
//                "\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"\",\"enStationId\":\"\",\"timeGroup\":\"\"," +
//                "\"gantryGroup\":\"3E3001|3D3012|3C3004|3C3005|3C3006|3C3007|3C3008|3C3009|3C3107\",\"testIndex\":0}";
//        returnString = pathRestoration.pathRestorationMethod(testString);
//        System.out.println(returnString);

//        returnString = pathRestoration.pathRestorationMethod(successJsonObject.toString());
//        System.out.println(returnString);
//
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

//        System.out.println(failureJsonObject.toString());
    }
}
