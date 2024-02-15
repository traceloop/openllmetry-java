package com.traceloop.sdk.promptregistry;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class Target {
    @JsonProperty("id")
    public String id;
    @JsonProperty("prompt_id")
    public String promptId;
    @JsonProperty("version")
    public String version;
    @JsonProperty("updated_at")
    public Instant updatedAt;
}
