package com.speedboot.speedbotagent.dto.rag;

public class RagResponseDTO {
    public RagResponseDTO(MessageEnum messageType, String content) {
        this.messageType = messageType;
        this.content = content;
    }

    public RagResponseDTO() {
    }

    public MessageEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageEnum messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public enum MessageEnum {
        partial_answer,
        related_document,
        identity,
    }

    private MessageEnum messageType;
    private String content;
}
