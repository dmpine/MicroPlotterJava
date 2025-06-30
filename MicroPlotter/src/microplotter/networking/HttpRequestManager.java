package microplotter.networking;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import microplotter.utils.AppLogger;

/**
 * @brief Utility class to handle sending HTTP POST requests.
 */
public class HttpRequestManager {

    /**
     * @brief Sends data to a specified URL via an HTTP POST request.
     * @details This method runs on a new thread to avoid freezing the GUI.
     * @param urlString The target URL to send the request to.
     * @param jsonData The data to send, typically in JSON format.
     */
    public static void sendPostRequest(String urlString, String jsonData) {
        // Run network operations on a separate thread to keep the UI responsive
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Check the response code to confirm success
                int responseCode = conn.getResponseCode();
                System.out.println("HTTP POST Response Code :: " + responseCode);
                
                conn.disconnect();

            } catch (Exception e) {
            	AppLogger.severe("HTTP Request failed: " + e.getMessage(), e);
            }
        }).start();
    }
}