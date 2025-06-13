package com.speedboot.speedbotagent.rag;

import com.speedboot.speedbotagent.dto.ChatDTO;
import com.speedboot.speedbotagent.dto.rag.RagResponseDTO;
import reactor.core.publisher.Flux;

public interface IRag {
    Flux<RagResponseDTO> chat(ChatDTO baseQueryDTO);
}
