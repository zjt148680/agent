DROP TABLE IF EXISTS document_metadata_t;

create table default_db.document_metadata_t
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
    on default_db.document_metadata_t (document_name);

