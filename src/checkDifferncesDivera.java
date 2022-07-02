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
    public static void IUK(Iterator iukInput, String issiWST, String nameWST, Long statusWST, Long tsWST) throws ParseException, IOException {
        String responetextIUK = Files.readString(Path.of("einheit2.json"), StandardCharsets.UTF_8);
        Iterator listIUK = Main.diveraDataList(responetextIUK);
        while (listIUK.hasNext()) {
            JSONObject slide2 = (JSONObject) listIUK.next();
            String issiIUK =  (String) slide2.get("issi");
            String nameIUK = (String) slide2.get("name");
            Long statusIUK = (Long) slide2.get("fmsstatus");
            Long tsIUK = (Long) slide2.get("fmsstatus_ts");
            checkSame(issiIUK, nameIUK, statusIUK, tsIUK, issiWST, nameWST, statusWST, tsWST);

        }
    }
    public static void checkSame(String issiIUK, String nameIUK, Long statusIUK, Long tsIUK, String issiWST, String nameWST, Long statusWST, Long tsWST){
        if (issiIUK.equals(issiWST)) {
            //System.out.println("Gleiches Fahrzeug gefunden!");

            if (statusIUK != statusWST) {
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
                if (tsIUK > tsWST) {
                    //Sende status von IUK
                    System.out.println("Sende status von IUK");
                    System.out.println("Sende Daten an Einheit2: " + issiIUK + " | " + nameIUK + " | " + statusIUK + " | " + tsIUK);
                    sendStatusDivera.send(issiIUK, statusIUK, token2);
                } else {
                    //sende status von WST
                    System.out.println("Sende status von WST");
                    System.out.println("Sende Daten an Einheit1: " + issiIUK + " | " + issiWST + " | " + statusWST + " | " + tsWST);
                    sendStatusDivera.send(issiWST, statusWST, token1);
                }
            }
        }
    }
}
