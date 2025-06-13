package com.speedboot.speedbotagent.knowledge.cleaner.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.speedboot.speedbotagent.generator.IGenerator;
import com.speedboot.speedbotagent.knowledge.cleaner.ICleaner;
import com.speedboot.speedbotagent.prompttemplate.PromptTemplates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LlmBasedCleaner implements ICleaner {
    private static final Logger LOGGER = LoggerFactory.getLogger(LlmBasedCleaner.class);

    @Autowired
    private IGenerator<ChatClientResponse> generator;

    private final PromptTemplate promptTemplate;

    public LlmBasedCleaner() {
        this.promptTemplate = PromptTemplates.DEFAULT_PROMPT_TEMPLATE_BUILDER
                .template(PromptTemplates.TEXT_FORMAT_PROMPT_TEMPLATE).build();
    }

    @Override
    public String clean(String text) {
        Prompt prompt = promptTemplate.create(Map.of("text", text));
        try {
            ChatClientResponse resp = generator.generate(prompt.getContents());
            String output = resp.chatResponse().getResult().getOutput().getText();
            output = output.split("</think>")[1];
            if (output.contains("```json")) {
                output = output.substring(output.lastIndexOf("```json") + "```json\n".length() - 1,
                        output.lastIndexOf("```"));
            }
            JsonObject jsonObject = JsonParser.parseString(output.trim()).getAsJsonObject();
            return jsonObject.get("text").getAsString();
        } catch (Exception e) {
            LOGGER.warn("清理文本失败，将按原样返回: text:%s...".formatted(text.substring(0, Math.min(text.length(), 20))), e);
        }
        return text;
    }
}
