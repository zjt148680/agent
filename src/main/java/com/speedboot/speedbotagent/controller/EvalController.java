package com.speedboot.speedbotagent.controller;

import com.speedboot.speedbotagent.common.util.ConverterUtils;
import com.speedboot.speedbotagent.dto.ChatDTO;
import com.speedboot.speedbotagent.dto.knowledge.DocumentInfoByOverlapChunkDTO;
import com.speedboot.speedbotagent.dto.vectordb.weaviate.WeaviateVectorDBQueryDTO;
import com.speedboot.speedbotagent.querypreprocess.filter.IQueryFilter;
import com.speedboot.speedbotagent.querypreprocess.rewrite.IQueryReWriter;
import com.speedboot.speedbotagent.rerank.IReranker;
import com.speedboot.speedbotagent.retrieve.IRetriever;
import com.speedboot.speedbotagent.vo.EvalTimeCostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 耗时统计接口，只在非生产环境启用
 */
@RestController
@RequestMapping("/eval")
@Profile("!prod")
public class EvalController {
    private static final ChatDTO CHAT_DTO = new ChatDTO("什么是权限组?");

    private static final int TRY_COUNT = 5;

    @Autowired
    IQueryFilter queryFilter;

    @Autowired
    IQueryReWriter queryReWriter;

    @Autowired
    IRetriever<DocumentInfoByOverlapChunkDTO, WeaviateVectorDBQueryDTO> retriever;

    @Autowired
    IReranker<DocumentInfoByOverlapChunkDTO> reranker;

    @GetMapping(value = "/cost/retrieve")
    public EvalTimeCostVO retrieveCost() {
        WeaviateVectorDBQueryDTO vectorDBQueryDTO =
                ConverterUtils.getWeaviateVectorDBQueryDTOFromBaseQueryDTO(CHAT_DTO);

        double res = 0.;
        for (int i = 0; i < TRY_COUNT; i++) {
            long startTime = System.currentTimeMillis();
            retriever.retrieve(vectorDBQueryDTO);
            res += System.currentTimeMillis() - startTime;
        }
        res /= TRY_COUNT;
        return new EvalTimeCostVO(res);
    }

    @GetMapping(value = "/cost/beforellm")
    public EvalTimeCostVO beforeLlmCost() {
        double res = 0.;
        for (int i = 0; i < TRY_COUNT; i++) {
            long startTime = System.currentTimeMillis();

            // clean
            ChatDTO chatDTO = queryFilter.filter(CHAT_DTO);
            // rewrite
            List<ChatDTO> ChatDTOs = queryReWriter.reWrite(chatDTO);
            chatDTO = ChatDTOs.get(0);
            // retrieve
            WeaviateVectorDBQueryDTO vectorDBQueryDTO =
                    ConverterUtils.getWeaviateVectorDBQueryDTOFromBaseQueryDTO(chatDTO);
            List<DocumentInfoByOverlapChunkDTO> retrieve = retriever.retrieve(vectorDBQueryDTO);
            // rerank
            reranker.rerank(chatDTO.getQuery(), retrieve);

            res += System.currentTimeMillis() - startTime;
        }
        res /= TRY_COUNT;
        return new EvalTimeCostVO(res);
    }
}
