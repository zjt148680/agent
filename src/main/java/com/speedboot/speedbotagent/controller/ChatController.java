package com.speedboot.speedbotagent.controller;

import com.speedboot.speedbotagent.dto.ChatDTO;
import com.speedboot.speedbotagent.dto.rag.RagResponseDTO;
import com.speedboot.speedbotagent.service.IChatService;
import com.speedboot.speedbotagent.vo.ChatResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    IChatService chatService;

    @PostMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponseVO> chat(@RequestBody ChatDTO baseQueryDTO) {
        Flux<RagResponseDTO> ragResponseDTO = chatService.chat(baseQueryDTO);
        return ragResponseDTO.map(r -> new ChatResponseVO(r.getMessageType(), r.getContent()));
    }
}
