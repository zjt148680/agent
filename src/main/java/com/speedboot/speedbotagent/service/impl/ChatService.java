package com.speedboot.speedbotagent.service.impl;

import com.speedboot.speedbotagent.dto.BaseQueryDTO;
import com.speedboot.speedbotagent.dto.rag.RagResponseDTO;
import com.speedboot.speedbotagent.rag.IRag;
import com.speedboot.speedbotagent.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService implements IChatService {
    @Autowired
    @Qualifier("simpleRag")
    IRag rag;

    public Flux<RagResponseDTO> chat(BaseQueryDTO baseQueryDTO) {
        return rag.chat(baseQueryDTO);
    }
}
