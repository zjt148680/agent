package com.speedboot.speedbotagent.db.dao;

import com.speedboot.speedbotagent.db.entity.DocumentMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocumentMetadataDao {
    void insert(DocumentMetadata documentMetadata);

    void batchInsert(@Param("list") List<DocumentMetadata> documentMetadataList);
}
