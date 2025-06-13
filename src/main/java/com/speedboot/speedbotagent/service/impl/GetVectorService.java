package com.speedboot.speedbotagent.service.impl;

import com.speedboot.speedbotagent.common.exception.SpeedBotException;
import com.speedboot.speedbotagent.dto.t2vservice.BatchVectorRequestDTO;
import com.speedboot.speedbotagent.dto.t2vservice.VectorRequestDTO;
import com.speedboot.speedbotagent.dto.t2vservice.VectorResponseDTO;
import com.speedboot.speedbotagent.service.IGetVectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class GetVectorService implements IGetVectorService {
    private static final String BATCH_VECTOR_SERVER_PATH = "/vectors/batch";

    private static final String VECTOR_SERVER_PATH = "/vectors";

    private static final int BATCH_SIZE = 8;

    private final WebClient webClient;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    public GetVectorService(WebClient.Builder webClientBuilder,
                            @Value("${transformers-interface-api}") String transformersInterfaceApi) {
        this.webClient = webClientBuilder.baseUrl(transformersInterfaceApi).build();
    }

    @Override
    public VectorResponseDTO getVector(VectorRequestDTO vectorRequestDTO) {
        return webClient.post()
                .uri(VECTOR_SERVER_PATH)
                .bodyValue(vectorRequestDTO)
                .retrieve()
                .bodyToMono(VectorResponseDTO.class)
                .block();
    }

    @Override
    public List<VectorResponseDTO> batchGetVector(BatchVectorRequestDTO vectorRequestDTO) {
        List<String> allTexts = vectorRequestDTO.getTexts();
        try {
            // 1. 创建异步任务列表
            List<CompletableFuture<List<VectorResponseDTO>>> futures = new ArrayList<>();
            for (int i = 0; i < allTexts.size(); i += BATCH_SIZE) {
                List<String> batch = allTexts.subList(i, Math.min(i + BATCH_SIZE, allTexts.size()));
                futures.add(CompletableFuture.supplyAsync(
                        () -> processBatch(batch),
                        executor
                ));
            }

            // 2. 创建聚合Future（任一失败立即失败）
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            ).exceptionally(ex -> {
                // 3. 取消所有未完成任务
                futures.forEach(future -> {
                    if (!future.isDone()) {
                        future.cancel(true);
                    }
                });
                throw new CompletionException(ex);
            });

            // 4. 等待所有任务完成（任一失败会立即抛出）
            allFutures.join();

            // 5. 收集成功结果
            return futures.stream()
                    .flatMap(future -> future.join().stream())
                    .toList();
        } catch (CompletionException e) {
            // 6. 处理异常
            throw new SpeedBotException("批量获取向量失败", e);
        }
    }

    // 处理单个批次的请求
    private List<VectorResponseDTO> processBatch(List<String> batchTexts) {
        BatchVectorRequestDTO batchRequest = new BatchVectorRequestDTO(batchTexts);
        return webClient.post()
                .uri(BATCH_VECTOR_SERVER_PATH)
                .bodyValue(batchRequest)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<VectorResponseDTO>>() {
                })
                .block();
    }
}
