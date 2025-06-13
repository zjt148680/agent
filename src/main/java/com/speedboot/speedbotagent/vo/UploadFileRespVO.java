package com.speedboot.speedbotagent.vo;

import com.speedboot.speedbotagent.dto.UploadFileRespDTO;

import java.util.List;

public class UploadFileRespVO extends UploadFileRespDTO {
    public UploadFileRespVO(int totalCount, int failCount, List<String> failedFileNames) {
        super(totalCount, failCount, failedFileNames);
    }

    public UploadFileRespVO(UploadFileRespDTO uploadFileRespDTO) {
        super(uploadFileRespDTO.getTotalCount(),
                uploadFileRespDTO.getFailCount(),
                uploadFileRespDTO.getFailedFileNames());
    }
}
