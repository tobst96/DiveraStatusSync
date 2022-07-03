import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;

public class Main extends Thread {
    public static void main(String[] args) throws ParseException, IOException {
        System.out.println("Starte Syncronisation");
        while(true) {
            Properties prop = new Properties();
            String fileName = "config.ini";
            try (FileInputStream fis = new FileInputStream(fileName)) {
                prop.load(fis);
            } catch (FileNotFoundException ex) {
                System.out.println("FileNotFoundException");
            } catch (IOException ex) {
                System.out.println("IOException ex");
            }
            String token1 = prop.getProperty("diveratoken1");
            String token2 = prop.getProperty("diveratoken2");

            String responetextEinh1 = RequetsGet.requetsData("https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + token1);
            String responetextEinh2 = RequetsGet.requetsData("https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + token2);
            //System.out.println(responetextEinh1);
            //System.out.println(responetextEinh2);
            Files.writeString(Path.of("einheit2.json"), responetextEinh1, StandardCharsets.UTF_8);
            Iterator listEinh2 = null;
            try {
                listEinh2 = diveraDataList(responetextEinh2);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Iterator listEinh1 = null;
            try {
                listEinh1 = diveraDataList(responetextEinh1);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                checkEinh2(listEinh2, listEinh1);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void checkEinh2(Iterator Einh2Input, Iterator Einh1Input) throws ParseException, IOException {
        String issiEinh2;
        String nameEinh2;
        Long statusEinh2;
        Long tsEinh2;

        while (Einh2Input.hasNext()) {
            JSONObject slide = (JSONObject) Einh2Input.next();
            issiEinh2 = (String) slide.get("issi");
            nameEinh2 = (String) slide.get("name");
            statusEinh2 = (Long) slide.get("fmsstatus");
            tsEinh2 = (Long) slide.get("fmsstatus_ts");
            //System.out.println("Einheit 1: " + issiEinh2 + " | " + nameEinh2 + " | " + statusEinh2 + " | " + tsEinh2);
            checkDifferncesDivera.Einh1(Einh1Input, issiEinh2, nameEinh2, statusEinh2, tsEinh2);
        }
    }

    public static Iterator diveraDataList(String responeText) throws ParseException {
        //System.out.println("Platter Json: " + responeText);
        JSONParser parser = new JSONParser();
        Object ob = (Object) parser.parse(responeText);

        // typecasting ob to JSONObject
        JSONObject js = (JSONObject) ob;
        //System.out.println("JSON Objekt: " + js);
        JSONArray val = (JSONArray) js.get("data");
        //System.out.println("Value: " + val);
        Iterator output = val.iterator();
        return output;
    }
}

