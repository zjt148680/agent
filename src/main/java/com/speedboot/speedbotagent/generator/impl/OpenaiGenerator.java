package com.speedboot.speedbotagent.generator.impl;

import com.speedboot.speedbotagent.generator.IGenerator;
import com.speedboot.speedbotagent.prompttemplate.PromptTemplates;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * todo memory的事务控制
 *
 */
@Component
public class OpenaiGenerator implements IGenerator<ChatClientResponse> {
    private final ChatClient chatClientWithMemory;

    private final ChatClient chatClient;

    public OpenaiGenerator(ChatClient.Builder chatClientBuilder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10).build();
        this.chatClientWithMemory = chatClientBuilder.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .defaultTemplateRenderer(PromptTemplates.DEFAULT_PROMPT_TEMPLATE_RENDERER)
                .defaultSystem(PromptTemplates.CHAT_PROMPT_TEMPLATE.get(MessageType.SYSTEM.getValue()))
                .build();
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Flux<ChatClientResponse> streamGenerate(String prompt, String identity) {
        return chatClientWithMemory.prompt()
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, identity))
                .user(prompt)
                .stream()
                .chatClientResponse();
    }

    @Override
    public ChatClientResponse generate(String prompt, String identity) {
        return this.chatClientWithMemory.prompt()
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, identity))
                .user(prompt)
                .call()
                .chatClientResponse();
    }

    @Override
    public Flux<ChatClientResponse> streamGenerate(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .chatClientResponse();
    }

    @Override
    public ChatClientResponse generate(String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .chatClientResponse();
    }
}
