package com.traceloop.sdk.promptregistry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.hubspot.jinjava.Jinjava;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Prompt {
    @JsonProperty("id")
    public String id;
    @JsonProperty("key")
    public String key;
    @JsonProperty("versions")
    public List<Version> versions;
    private List<Version> bla;
    @JsonProperty("target")
    public Target target;
    @JsonProperty("created_at")
    public Instant createdAt;
    @JsonProperty("updated_at")
    public Instant updatedAt;

    public ChatCompletionRequest getPrompt(Map<String, Object> variables) {
        Version targetVersion = versions.stream()
                .filter(version -> version.id.equals(target.version))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Target version not found: " + target.version));

        return this.renderVersion(targetVersion, variables);
    }

    private ChatCompletionRequest renderVersion(Version promptVersion, Map<String, Object> variables) {
        if (Objects.equals(promptVersion.templatingEngine, "jinja2")) {
            return renderJinja2(promptVersion, variables);
        } else {
            throw new IllegalArgumentException("Unsupported templating engine: " + promptVersion.templatingEngine);
        }
    }

    private ChatCompletionRequest renderJinja2(Version promptVersion, Map<String, Object> variables) {
        Jinjava jinjava = new Jinjava();

        List<ChatMessage> messages = promptVersion.messages.stream().map(message -> {
            String renderedTemplate = jinjava.render(message.template, variables);

            return new ChatMessage(message.role, renderedTemplate);
        }).toList();

        return ChatCompletionRequest.builder()
                .model(promptVersion.modelConfig.model)
                .temperature(promptVersion.modelConfig.temperature)
                .topP(promptVersion.modelConfig.topP)
                .frequencyPenalty(promptVersion.modelConfig.frequencyPenalty)
                .presencePenalty(promptVersion.modelConfig.presencePenalty)
                .stop(promptVersion.modelConfig.stop)
                .messages(messages)
                .build();
    }
}
