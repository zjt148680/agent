package com.speedboot.speedbotagent.dto;

import com.speedboot.speedbotagent.base.entities.Query;

public class BaseQueryDTO extends Query {

    private String userId;

    private String threadId;

    public BaseQueryDTO() {
    }

    public BaseQueryDTO(String query) {
        super(query);
    }

    public BaseQueryDTO(String userId, String query) {
        super(query);
        this.userId = userId;
    }

    public BaseQueryDTO(String query, String userId, String threadId) {
        super(query);
        this.userId = userId;
        this.threadId = threadId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
}
