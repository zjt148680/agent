package com.speedboot.speedbotagent.dto;

import java.util.List;

public class UploadFileRespDTO {
    private int totalCount;

    private int failCount;

    List<String> failedFileNames;

    public UploadFileRespDTO(int totalCount, int failCount, List<String> failedFileNames) {
        this.totalCount = totalCount;
        this.failCount = failCount;
        this.failedFileNames = failedFileNames;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public List<String> getFailedFileNames() {
        return failedFileNames;
    }

    public void setFailedFileNames(List<String> failedFileNames) {
        this.failedFileNames = failedFileNames;
    }
}
