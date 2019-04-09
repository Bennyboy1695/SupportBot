package uk.co.netbans.supportbot.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Hastebin {

    @Nonnull
    public static String post(String data) {
        String link = "";
        try {
            String pasteURL = "https://hastebin.com/";
            HttpURLConnection conn = (HttpURLConnection) new URL(pasteURL + "documents").openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);

            conn.connect();
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes());
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String json = reader.readLine();
                String real = json.substring(8, json.length() - 2);
                link = pasteURL + real;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return link;
    }

}
