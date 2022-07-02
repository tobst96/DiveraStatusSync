import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class TetraControl {

    public static String platterRespone(String responeText) throws ParseException {
        System.out.println("Platter Json: " + responeText);
        JSONParser parser = new JSONParser();
        Object ob = (Object) parser.parse(responeText);

        // typecasting ob to JSONObject
        JSONObject js = (JSONObject) ob;
        System.out.println("JSON Objekt: " + js);
        String val = (String) js.get("c");
        System.out.println("Value: " + val);
        return val;
    }

}
