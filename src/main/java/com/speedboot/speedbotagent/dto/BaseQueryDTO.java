package com.speedboot.speedbotagent.dto;

import com.speedboot.speedbotagent.base.entities.Query;

public class BaseQueryDTO extends Query {

    private long userId;

    public BaseQueryDTO() {
    }

    public BaseQueryDTO(String query) {
        super(query);
    }

    public BaseQueryDTO(long userId, String query) {
        super(query);
        this.userId = userId;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
