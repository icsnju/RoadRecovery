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

        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"S0019370010080\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"G2011370030030\"," +
                "\"enTime\":\"2020-01-23 13:23:20\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"2020-01-23 14:39:06\",\"deleteCost\":0.6,\"gantryGroup\":\"3C581B|3D5819|3C7511|3C7513|3C7601|3C7602|3C7603|3C7604|3C7712|3C7714|3C770C|3C770E\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"G0321370090040\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"G0321370090020\"," +
                "\"enTime\":\"2020-01-04 12:10:02\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"2020-01-23 15:32:36\",\"deleteCost\":0.6,\"gantryGroup\":\"3D7C0E|3C7C02|3D7C0F|3C7C01|3D7C10|3C7C08|3D7C11|3C7C07|3D7C12|3C7C06|3D3B0D|3D3B0C|3D3B0B|3D3B08|3D3B06|3D3B03|3D3B01|3D1C06\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"3D1C06\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"G0321370090020\"," +
                "\"enTime\":\"2020-01-04 12:10:02\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"2020-01-23 15:32:36\",\"deleteCost\":0.6,\"gantryGroup\":\"\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);


        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"3D3B01\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"3D1C06\"," +
                "\"enTime\":\"2020-01-04 12:10:02\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"2020-01-23 15:32:36\",\"deleteCost\":0.6,\"gantryGroup\":\"\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"3D1C06\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"3C1C05\"," +
                "\"enTime\":\"2020-01-04 12:10:02\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"2020-01-23 15:32:36\",\"deleteCost\":0.6,\"gantryGroup\":\"\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"3D3B01\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"3D1C06\"," +
                "\"enTime\":\"2020-01-04 12:10:02\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"2020-01-23 15:32:36\",\"deleteCost\":0.6,\"gantryGroup\":\"\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);
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
