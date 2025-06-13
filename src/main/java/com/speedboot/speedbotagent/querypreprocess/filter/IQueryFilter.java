package com.speedboot.speedbotagent.querypreprocess.filter;

import com.speedboot.speedbotagent.dto.BaseQueryDTO;

public interface IQueryFilter {
    BaseQueryDTO filter(BaseQueryDTO query);
}