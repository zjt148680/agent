package com.speedboot.speedbotagent.weaviate.dao.impl;

import com.speedboot.speedbotagent.common.exception.SpeedBotException;
import com.speedboot.speedbotagent.dto.vectordb.weaviate.WeaviateVectorDBQueryDTO;
import com.speedboot.speedbotagent.weaviate.dao.IDocumentInfoByOverlapChunkDao;
import com.speedboot.speedbotagent.weaviate.entity.DocumentInfoByOverlapChunk;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.batch.api.ObjectsBatcher;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearTextArgument;
import io.weaviate.client.v1.graphql.query.builder.GetBuilder;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.graphql.query.fields.Fields;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentInfoByOverlapChunkDao implements IDocumentInfoByOverlapChunkDao {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DocumentInfoByOverlapChunkDao.class);

    private static final String COLLECTION_NAME = "DocumentInfoByOverlapChunk";

    private static final String[] COMP_VECTOR_PROPERTIES = {"chunkTextVector"};

    private static final int TOP_K = 10;

    private static final float THRESHOLD = 0.4f;

    @Autowired
    private WeaviateClient client;

    @Override
    public List<DocumentInfoByOverlapChunk> retrieve(WeaviateVectorDBQueryDTO vectorDBQueryDTO) {
        String query = vectorDBQueryDTO.getQuery();

        // 和COMP_VECTOR_PROPERTIES的向量比较
        NearTextArgument nearText = NearTextArgument.builder()
                .concepts(new String[]{query})
                .targetVectors(COMP_VECTOR_PROPERTIES)
                .distance(THRESHOLD)
                .build();

        // 要返回的字段
        Fields fields = getFields();

        String q = GetBuilder.builder()
                .className(COLLECTION_NAME)
                .fields(fields)
                .withNearTextFilter(nearText)
                .limit(TOP_K)
                .build()
                .buildQuery();
        Result<GraphQLResponse> result = client.graphQL().raw().withQuery(q).run();
        if (result.hasErrors()) {
            throw new SpeedBotException("检索文档失败，error: %s".formatted(
                    result.getError().getMessages().get(0).getMessage()));
        }

        return getDocumentInfoByOverlapChunks(result);
    }

    @Override
    public void insert(DocumentInfoByOverlapChunk documentInfo) {
        Result<WeaviateObject> result = client.data().creator()
                .withClassName(COLLECTION_NAME)
                .withProperties(documentInfo.toMap())
                .run();
        if (result.hasErrors()) {
            throw new SpeedBotException("%s插入向量库失败, error: %s".formatted(documentInfo.getDocumentName(),
                    result.getError().getMessages().get(0).getMessage()));
        }
    }

    @Override
    public void batchInsert(List<DocumentInfoByOverlapChunk> documentInfoList) {
        ObjectsBatcher.AutoBatchConfig autoBatchConfig = ObjectsBatcher.AutoBatchConfig.defaultConfig()
                .callback(result -> {
                    if (result.hasErrors()) {
                        String errorMsg = result.getError().getMessages().get(0).getMessage();
                        LOGGER.error("批量插入数据失败：%s".formatted(errorMsg));
                        throw new SpeedBotException(errorMsg);
                    }
                }).build();
        ObjectsBatcher batcher = client.batch().objectsAutoBatcher(autoBatchConfig);
        batcher.withObjects(documentInfoList.stream()
                .map(documentInfo -> WeaviateObject.builder()
                        .className(COLLECTION_NAME)
                        .properties(documentInfo.toMap())
                        .build()
                )
                .toList()
                .toArray(new WeaviateObject[0]));
        batcher.run();
    }

    private Fields getFields() {
        List<Field> fieldList = DocumentInfoByOverlapChunk.getFieldList();

        Field additionalField = Field.builder()
                .name("_additional")
                .fields(new Field[]{
                        Field.builder().name("distance").build(),
                })
                .build();
        fieldList.add(additionalField);

        return Fields.builder().fields(fieldList.toArray(new Field[0])).build();
    }

    private List<DocumentInfoByOverlapChunk> getDocumentInfoByOverlapChunks(
            Result<GraphQLResponse> graphQLResponseResult) {
        Map tmp = (Map) (graphQLResponseResult.getResult().getData());
        tmp = (Map) (tmp.get("Get"));
        List<Map> objMaps = (List) (tmp.get(COLLECTION_NAME));

        return objMaps.stream().map(DocumentInfoByOverlapChunk::new).toList();
    }
}
