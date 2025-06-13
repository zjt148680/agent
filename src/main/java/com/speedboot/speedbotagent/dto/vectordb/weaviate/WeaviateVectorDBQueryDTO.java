package com.speedboot.speedbotagent.dto.vectordb.weaviate;

import com.speedboot.speedbotagent.dto.vectordb.VectorDBQueryDTO;


public class WeaviateVectorDBQueryDTO extends VectorDBQueryDTO {
    String collection;

    String vectorProperty;

    public WeaviateVectorDBQueryDTO(String userId, String query, String collection, String vectorProperty) {
        super(userId, query);
        this.collection = collection;
        this.vectorProperty = vectorProperty;
    }

    public WeaviateVectorDBQueryDTO(String userId, String query) {
        super(userId, query);
    }

    public WeaviateVectorDBQueryDTO() {
        super();
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getVectorProperty() {
        return vectorProperty;
    }

    public void setVectorProperty(String vectorProperty) {
        this.vectorProperty = vectorProperty;
    }
}
