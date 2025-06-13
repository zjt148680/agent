package com.speedboot.speedbotagent.querypreprocess.filter.impl;

import com.speedboot.speedbotagent.dto.ChatDTO;
import com.speedboot.speedbotagent.querypreprocess.filter.IQueryFilter;
import org.springframework.stereotype.Component;

@Component
public class DefaultQueryFilter implements IQueryFilter {
    @Override
    public ChatDTO filter(ChatDTO query) {
        return query;
    }
}