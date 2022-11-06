package io.flybird.platform.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {
  public static String sendGet(String url) throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection)obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = con.getResponseCode();
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    StringBuffer response = new StringBuffer();
    String inputLine;
    while ((inputLine = in.readLine()) != null)
      response.append(inputLine); 
    in.close();
    return response.toString();
  }
}
