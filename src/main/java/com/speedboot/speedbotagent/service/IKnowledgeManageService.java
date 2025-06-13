package com.speedboot.speedbotagent.service;

import com.speedboot.speedbotagent.dto.UploadFileRespDTO;
import com.speedboot.speedbotagent.dto.UploadFilesDTO;

public interface IKnowledgeManageService {
    UploadFileRespDTO uploadFiles(UploadFilesDTO uploadFilesDTO);
}
