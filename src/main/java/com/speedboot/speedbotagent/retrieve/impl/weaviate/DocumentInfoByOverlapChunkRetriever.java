package com.speedboot.speedbotagent.retrieve.impl.weaviate;

import com.speedboot.speedbotagent.dto.knowledge.DocumentInfoByOverlapChunkDTO;
import com.speedboot.speedbotagent.dto.vectordb.weaviate.WeaviateVectorDBQueryDTO;
import com.speedboot.speedbotagent.retrieve.IRetriever;
import com.speedboot.speedbotagent.weaviate.dao.IDocumentInfoByOverlapChunkDao;
import com.speedboot.speedbotagent.weaviate.entity.DocumentInfoByOverlapChunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentInfoByOverlapChunkRetriever implements IRetriever<DocumentInfoByOverlapChunkDTO, WeaviateVectorDBQueryDTO> {
    @Autowired
    private IDocumentInfoByOverlapChunkDao documentInfoDao;

    @Override
    public List<DocumentInfoByOverlapChunkDTO> retrieve(WeaviateVectorDBQueryDTO vectorDBQueryDTO) {
        List<DocumentInfoByOverlapChunk> res =  documentInfoDao.simpleQuery(vectorDBQueryDTO);
        return res.stream().map(DocumentInfoByOverlapChunkDTO::new).toList();
    }

}
