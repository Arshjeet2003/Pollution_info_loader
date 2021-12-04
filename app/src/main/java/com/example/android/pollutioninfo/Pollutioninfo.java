package com.example.android.pollutioninfo;

import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Pollutioninfo {

    private static final String LOG_TAG = Pollutioninfo.class.getSimpleName();
    public static String fetchPollutiondata(String JsonResponse) {
        String aqi = "";
        String response="";
        URL url = createUrl(JsonResponse);
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(response);
            JSONObject data = baseJsonResponse.getJSONObject("data");
            aqi = String.valueOf(data.getInt("aqi"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "problem in key", e);
        }
        return aqi;
    }
        private static URL createUrl(String stringUrl)
    {
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG , "Problem building the url", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if(url==null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection =null;
        InputStream inputStream = null;
    if(URLUtil.isValidUrl(String.valueOf(url)) && Patterns.WEB_URL.matcher(String.valueOf(url)).matches()) {
    try {
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // If the request was successful (response code 200),
        // then read the input stream and parse the response.
        if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } else {
            Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
        }
    } catch (IOException e) {
        Log.e(LOG_TAG, "problem receiving the earthquake JSON results", e);
    } finally {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (inputStream != null) {
            // Closing the input stream could throw an IOException, which is why
            // the makeHttpRequest(URL url) method signature specifies than an IOException
            // could be thrown.
            inputStream.close();
        }
    }
}
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream , Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}