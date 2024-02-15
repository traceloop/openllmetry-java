package com.traceloop.sdk;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.traceloop.sdk.promptregistry.GetPromptsResponse;
import com.traceloop.sdk.promptregistry.Prompt;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.semconv.ResourceAttributes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.*;

public class Client {
    private final String baseUrl;
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, Prompt> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Client(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        initTracing();
    }

    private void initTracing() {
        Resource resource = Resource.getDefault().toBuilder().put(ResourceAttributes.SERVICE_NAME, "dice-server").build();

        try (SpanExporter exporter = LoggingSpanExporter.create()) {
            try (SpanProcessor processor = BatchSpanProcessor.builder(exporter).build()) {
            }
        }
    }

    public static ClientBuilder newBuilder() {
        return new
            ClientBuilder()
                .baseUrl("http://api.traceloop.com");
    }

    public void start() {
        fetchAndCachePrompts();

        scheduler.scheduleAtFixedRate(this::fetchAndCachePrompts, 0, 3, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
    }

    private void fetchAndCachePrompts() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/v1/prompts"))
                    .headers("Authorization", "Bearer " + apiKey)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                try {
                    GetPromptsResponse responseObj = objectMapper.readValue(response.body(), GetPromptsResponse.class);
                    System.out.println(responseObj);

                    for (Prompt prompt : responseObj.prompts) {
                        cache.put(prompt.key, prompt);
                    }

                    System.out.println(cache);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println(response.statusCode());
                handleFailure(1);
            }
        } catch (IOException | InterruptedException e) {
            handleFailure(1);
        }
    }

    private void handleFailure(int attempt) {
        if (attempt <= 3) { // Maximum of 3 retries
            try {
                TimeUnit.SECONDS.sleep((long) Math.pow(2, attempt)); // Exponential backoff
                fetchAndCachePrompts();
            } catch (InterruptedException ignored) {}
        } else {
            System.err.println("Failed to fetch prompts after retries.");
        }
    }

    public ChatCompletionRequest getPrompt(String key, Map<String, Object> variables) {
        return cache
                .get(key)
                .getPrompt(variables);
    }
}
