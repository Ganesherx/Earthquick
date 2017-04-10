package com.example.android.quakereport;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Sample JSON response for a USGS query
     */

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    /**
     * Returns new URl object from the given string url
     **/
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the Url", e);
        }

        Log.e(LOG_TAG, "creating url:" + url);
        return url;

    }

    private static String makeHttpRequest(URL url) throws IOException {
        Log.e(LOG_TAG, "makeHttpRequest:");
        String jsonResponse = "";
        //If the url is null , then return early
        if (url == null) {
            return jsonResponse;
        }

        Log.e(LOG_TAG, "makeHttprequest  url:" + url);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            Log.e(LOG_TAG, "http response:" + urlConnection.getResponseCode());

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        List<Earthquake> earthquakes = new ArrayList<>();

        /**
         * JSON parsing
         */
        try {

            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            Log.e(LOG_TAG, "baseJsonResponse:" + baseJsonResponse);

            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            for (int i = 0; i < earthquakeArray.length(); i++) {

                JSONObject currentEartquake = earthquakeArray.getJSONObject(i);
                JSONObject propertise = currentEartquake.getJSONObject("properties");
                double magnitude = propertise.getDouble("mag");
                String location = propertise.getString("place");
                long time = propertise.getLong("time");
                String url = propertise.getString("url");

                Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                earthquakes.add(earthquake);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results.", e);
        }

        return earthquakes;
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

        Log.i(LOG_TAG, "TEST: fetchEarthquakeData() called...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTO request.", e);
        }

        Log.e(LOG_TAG, "fetchEartquakedata.jsonresponse:" + jsonResponse);

        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        return earthquakes;
    }
}