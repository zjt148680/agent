package com.speedboot.speedbotagent.weaviate.entity;

import io.weaviate.client.v1.graphql.query.fields.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentInfoByOverlapChunk {
    private int documentId;

    private String documentName;

    private String documentType;

    private int chunkId;

    private String chunkText;

    public DocumentInfoByOverlapChunk(int documentId, String documentName, String documentType, int chunkId, String chunkText) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.chunkId = chunkId;
        this.chunkText = chunkText;
    }

    public DocumentInfoByOverlapChunk(Map map) {
        this.documentId = ((Double) map.getOrDefault("documentId", 0)).intValue();
        this.documentName = (String) map.getOrDefault("documentName", "");
        this.documentType = (String) map.getOrDefault("documentType", "");
        this.chunkId = ((Double) map.getOrDefault("chunkId", 0)).intValue();
        this.chunkText = (String) map.getOrDefault("chunkText", "");
    }

    public DocumentInfoByOverlapChunk() {
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "documentId", documentId,
                "documentName", documentName,
                "documentType", documentType,
                "chunkId", chunkId,
                "chunkText", chunkText
        );
    }

    public static List<Field> getFieldList() {
        return new ArrayList<>() {{
            add(Field.builder().name("documentId").build());
            add(Field.builder().name("documentName").build());
            add(Field.builder().name("documentType").build());
            add(Field.builder().name("chunkId").build());
            add(Field.builder().name("chunkText").build());
        }};
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getChunkId() {
        return chunkId;
    }

    public void setChunkId(int chunkId) {
        this.chunkId = chunkId;
    }

    public String getChunkText() {
        return chunkText;
    }

    public void setChunkText(String chunkText) {
        this.chunkText = chunkText;
    }
}
