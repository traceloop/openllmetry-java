package com.traceloop.sdk.promptregistry;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Message {
    @JsonProperty("index")
    public int index;
    @JsonProperty("role")
    public String role;
    @JsonProperty("template")
    public String template;
    @JsonProperty("variables")
    public List<String> variables;
}
