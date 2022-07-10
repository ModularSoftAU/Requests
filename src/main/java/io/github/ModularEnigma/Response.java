package io.github.ModularEnigma;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class Response {
    private final String body;
    private final int statusCode;
    private final Map<String, List<String>> headers;

    public Response(HttpResponse<String> response) {
        this.body = response.body();
        this.statusCode = response.statusCode();
        this.headers = response.headers().map();
    }

    /**
     * Gets the body of the response. May be empty
     */
    public String getBody() {
        return body;
    }

    /**
     * Gets the response's status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the response headers
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
