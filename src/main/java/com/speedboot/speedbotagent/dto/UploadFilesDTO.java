package com.speedboot.speedbotagent.dto;

import org.springframework.web.multipart.MultipartFile;

public class UploadFilesDTO {
    int userId;

    MultipartFile[] files;

    public UploadFilesDTO(int userId, MultipartFile[] files) {
        this.userId = userId;
        this.files = files;
    }

    public UploadFilesDTO() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
