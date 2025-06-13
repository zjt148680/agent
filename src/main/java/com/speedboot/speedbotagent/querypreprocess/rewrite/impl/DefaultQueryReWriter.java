package com.speedboot.speedbotagent.querypreprocess.rewrite.impl;

import com.speedboot.speedbotagent.dto.ChatDTO;
import com.speedboot.speedbotagent.querypreprocess.rewrite.IQueryReWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultQueryReWriter implements IQueryReWriter {

    @Override
    public List<ChatDTO> reWrite(ChatDTO query) {
        return List.of(query);
    }
}