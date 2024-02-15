package com.traceloop.sdk;

public class ClientBuilder {
    private String baseUrl;
    private String apiKey;

    public ClientBuilder baseUrl(String serverUrl) {
        this.baseUrl = serverUrl;
        return this;
    }

    public ClientBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public Client build() {
        return new Client(baseUrl, apiKey);
    }
}
