package com.speedboot.speedbotagent.service;

import com.speedboot.speedbotagent.dto.ChatDTO;
import com.speedboot.speedbotagent.dto.rag.RagResponseDTO;
import reactor.core.publisher.Flux;

public interface IChatService {
    Flux<RagResponseDTO> chat(ChatDTO baseQueryDTO);
}
