package uk.co.netbans.supportbot.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Hastebin {

    @Nonnull
    public static String post(String data) throws UnirestException {
        HttpResponse response = Unirest.post("https://hastebin.com/documents").body(data).asString();
        System.out.println(response.getBody().toString());
        return "https://hastebin.com/" + new JSONObject(response.getBody().toString()).getString("key");
    }

    private static String pasteURL = "http://hastebin.com/";

    public synchronized static String paste(String urlParameters) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(pasteURL + "documents");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            System.out.println(rd.lines());
            return pasteURL + new JSONObject(rd.readLine()).getString("key");

        } catch (IOException e) {
            return null;
        } finally {
            if (connection == null) return null;
            connection.disconnect();
        }
    }

    public static String getPasteURL() {
        return pasteURL;
    }

    public static void setPasteURL(String URL) {
        pasteURL = URL;
    }

    public static synchronized String getPaste(String ID) {
        String URLString = pasteURL + "raw/" + ID + "/";
        try {
            URL URL = new URL(URLString);
            HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String paste = "";
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.contains("package")) continue;
                if (paste.equals("")) paste = line;
                else paste = paste + "\n" + line;
            }
            return paste;
        } catch (IOException e) {
            return "";
        }
    }
}
