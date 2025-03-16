package com.siteminder.util;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

public class HttpClientUtil {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static String sendPostRequest(String targetUrl, String requestBody, String apiKey) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(targetUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Check the response status
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            // Log the error response for debugging
            String errorMessage = String.format("Failed request to %s: [%d] %s", targetUrl, response.statusCode(), response.body());
            throw new IOException(errorMessage); // Throw an IOException with details on failure
        }
        return response.body();
    }
}