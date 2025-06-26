1. weaviate服务启动

```yml
services:
  weaviate:
    image: semitechnologies/weaviate:1.31.0-5e97f92.amd64
    ports:
      - 22223:8080
      - 22224:50051
    restart: on-failure:0
    volumes:
      - /root/.weaviate:/var/lib/weaviate
    environment:
      QUERY_DEFAULTS_LIMIT: 200
      AUTHENTICATION_ANONYMOUS_ACCESS_ENABLED: 'true'
      PERSISTENCE_DATA_PATH: '/var/lib/weaviate'
      DEFAULT_VECTORIZER_MODULE: text2vec-transformers
      ENABLE_MODULES: text2vec-transformers
      TRANSFORMERS_INFERENCE_API: http://10.100.15.27:22225
      CLUSTER_HOSTNAME: 'node1'
      AUTOSCHEMA_ENABLED: 'false'
```

2. 数据库新增表

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

3. 向量库表创建

```python
# pip install weaviate-client

import weaviate
from weaviate.collections.classes.config import Property, DataType, Configure


def main():
    global client
    try:
        client = weaviate.connect_to_local(
            host="127.0.0.1",
            port=22223,
            grpc_port=22224,
        )
        print(client.is_ready())

        client.collections.delete("DocumentInfoByOverlapChunk")
        d = client.collections.create(
            "DocumentInfoByOverlapChunk",
            properties=[
                Property(name="documentId", data_type=DataType.INT),
                Property(name="documentName", data_type=DataType.TEXT),
                Property(name="documentType", data_type=DataType.TEXT),
                Property(name="chunkId", data_type=DataType.INT),
                Property(name="chunkText", data_type=DataType.TEXT),
            ],
            vectorizer_config=[
                Configure.NamedVectors.text2vec_transformers(
                    name="documentNameVector",
                    source_properties=["documentName"]
                ),
                Configure.NamedVectors.text2vec_transformers(
                    name="chunkTextVector",
                    source_properties=["chunkText"]
                ),
            ],
        )
        client.close()
    except:
        print("Error")
        if client:
            client.close()


if __name__ == '__main__':
    main()

```

4. docker方式启动

```bash
docker run
-e DATABASE_URL=
-e DATABASE_USERNAME=
-e DATABASE_PASSWORD=
-p 17777:17777
zjt148680/speedbotagent:0.0.1 
```
