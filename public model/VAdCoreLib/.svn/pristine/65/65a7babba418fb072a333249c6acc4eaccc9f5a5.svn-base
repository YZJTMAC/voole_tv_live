package com.vad.sdk.core.Utils.v30;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelp {
  public String Get(String url) throws IOException {
    URL mUrl = new URL(url);
    HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);
    connection.setRequestMethod("GET");
    connection.connect();
    int responseCode = connection.getResponseCode();
    Lg.d("VADSDK=======>HttpHelp=========>Get responseCode " + responseCode);
    if (responseCode <= 299 && 200 <= responseCode) {
      InputStream inputStream = connection.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String response = "";
      String readLine = null;
      while ((readLine = br.readLine()) != null) {
        // response = br.readLine();
        response = response + readLine;
      }
      inputStream.close();
      br.close();
      connection.disconnect();
      return response;
    } else {
      throw new IOException("Unexpected code " + connection.getResponseCode());
    }
  }
}
