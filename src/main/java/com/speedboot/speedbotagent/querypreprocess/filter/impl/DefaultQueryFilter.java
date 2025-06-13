package com.speedboot.speedbotagent.querypreprocess.filter.impl;

import com.speedboot.speedbotagent.dto.BaseQueryDTO;
import com.speedboot.speedbotagent.querypreprocess.filter.IQueryFilter;
import org.springframework.stereotype.Component;

@Component
public class DefaultQueryFilter implements IQueryFilter {
    @Override
    public BaseQueryDTO filter(BaseQueryDTO query) {
        return query;
    }
}