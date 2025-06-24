package com.speedboot.speedbotagent.rerank.impl;

import com.speedboot.speedbotagent.dto.knowledge.DocumentInfoByOverlapChunkDTO;
import com.speedboot.speedbotagent.rerank.IReranker;
import com.speedboot.speedbotagent.weaviate.entity.DocumentInfoByOverlapChunk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultReranker implements IReranker<DocumentInfoByOverlapChunkDTO> {
    @Override
    public List<DocumentInfoByOverlapChunkDTO> rerank(String query, List<DocumentInfoByOverlapChunkDTO> knowledge) {
        List<DocumentInfoByOverlapChunkDTO> res = new ArrayList<>(knowledge);
        res.sort((k1, k2) -> {
            DocumentInfoByOverlapChunk d1 = k1.getDocumentInfoByOverlapChunk();
            DocumentInfoByOverlapChunk d2 = k2.getDocumentInfoByOverlapChunk();
            if (d1.getDocumentId() == d2.getDocumentId()) {
                return d1.getChunkId() - d2.getChunkId();
            }
            return d1.getDocumentId() - d2.getDocumentId();
        });
        return res;
    }
}
