package com.speedboot.speedbotagent.dto;

public class ChatDTO extends BaseQueryDTO {
    private String conversationId;

    public ChatDTO(String conversationId) {
        this.conversationId = conversationId;
    }

    public ChatDTO(String query, String conversationId) {
        super(query);
        this.conversationId = conversationId;
    }

    public ChatDTO(long userId, String query, String conversationId) {
        super(userId, query);
        this.conversationId = conversationId;
    }

    public ChatDTO() {
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
