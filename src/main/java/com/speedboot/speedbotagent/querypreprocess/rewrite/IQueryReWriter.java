package com.speedboot.speedbotagent.querypreprocess.rewrite;

import com.speedboot.speedbotagent.dto.BaseQueryDTO;

import java.util.List;

public interface IQueryReWriter {
    List<BaseQueryDTO> reWrite(BaseQueryDTO query);
}