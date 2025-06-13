package com.speedboot.speedbotagent.rag.impl;

import com.speedboot.speedbotagent.common.util.ConverterUtils;
import com.speedboot.speedbotagent.dto.BaseQueryDTO;
import com.speedboot.speedbotagent.dto.knowledge.BaseKnowledgeDTO;
import com.speedboot.speedbotagent.dto.knowledge.DocumentInfoByOverlapChunkDTO;
import com.speedboot.speedbotagent.dto.rag.RagResponseDTO;
import com.speedboot.speedbotagent.dto.vectordb.weaviate.WeaviateVectorDBQueryDTO;
import com.speedboot.speedbotagent.generator.IGenerator;
import com.speedboot.speedbotagent.prompttemplate.PromptTemplates;
import com.speedboot.speedbotagent.querypreprocess.filter.IQueryFilter;
import com.speedboot.speedbotagent.querypreprocess.rewrite.IQueryReWriter;
import com.speedboot.speedbotagent.rag.IRag;
import com.speedboot.speedbotagent.rerank.IReranker;
import com.speedboot.speedbotagent.retrieve.IRetriever;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SimpleRag implements IRag {
    private final PromptTemplate userPromptTemplate;

    @Autowired
    ChatMemoryRepository chatMemoryRepository;

    @Autowired
    IQueryFilter queryFilter;

    @Autowired
    IQueryReWriter queryReWriter;

    @Autowired
    IRetriever<DocumentInfoByOverlapChunkDTO, WeaviateVectorDBQueryDTO> retriever;

    @Autowired
    IReranker reranker;

    @Autowired
    private IGenerator<ChatClientResponse> generator;

    public SimpleRag() {
        this.userPromptTemplate = PromptTemplates.DEFAULT_PROMPT_TEMPLATE_BUILDER
                .template(PromptTemplates.CHAT_PROMPT_TEMPLATE.get(MessageType.USER.getValue())).build();
    }

    @Override
    public Flux<RagResponseDTO> chat(BaseQueryDTO baseQueryDTO) {
        // clean
        baseQueryDTO = queryFilter.filter(baseQueryDTO);

        // rewrite
        List<BaseQueryDTO> baseQueryDTOs = queryReWriter.reWrite(baseQueryDTO);
        baseQueryDTO = baseQueryDTOs.get(0);

        // retrieve
        WeaviateVectorDBQueryDTO vectorDBQueryDTO =
                ConverterUtils.getWeaviateVectorDBQueryDTOFromBaseQueryDTO(baseQueryDTO);
        List<DocumentInfoByOverlapChunkDTO> retrieve = retriever.retrieve(vectorDBQueryDTO);

        // rerank
        retrieve = (List<DocumentInfoByOverlapChunkDTO>) reranker.rerank(baseQueryDTO.getQuery(), retrieve);

        // prompt
        Prompt prompt = getPrompt(baseQueryDTO.getQuery(), retrieve);

        // chat
        Flux<ChatClientResponse> response = generator.streamGenerate(prompt.getContents(), baseQueryDTO.getThreadId());

        Flux<RagResponseDTO> answer = response.map(chatClientResponse -> {
            RagResponseDTO ragResponseDTO = new RagResponseDTO();
            ragResponseDTO.setMessageType(RagResponseDTO.MessageEnum.partial_answer);
            if (chatClientResponse.chatResponse() != null) {
                ragResponseDTO.setContent(chatClientResponse.chatResponse().getResult().getOutput().getText());
            }
            return ragResponseDTO;
        });

        List<String> relDocumentNames = retrieve.stream()
                .map(documentInfoByOverlapChunkDTO ->
                        documentInfoByOverlapChunkDTO.getDocumentInfoByOverlapChunk().getDocumentName() + ": \n\n" +
                        documentInfoByOverlapChunkDTO.getDocumentInfoByOverlapChunk().getChunkText())
                .distinct()
                .toList();
        Flux<RagResponseDTO> rel = Flux.just(
                relDocumentNames.stream()
                        .map(d -> new RagResponseDTO(RagResponseDTO.MessageEnum.related_document, d))
                        .distinct()
                        .toArray(RagResponseDTO[]::new));
        Flux<RagResponseDTO> identity = Flux.just(
                new RagResponseDTO(RagResponseDTO.MessageEnum.identity, baseQueryDTO.getThreadId()));

        return answer.concatWith(rel).concatWith(identity);
    }

    private Prompt getPrompt(String query, List<DocumentInfoByOverlapChunkDTO> knowledge) {
        Map<String, Object> params = new HashMap<>();
        params.put("knowledge", parseKnowledge(knowledge));
        params.put("query", query);
        return this.userPromptTemplate.create(params);
    }

    private String parseKnowledge(List<? extends BaseKnowledgeDTO> knowledge) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < knowledge.size(); i++) {
            sb.append("[").append(i + 1).append("]. ").append(knowledge.get(i).getText()).append("\n");
        }
        return sb.toString();
    }
}
