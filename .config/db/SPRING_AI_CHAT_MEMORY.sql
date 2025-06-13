-- 注意这个表字段就是这样定义的， 见MysqlChatMemoryRepositoryDialect
-- 要放在resources下

-- DROP TABLE IF EXISTS SPRING_AI_CHAT_MEMORY;

CREATE TABLE IF NOT EXISTS default_db.SPRING_AI_CHAT_MEMORY (
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,
    CONSTRAINT TYPE_CHECK CHECK (type IN ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL'))
);

CREATE INDEX SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX
ON default_db.SPRING_AI_CHAT_MEMORY(conversation_id, `timestamp`);
