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
        System.out.println("Starte Syncronisation :)");
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

            String responetextIUK = RequetsGet.requetsData("https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + token1);
            String responetextWST = RequetsGet.requetsData("https://app.divera247.com/api/v2/pull/vehicle-status?accesskey=" + token2);
            //System.out.println(responetextIUK);
            //System.out.println(responetextWST);
            Files.writeString(Path.of("einheit2.json"), responetextIUK, StandardCharsets.UTF_8);
            Iterator listWST = null;
            try {
                listWST = diveraDataList(responetextWST);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Iterator listIUK = null;
            try {
                listIUK = diveraDataList(responetextIUK);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                checkWST(listWST, listIUK);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void checkWST(Iterator wstInput, Iterator iukInput) throws ParseException, IOException {
        String issiWST;
        String nameWST;
        Long statusWST;
        Long tsWST;

        while (wstInput.hasNext()) {
            JSONObject slide = (JSONObject) wstInput.next();
            issiWST = (String) slide.get("issi");
            nameWST = (String) slide.get("name");
            statusWST = (Long) slide.get("fmsstatus");
            tsWST = (Long) slide.get("fmsstatus_ts");
            //System.out.println("Einheit 1: " + issiWST + " | " + nameWST + " | " + statusWST + " | " + tsWST);
            checkDifferncesDivera.IUK(iukInput, issiWST, nameWST, statusWST, tsWST);
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

