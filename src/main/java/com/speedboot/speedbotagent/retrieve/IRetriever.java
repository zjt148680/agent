package com.speedboot.speedbotagent.retrieve;

import com.speedboot.speedbotagent.dto.knowledge.BaseKnowledgeDTO;
import com.speedboot.speedbotagent.dto.vectordb.VectorDBQueryDTO;

import java.util.List;

public interface IRetriever<R extends BaseKnowledgeDTO, T extends VectorDBQueryDTO> {
    List<R> retrieve(T vectorDBQueryDTO);
}