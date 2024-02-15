package com.traceloop.sdk.promptregistry;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetPromptsResponse {
    @JsonProperty("prompts")
    public List<Prompt> prompts;
}
