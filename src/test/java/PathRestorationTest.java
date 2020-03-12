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

        String testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"G003W370030010\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"G003W370030030\"," +
                "\"enTime\":\"2020-01-22 16:04:11\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"\"," +
                "\"deleteCost\":0.6,\"gantryGroup\":\"3D5F07|3D5F08|3D5F09|3D5F0A|3D6009|3D600A|3C6008|3C5F01|3C5F02|3C5F02\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);
//
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
