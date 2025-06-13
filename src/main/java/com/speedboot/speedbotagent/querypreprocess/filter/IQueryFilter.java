package com.speedboot.speedbotagent.querypreprocess.filter;

import com.speedboot.speedbotagent.dto.ChatDTO;

public interface IQueryFilter {
    ChatDTO filter(ChatDTO query);
}