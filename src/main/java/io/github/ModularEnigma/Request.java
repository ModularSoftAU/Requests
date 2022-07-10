package io.github.ModularEnigma;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class Request {
    private final String url;
    private final URI uri;
    private final Method requestMethod;
    private final String requestBody;
    private final Map<String, String> headers;

    public enum Method {
        GET, POST // Add others if desired
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public Request(String url, Method requestMethod, String requestBody, Map<String, String> headers) {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        } else if (requestMethod == null) {
            throw new IllegalArgumentException("Request method must be set");
        }

        try {
            this.uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("URI is not valid as per RFC 2396");
        }

        this.headers = headers;
        this.url = url;
        this.requestMethod = requestMethod;
        this.requestBody = requestBody;
    }

    /**
     * Execute the request.
     *
     * @return A {@link Response} instance that contains the response's body and status code.
     */
    public Response execute() {
        // System.out.println(this);
        // Setup headers
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");

        // Include additional headers if added
        for (Map.Entry<String,String> header : headers.entrySet()) {
            builder = builder.header(header.getKey(), header.getValue());
        }

            // Include the body of the Request
        switch (requestMethod) {
            case POST -> builder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
            case GET -> builder.GET();
        }

        // Send the request and listen for a response
        HttpRequest request = builder.build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> httpResponse;
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Exception raised while sending request.");
        }
        return new Response(httpResponse);
    }

    @Override
    public String toString() {
        return "Request:\n" + requestMethod + " " + url + "\n" +
                "Body: " + requestBody + "\n";
    }
}
