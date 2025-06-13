package com.speedboot.speedbotagent.rerank;

import com.speedboot.speedbotagent.dto.knowledge.BaseKnowledgeDTO;

import java.util.List;

/**
 * 根据query重新对knowledge进行排序
 */
public interface IReranker {
    List<? extends BaseKnowledgeDTO> rerank(String query, List<? extends BaseKnowledgeDTO> knowledge);
}
