package com.speedboot.speedbotagent.common.util;

import com.speedboot.speedbotagent.dto.BaseQueryDTO;
import com.speedboot.speedbotagent.dto.vectordb.weaviate.WeaviateVectorDBQueryDTO;

public class ConverterUtils {
    public static WeaviateVectorDBQueryDTO getWeaviateVectorDBQueryDTOFromBaseQueryDTO(BaseQueryDTO baseQueryDTO) {
        return new WeaviateVectorDBQueryDTO(
                baseQueryDTO.getUserId(),
                baseQueryDTO.getQuery());
    }
}
