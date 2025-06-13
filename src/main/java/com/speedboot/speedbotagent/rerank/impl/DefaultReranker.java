package com.speedboot.speedbotagent.rerank.impl;

import com.speedboot.speedbotagent.dto.knowledge.BaseKnowledgeDTO;
import com.speedboot.speedbotagent.rerank.IReranker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultReranker implements IReranker {
    @Override
    public List<? extends BaseKnowledgeDTO> rerank(String query, List<? extends BaseKnowledgeDTO> knowledge) {
        return knowledge;
    }
}
