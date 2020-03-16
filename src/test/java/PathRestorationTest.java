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

        String returnString, oracle;

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
//        returnString = pathRestoration.pathRestorationMethod(testString);
//        System.out.println(returnString);

        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000," +
                "\"modifyCost\":0.01,\"addCost\":0.1," +
                "\"enStationId\":\"G0321370090040\",\"timeGroup\":\"\"," +
                "\"testIndex\":0,\"exStationId\":\"G0321370090020\"," +
                "\"enTime\":\"2020-01-04 12:10:02\"," +
                "\"basicDataPath\":\"src/test/resources/basic-data.xls\"," +
                "\"exTime\":\"2020-01-23 15:32:36\",\"deleteCost\":0.6,\"gantryGroup\":\"3D7C0E|3C7C02|3D7C0F|3C7C01|3D7C10|3C7C08|3D7C11|3C7C07|3D7C12|3C7C06|3D3B0D|3D3B0C|3D3B0B|3D3B08|3D3B06|3D3B03|3D3B01|3D1C06\"}";
//        returnString = pathRestoration.pathRestorationMethod(testString);
//        System.out.println(returnString);

//1. ID:9868	PASSID:320101610135231020200122072000  
        testString =
            "{\"typeGroup\":\"\",\"deleteEndCost\":10000," + "\"modifyCost\":0.01,\"addCost\":0.1,"
                + "\"enStationId\":\"G0015370080070\",\"timeGroup\":\"\","
                + "\"testIndex\":0,\"exStationId\":\"G0025370030010\","
                + "\"enTime\":\"2020-01-22 07:20:00\","
                + "\"basicDataPath\":\"src/test/resources/basic-data.xls\","
                + "\"exTime\":\"2020-01-22 10:57:43\",\"deleteCost\":0.6,\"gantryGroup\":\"3D2509|3C2401|3C2402|3D240E|3C2404|3D240C|3C2407|3C2408|3C2B01|3C2B02|3C7001|3C7002|3C7003|3C7101|3C7102|3C7103|3C7207|3C7209|3C720B|3C7301|3C4004|3C4005|3C4006|3C3F01|3C3F02|3C3F03|3C3F04|3C3F0B|3D3E08|3C3E02|3D510C\"}";
        oracle = "福山|福山-福山虚收费门架|福山虚-大杨家枢纽收费门架|大杨家枢纽-中桥虚收费门架|中桥虚-臧家庄虚收费门架|臧家庄虚-沈海蓬栖枢纽收费门架|沈海蓬栖枢纽-栖霞北虚收费门架|栖霞北虚-栖霞虚收费门架|栖霞虚-栖霞南虚收费门架|栖霞南虚-莱阳虚收费门架|莱阳虚-河头店虚收费门架|河头店虚-高格庄枢纽收费门架| 高格庄枢纽-莱西枢纽收费门架|莱西枢纽-牛溪埠虚收费门架|牛溪埠虚-院上虚收费门架|院上虚-仁兆虚收费门架|仁兆虚-南村临枢纽收费门架|南村临枢纽-南村虚收费门架|南村虚-马店枢纽收费门架|马店枢纽-沈海胶州虚收费门架|沈海胶州虚-九龙虚收费门架|九龙虚-王台虚收费门架|王台虚-胶南枢纽收费门架|胶南枢纽-里岔虚收费门架|里岔虚-辛兴虚收费门架|辛兴虚-诸城东虚收费门架|诸城东虚-诸城虚收费门架|诸城虚-诸城枢纽收费门架| 诸城枢纽-诸城西虚收费门架|诸城西虚-孟疃虚收费门架|孟疃虚-青兰杨庄虚收费门架|青兰杨庄虚-马站枢纽收费门架|马站枢纽-沂水北虚收费门架|沂水北虚-沂水北收费门架|沂水北";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//2.ID:9844	PASSID:320101610132557420200122064450   
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G2011370010020\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G2011370030010\",\"enTime\":\"2020-01-22 06:44:50\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 07:28:31\",\"deleteCost\":0.6,\"gantryGroup\":\"3D750C|3C750F|3C7511|3C7513|3C7601|3C7602|3C7603|3C7604|3C7712|3C7714\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//3.ID:9710	PASSID:320101610123373520200123132320  
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"S0019370010080\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G2011370030030\",\"enTime\":\"2020-01-23 13:23:20\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-23 14:39:06\",\"deleteCost\":0.6,\"gantryGroup\":\"3C581B|3D5819|3C7511|3C7513|3C7601|3C7602|3C7603|3C7604|3C7712|3C7714|3C770C|3C770E\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//4.ID:9674	PASSID:320101610121371620200104121002 
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0321370090040\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G0321370090020\",\"enTime\":\"2020-01-04 12:10:02\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-23 15:32:36\",\"deleteCost\":0.6,\"gantryGroup\":\"3D7C0E|3C7C02|3D7C0F|3C7C01|3D7C10|3C7C08|3D7C11|3C7C07|3D7C12|3C7C06|3D3B0D|3D3B0C|3D3B0B|3D3B08|3D3B06|3D3B03|3D3B01|3D1C06\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//5.ID:9398	PASSID:320101610106673420200123205952 
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0015370030010\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"S0016370010010\",\"enTime\":\"2020-01-23 20:59:52\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-24 08:54:59\",\"deleteCost\":0.6,\"gantryGroup\":\"3C7002|3C7003|3C7101|3C7102|3C7103|3C7207|3C7209|3C720B|3C7301|3C7302|3C7303|3C7304|3C7305|3C2201|3C2202|3C2203|3D220D|3D220E|3D7306|3D7307|3C7303|3D7309|3D730A|3D720C|3D720A|3D7208|3D7104|3D7105|3C7712|3C7714|3C770C|3D010B|3D010C|3D010D|3D010E\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//6.ID:9073	PASSID:320101610089614420200123164147 
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0035370030050\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G0035370030060\",\"enTime\":\"2020-01-23 16:41:47\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-23 18:54:38\",\"deleteCost\":0.6,\"gantryGroup\":\"3D200A|3C2004|3D200C|3D200C|3C2004|3C2005|3C2006|3C2007\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//7.ID:9021	PASSID:320101610086512620200122113903
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"S0019370010080\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G2011370010010\",\"enTime\":\"2020-01-22 11:39:03\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 12:06:05\",\"deleteCost\":0.6,\"gantryGroup\":\"3C581B|3D5819|3D7510|3D750C\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//8.ID:8460	PASSID:320101610057139120200124090624  
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"S0019370040030\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"S0019370040040\",\"enTime\":\"2020-01-24 09:06:24\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-24 10:28:36\",\"deleteCost\":0.6,\"gantryGroup\":\"3D6C08|3D6C09|3D6C0A|3D7402|3C6C01|3C6C02|3C6C03|3C6C04|3C6C05\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//9.ID:8402	PASSID:320101610054413320200122123316   
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0015370080030\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G0015370080010\",\"enTime\":\"2020-01-22 12:33:16\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 16:59:40\",\"deleteCost\":0.6,\"gantryGroup\":\"3D250E|3D250F|3D2510|3D4F04|3D4F05|3D4F06|3D4E03|3D4E04|3C4E02|3C4F01|3C4F02|3C4F03|3C2501\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//10.ID:6418	PASSID:320101600117201320200122143713  
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0022370050080\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G0022370050040\",\"enTime\":\"2020-01-22 14:37:13\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 15:31:03\",\"deleteCost\":0.6,\"gantryGroup\":\"3C7805|3C7806|3D7808|3D7809|3D780A|3D780B|3D780C\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//11.ID:6043	PASSID:320101600090314920200122134442  
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"S7201370010010\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"S0001370020020\",\"enTime\":\"2020-01-22 13:44:42\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 16:42:43\",\"deleteCost\":0.6,\"gantryGroup\":\"3C360D|3C3607|3C6401|3C6402|3C301B|3C301C|3C301D|3C301E|3C301F|3C3020|3C6201|3C6202|3C5413|3D6505|3D6506|3D6507|3D6508|3C1B01|3C1B02|3C1B03|3C1B04|3C1A01|3C1A02|3C1A03|3C1A04\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//12.ID:25	PASSID:110101610003981320200122160411
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G003W370030010\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G003W370030030\",\"enTime\":\"2020-01-22 16:04:11\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-23 08:31:34\",\"deleteCost\":0.6,\"gantryGroup\":\"3D5F07|3D5F08|3D5F09|3D5F0A|3D6009|3D600A|3C6008|3C5F01|3C5F02|3C5F02\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//13.ID:392	PASSID:110101840000661820200122072159
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0018370160010\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G1511370010040\",\"enTime\":\"2020-01-22 07:21:59\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 15:18:53\",\"deleteCost\":0.6,\"gantryGroup\":\"3C0401|3C0402|3C0403|3C0404|3C0405|3C0406|3C4305|3C4301|3C4302|3D420F|3C4202|3D420D|3C4204|3C4205|3C4206|3C4207|3C4208|3C4101|3C4102|3C4103|3C4104|3C4105|3C4106|3C1202|3C1203|3D1207|3C7207|3C7209|3C7301|3C4004|3C4005|3C4006|3C3F01|3C3F02|3C3F03|3C3F04|3C3F0B|3C3E01|3C3E02|3D510C|3C5102|3C5103|3C5104|3C5105|3C5106|3D1D08\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//14.ID:1136	PASSID:120101650010431220200122103202
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"S0019370010080\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G2011370030030\",\"enTime\":\"2020-01-22 10:32:02\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 11:42:16\",\"deleteCost\":0.6,\"gantryGroup\":\"3C581B|3D5819|3C7511|3C7513|3C7601|3C7602|3C7603|3C7604|3C7712|3C7714|3C770C|3C770E\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//15.ID:1453	PASSID:120101650029343020200122130216
//        testString = "";
//        returnString = pathRestoration.pathRestorationMethod(testString);
//        System.out.println(returnString);

