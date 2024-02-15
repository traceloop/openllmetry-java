package com.traceloop.sdk.promptregistry;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public class Version {
    @JsonProperty("id")
    public String id;
    @JsonProperty("hash")
    public String hash;
    @JsonProperty("version")
    public int version;
    @JsonProperty("name")
    public String name;
    @JsonProperty("provider")
    public String provider;
    @JsonProperty("templating_engine")
    public String templatingEngine;
    @JsonProperty("messages")
    public List<Message> messages;
    @JsonProperty("llm_config")
    public ModelConfig modelConfig;
    @JsonProperty("created_at")
    public Instant createdAt;
    @JsonProperty("updated_at")
    public Instant updatedAt;
}
