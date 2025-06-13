package com.speedboot.speedbotagent.db.entity;

public class DocumentMetadata {
    private int userId;

    private int documentId;

    private String documentName;

    private String documentType;

    private String documentUrl;

    private int chunkCount;

    public DocumentMetadata() {
    }

    public DocumentMetadata(int userId, int documentId, String documentName, String documentType, String documentUrl, int chunkCount) {
        this.userId = userId;
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.documentUrl = documentUrl;
        this.chunkCount = chunkCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public int getChunkCount() {
        return chunkCount;
    }

    public void setChunkCount(int chunkCount) {
        this.chunkCount = chunkCount;
    }
}
