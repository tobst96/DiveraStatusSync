import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;

class checkDifferncesDivera {
    public static void Einh1(Iterator Einh1Input, String issiEinh2, String nameEinh2, Long statusEinh2, Long tsEinh2) throws ParseException, IOException {
        String responetextEinh1 = Files.readString(Path.of("einheit2.json"), StandardCharsets.UTF_8);
        Iterator listEinh1 = Main.diveraDataList(responetextEinh1);
        while (listEinh1.hasNext()) {
            JSONObject slide2 = (JSONObject) listEinh1.next();
            String issiEinh1 =  (String) slide2.get("issi");
            String nameEinh1 = (String) slide2.get("name");
            Long statusEinh1 = (Long) slide2.get("fmsstatus");
            Long tsEinh1 = (Long) slide2.get("fmsstatus_ts");
            checkSame(issiEinh1, nameEinh1, statusEinh1, tsEinh1, issiEinh2, nameEinh2, statusEinh2, tsEinh2);

        }
    }
    public static void checkSame(String issiEinh1, String nameEinh1, Long statusEinh1, Long tsEinh1, String issiEinh2, String nameEinh2, Long statusEinh2, Long tsEinh2){
        if (issiEinh1.equals(issiEinh2)) {
            //System.out.println("Gleiches Fahrzeug gefunden!");

            if (statusEinh1 != statusEinh2) {
                //Config read
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

                //Config Read ende
                if (tsEinh1 > tsEinh2) {
                    //Sende status von Einh1
                    System.out.println("Sende status von Einh1");
                    System.out.println("Sende Daten an Einheit2: " + issiEinh1 + " | " + nameEinh1 + " | " + statusEinh1 + " | " + tsEinh1);
                    sendStatusDivera.send(issiEinh1, statusEinh1, token2);
                } else {
                    //sende status von Einh2
                    System.out.println("Sende status von Einh2");
                    System.out.println("Sende Daten an Einheit1: " + issiEinh1 + " | " + issiEinh2 + " | " + statusEinh2 + " | " + tsEinh2);
                    sendStatusDivera.send(issiEinh2, statusEinh2, token1);
                }
            }
        }
    }
}
