package com.traceloop.sdk.promptregistry;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ModelConfig {
    @JsonProperty("mode")
    public String mode;
    @JsonProperty("model")
    public String model;
    @JsonProperty("temperature")
    public double temperature;
    @JsonProperty("top_p")
    public double topP;
    @JsonProperty("stop")
    public List<String> stop;
    @JsonProperty("frequency_penalty")
    public double frequencyPenalty;
    @JsonProperty("presence_penalty")
    public double presencePenalty;
}
