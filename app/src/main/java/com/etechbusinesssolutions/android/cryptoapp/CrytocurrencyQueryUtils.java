package com.etechbusinesssolutions.android.cryptoapp;

import android.text.TextUtils;
import android.util.Log;

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

import static com.etechbusinesssolutions.android.cryptoapp.BtcFragment.LOG_TAG;

/**
 * Created by george on 10/10/17.
 */

public class CrytocurrencyQueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link CrytocurrencyQueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private CrytocurrencyQueryUtils() {

    }


    private  static List<Currency> extractFeatureFromJson (String cryptoJSON) {

        // if the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(cryptoJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding currency values
        List<Currency> cryptoValues = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(cryptoJSON);

            // Iterate over the returned object and get the keys and values
            // and add that to the List
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                String currency = baseJsonResponse.keys().next();
                long value = baseJsonResponse.getLong(currency);

                // Create a new {@link Currency} object with the key, and value
                //TODO: insert real image id values
                Currency cur = new Currency(currency, value, 3);

                // Add the new {@link Currency} object to the list of currencies
                cryptoValues.add(cur);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("GithubQueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of currencies
        return cryptoValues;

    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the url is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

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
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);

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

        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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


    public static List<Currency> fetchCurrencyData (String requestUrl) {

        Log.i(LOG_TAG, "TEST: fetchCurrencyData() called ...");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, "TEST: Url used to request data " + url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Currency}
        List<Currency> currency = extractFeatureFromJson(jsonResponse);


        // Return the list of {@link Github}
        return currency;
    }



}
