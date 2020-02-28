import org.json.JSONObject;
import org.junit.Test;

public class PathRestorationTest {

    static JSONObject successJsonObject = new JSONObject();
    static JSONObject failureJsonObject = new JSONObject();

    static {
        getInput();
    }

    @Test
    public void testPathRestorationMethod() {
        String basic_data_file_path = "src/test/resources/basic-data.xls";

        //TODO: execute the method under test
        PathRestoration pathRestoration = new PathRestoration();


        String returnString;
        returnString = pathRestoration.pathRestorationMethod(successJsonObject.toString(), basic_data_file_path);
        System.out.println(returnString);

//        returnString = pathRestoration.pathRestorationMethod(failureJsonObject.toString(), basic_data_file_path);
//        System.out.println(returnString);

        //TODO: assert some properties
    }

    private static void getInput() {
        //TODO: manually curate a successful JSON data
        //case 10026
        successJsonObject.put("enStationId", "S0033370020050");
        successJsonObject.put("exStationId", "S0033370010030");
        successJsonObject.put("enTime",      "2020-02-26 08:01:15");
        successJsonObject.put("exTime",      "2020-02-26 08:58:19");
        successJsonObject.put("gantryGroup", "3D6105|3C610A|3D6104|3D6103|3C610D|3D6101|3D5311");
        successJsonObject.put("typeGroup",   "0|0|0|0|0|0|0");
        successJsonObject.put("timeGroup",   "");

        System.out.println(successJsonObject.toString());

        //TODO: manually curate a failure JSON data
//        failureJsonObject.put("enStationId", "S0033370020050");
        failureJsonObject.put("enStationId", "");
        failureJsonObject.put("exStationId", "S0033370010030");
        failureJsonObject.put("enTime",      "2020-02-26 08:01:15");
        failureJsonObject.put("exTime",      "2020-02-26 08:58:19");
        failureJsonObject.put("gantryGroup", "3D6105|12|3D6104|3D6103|3C610D|3D6101|3D5311");
        failureJsonObject.put("typeGroup",   "0|0|0|0|0|0|0");
        failureJsonObject.put("timeGroup",   "");

//        System.out.println(failureJsonObject.toString());
    }
}
