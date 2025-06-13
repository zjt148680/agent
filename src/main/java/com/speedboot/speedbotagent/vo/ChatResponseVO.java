package com.speedboot.speedbotagent.vo;

import com.speedboot.speedbotagent.dto.rag.RagResponseDTO;

public class ChatResponseVO extends RagResponseDTO {

    public ChatResponseVO(MessageEnum messageType, String content) {
        super(messageType, content);
    }

    public ChatResponseVO() {
    }
}
