package com.speedboot.speedbotagent.base.entities;

import java.util.List;

public class BaseEvalEntity {
    private Query query;
    private List<? extends Knowledge> knowledge;
    private String response;

    public BaseEvalEntity(Query query, String response, List<? extends Knowledge> knowledge) {
        this.query = query;
        this.response = response;
        this.knowledge = knowledge;
    }

    public BaseEvalEntity() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<? extends Knowledge> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(List<? extends Knowledge> knowledge) {
        this.knowledge = knowledge;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
