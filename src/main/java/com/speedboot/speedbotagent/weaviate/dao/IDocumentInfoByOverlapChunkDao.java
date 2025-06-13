package com.speedboot.speedbotagent.weaviate.dao;

import com.speedboot.speedbotagent.dto.vectordb.weaviate.WeaviateVectorDBQueryDTO;
import com.speedboot.speedbotagent.weaviate.entity.DocumentInfoByOverlapChunk;

import java.util.List;

public interface IDocumentInfoByOverlapChunkDao {
    List<DocumentInfoByOverlapChunk> simpleQuery(WeaviateVectorDBQueryDTO vectorDBQueryDTO);

    void insert(DocumentInfoByOverlapChunk documentInfo);

    void batchInsert(List<DocumentInfoByOverlapChunk> documentInfoList);
}
