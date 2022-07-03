public class sendStatusDivera {
    public static void send(String issi, Long status, String token){
        try {
            String[] words = issi.split(",");
            for (String word : words) {
                String url2 = "https://www.divera247.com/api/fms?status_id=" + status + "&vehicle_issi=" + word + "&accesskey=" + token;
                String responetextEinh2 = RequetsGet.requetsData(url2);
            }
        } catch (Exception e) {
            String urlok = "https://www.divera247.com/api/fms?status_id=" + status + "&vehicle_issi=" + issi + "&accesskey=" + token;
            String responetextEinh2 = RequetsGet.requetsData(urlok);
            throw new RuntimeException(e);
        }


    }
}
