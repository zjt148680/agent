package com.speedboot.speedbotagent.service.impl;

import com.speedboot.speedbotagent.db.dao.DocumentMetadataDao;
import com.speedboot.speedbotagent.db.entity.DocumentMetadata;
import com.speedboot.speedbotagent.dto.UploadFileRespDTO;
import com.speedboot.speedbotagent.dto.UploadFilesDTO;
import com.speedboot.speedbotagent.knowledge.cleaner.ICleaner;
import com.speedboot.speedbotagent.knowledge.reader.IReader;
import com.speedboot.speedbotagent.knowledge.splitter.ISplitter;
import com.speedboot.speedbotagent.service.IKnowledgeManageService;
import com.speedboot.speedbotagent.weaviate.dao.IDocumentInfoByOverlapChunkDao;
import com.speedboot.speedbotagent.weaviate.entity.DocumentInfoByOverlapChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * todo 事务一致性，目前未开放给用户，所以没处理
 *
 */
@Service
public class KnowledgeManageService implements IKnowledgeManageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeManageService.class);

    @Autowired
    private DocumentMetadataDao documentMetadataDao;

    @Qualifier("tikaReader")
    @Autowired
    private IReader reader;

    @Qualifier("defaultCleaner")
    @Autowired
    private ICleaner cleaner;

    @Qualifier("chineseRecursiveCharacterTextSplitter")
    @Autowired
    private ISplitter splitter;

    @Autowired
    IDocumentInfoByOverlapChunkDao documentInfoService;

    // todo 事务一致性
    @Override
    public UploadFileRespDTO uploadFiles(UploadFilesDTO uploadFilesDTO) {
        long userId = uploadFilesDTO.getUserId();
        List<ExecuteRes> executeResList = Arrays.stream(uploadFilesDTO.getFiles())
                .map(file -> processFile(userId, file))
                .toList();

        int totalCount = uploadFilesDTO.getFiles().length;
        List<String> failedFileNames = executeResList.stream()
                .filter(executeRes -> !executeRes.isSuccess())
                .map(ExecuteRes::getFileName)
                .toList();
        int failCount = failedFileNames.size();

        return new UploadFileRespDTO(totalCount, failCount, failedFileNames);
    }

    private ExecuteRes processFile(long userId, MultipartFile file) {
        try {
            // 读取文件并分块
            InputStream inputStream = file.getInputStream();
            String text = reader.read(inputStream);
            List<String> chunks = splitter.split(text);
            chunks = chunks.stream()
                    .map(cleaner::clean)
                    .toList();

            // 保存到数据库
            String fileName = file.getOriginalFilename();
            DocumentMetadata documentMetadata = constructDocumentMetadata(userId, fileName, chunks.size());
            documentMetadataDao.insert(documentMetadata);

            // 保存到向量数据库
            List<DocumentInfoByOverlapChunk> documentInfos = constructDocumentInfoByOverlapChunks(
                    documentMetadata,
                    chunks
            );
            documentInfoService.batchInsert(documentInfos);

            return new ExecuteRes(true, fileName);
        } catch (Exception e) {
            LOGGER.error("处理文件%s时出错。".formatted(file.getOriginalFilename()), e);
            return new ExecuteRes(false, file.getOriginalFilename());
        }
    }

    private DocumentMetadata constructDocumentMetadata(long userId, String documentName, int chunkCount) {
        DocumentMetadata documentMetadata = new DocumentMetadata();
        documentMetadata.setUserId(userId);
        documentMetadata.setDocumentName(documentName);
        String[] tmp = documentName.split("\\.");
        // todo 判断
        documentMetadata.setDocumentType(tmp[tmp.length - 1]);
        documentMetadata.setChunkCount(chunkCount);
        return documentMetadata;
    }

    private List<DocumentInfoByOverlapChunk> constructDocumentInfoByOverlapChunks(
            DocumentMetadata documentMetadata, List<String> chunks) {
        List<DocumentInfoByOverlapChunk> res = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            res.add(new DocumentInfoByOverlapChunk(
                    documentMetadata.getDocumentId(),
                    documentMetadata.getDocumentName(),
                    documentMetadata.getDocumentType(),
                    i,
                    chunks.get(i)));
        }
        return res;
    }
}

class ExecuteRes {
    private final boolean success;
    private final String fileName;

    public ExecuteRes(boolean success, String fileName) {
        this.success = success;
        this.fileName = fileName;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFileName() {
        return fileName;
    }
}