//16.ID:1683	PASSID:120101650037952620200122134404
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0025370050020\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G0035370030040\",\"enTime\":\"2020-01-22 13:02:16\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 18:43:51\",\"deleteCost\":0.6,\"gantryGroup\":\"3C0E05|3C3B17|3C3B13|3C3B11|3C3B10|3C3B0F|3C3B0E|3D200B\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//17.ID:1808	PASSID:120101650046227620200122072748
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"S7201370010010\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"G0018370120020\",\"enTime\":\"2020-01-22 07:27:48\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-22 12:38:52\",\"deleteCost\":0.6,\"gantryGroup\":\"3C360D|3D3503|3D3702|3D2E0C|3D2E0B|3D2E0A|3D2E09|3D2E08|3D4408|3C2E05|3C2E04|3C2E03\"}";
        returnString = pathRestoration.pathRestorationMethod(testString);
        System.out.println(returnString);

//18.ID:2739	PASSID:130101720013015220200123112438
        testString = "{\"typeGroup\":\"\",\"deleteEndCost\":10000,\"modifyCost\":0.01,\"addCost\":0.1,\"enStationId\":\"G0018370120040\",\"timeGroup\":\"\",\"testIndex\":0,\"exStationId\":\"S7201370010010\",\"enTime\":\"2020-01-23 11:24:38\",\"basicDataPath\":\"src/test/resources/basic-data.xls\",\"exTime\":\"2020-01-23 17:27:34\",\"deleteCost\":0.6,\"gantryGroup\":\"3D2E08|3D4408|3D4408|3C2E05|3C2E04|3C2E03|3C2E02|3C2E01|3C3701|3C3501\"}";
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
