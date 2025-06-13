package com.speedboot.speedbotagent.dto.vectordb;

import com.speedboot.speedbotagent.dto.BaseQueryDTO;

public class VectorDBQueryDTO extends BaseQueryDTO {
    public VectorDBQueryDTO(long userId, String query) {
        super(userId, query);
    }

    public VectorDBQueryDTO() {
        super();
    }
}
