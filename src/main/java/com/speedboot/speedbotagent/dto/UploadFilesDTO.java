package com.speedboot.speedbotagent.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UploadFilesDTO {
    long userId;

    MultipartFile[] files;

    List<String> urls;

    public UploadFilesDTO(long userId, MultipartFile[] files, List<String> urls) {
        this.userId = userId;
        this.files = files;
        this.urls = urls;
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

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
