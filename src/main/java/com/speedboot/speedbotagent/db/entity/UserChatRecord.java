package com.speedboot.speedbotagent.db.entity;

import java.time.LocalDateTime;

public class UserChatRecord {
    private long userId;

    private String conversationId;

    private LocalDateTime createTime;

    public UserChatRecord(long userId, String conversationId, LocalDateTime createTime) {
        this.userId = userId;
        this.conversationId = conversationId;
        this.createTime = createTime;
    }

    public UserChatRecord() {
    }

    public UserChatRecord(long userId, String conversationId) {
        this.userId = userId;
        this.conversationId = conversationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
