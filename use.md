1. 数据库新增表

```sql
DROP TABLE IF EXISTS document_metadata_t;
create table document_metadata_t
(
    user_id       BIGINT        not null,
    document_id   INT auto_increment,
    document_name varchar(200)  not null,
    document_url  varchar(1024) null,
    document_type varchar(50)   null,
    chunk_count   int           not null comment '块数目',
    primary key (user_id, document_id),
    constraint document_metadata_t_pk
        unique (document_id)
)
    comment 'pdf元数据';
create index pdf_metadata_documentName_index
    on document_metadata_t (document_name);


CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,
    CONSTRAINT TYPE_CHECK CHECK (type IN ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL'))
)
    comment '聊天记录';
CREATE INDEX SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX
ON SPRING_AI_CHAT_MEMORY(conversation_id, `timestamp`);


DROP TABLE IF EXISTS user_chat_record_t;
create table user_chat_record_t
(
    user_id         BIGINT      not null,
    conversation_id VARCHAR(36) not null,
    create_time     TIMESTAMP   NOT NULL,
    primary key (user_id, conversation_id)
);
```

2. docker方式启动

```bash
docker run
-p 17777:17777
-e SPRING_PROFILES_ACTIVE=prod
zjt148680/speedbotagent:0.0.1 
```
