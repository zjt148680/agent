package com.speedboot.speedbotagent.rag;

import com.speedboot.speedbotagent.dto.BaseQueryDTO;
import com.speedboot.speedbotagent.dto.rag.RagResponseDTO;
import reactor.core.publisher.Flux;

public interface IRag {
    Flux<RagResponseDTO> chat(BaseQueryDTO baseQueryDTO);
}
