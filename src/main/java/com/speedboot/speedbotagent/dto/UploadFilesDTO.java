package com.speedboot.speedbotagent.dto;

import org.springframework.web.multipart.MultipartFile;

public class UploadFilesDTO {
    long userId;

    MultipartFile[] files;

    public UploadFilesDTO(long userId, MultipartFile[] files) {
        this.userId = userId;
        this.files = files;
    }

    public UploadFilesDTO() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
