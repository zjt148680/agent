DROP TABLE IF EXISTS user_chat_record_t;

create table default_db.user_chat_record_t
(
    user_id         BIGINT      not null,
    conversation_id VARCHAR(36) not null,
    create_time     TIMESTAMP   NOT NULL,
    primary key (user_id, conversation_id)
);

