package com.speedboot.speedbotagent.dto.knowledge;

import com.speedboot.speedbotagent.weaviate.entity.DocumentInfoByOverlapChunk;

public class DocumentInfoByOverlapChunkDTO extends BaseKnowledgeDTO {
    DocumentInfoByOverlapChunk documentInfoByOverlapChunk;

    public DocumentInfoByOverlapChunkDTO(DocumentInfoByOverlapChunk documentInfoByOverlapChunk) {
        super(documentInfoByOverlapChunk.getChunkText());
        this.documentInfoByOverlapChunk = documentInfoByOverlapChunk;
    }

    public DocumentInfoByOverlapChunkDTO() {
        super();
    }

    public DocumentInfoByOverlapChunk getDocumentInfoByOverlapChunk() {
        return documentInfoByOverlapChunk;
    }

    public void setDocumentInfoByOverlapChunk(DocumentInfoByOverlapChunk documentInfoByOverlapChunk) {
        this.documentInfoByOverlapChunk = documentInfoByOverlapChunk;
    }
}
