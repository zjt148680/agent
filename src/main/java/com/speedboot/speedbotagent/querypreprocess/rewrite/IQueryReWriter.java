package com.speedboot.speedbotagent.querypreprocess.rewrite;

import com.speedboot.speedbotagent.dto.ChatDTO;

import java.util.List;

public interface IQueryReWriter {
    List<ChatDTO> reWrite(ChatDTO query);
}