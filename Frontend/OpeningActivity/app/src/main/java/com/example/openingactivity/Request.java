package com.example.openingactivity;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public interface Request {
    // URL variable
    final String URL = "http://10.90.74.200:8080";
    //final String URL = "http://localhost:8080";
    //final String URL = "https://bdf879e6-93fa-44fc-aac2-954a4fe85a4a.mock.pstmn.io"; // POSTMAN URL for testing

    /*
     * Method to send ANY Request
     * @param requestType: GET, POST, PUT, DELETE
     * @param urlString: URL string add on for the request, all start with "/"
     * @param jsonData: JSON data to send with the request, put "null" if not using json (for get or delete)
     * @return: String response from the request
     */
    default String sendRequest(String requestType, String urlString, String jsonData) {
        try {
            java.net.URL url = new URL(URL + urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestType);

            if (requestType.equals("POST") || requestType.equals("PUT")) {
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setDoOutput(true);


                // Write JSON data to the output stream
                OutputStream os = urlConnection.getOutputStream();
                os.write(jsonData.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            urlConnection.disconnect();

            String result = content.toString();
            Log.d(requestType + " RESPONSE: ", result); // Log the response for debugging
            return result;

        } catch (Exception e) {
            Log.e(requestType + " ERROR", e.getMessage(), e); // Log any errors
            e.printStackTrace();
            // return in Exception
            return null;
        }
    }

    default String sendDeezerRequest(String requestType, String urlString) {

        try {
            java.net.URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestType);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            urlConnection.disconnect();

            String result = content.toString();
            Log.d(requestType + " RESPONSE: ", result); // Log the response for debugging
            return result;

        } catch (Exception e) {
            Log.e(requestType + " ERROR", e.getMessage(), e); // Log any errors
            e.printStackTrace();
            // return in Exception
            return null;
        }

    }
}

