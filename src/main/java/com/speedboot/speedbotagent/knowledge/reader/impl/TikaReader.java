package com.speedboot.speedbotagent.knowledge.reader.impl;

import com.speedboot.speedbotagent.knowledge.reader.IReader;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class TikaReader implements IReader {
    @Override
    public String read(String url) {
        // 使用PagePdfDocumentReader解析，只有文字，无格式。一页一项
        if (isLocalPath(url)) {
            return localRead(url);
        }
        throw new RuntimeException("暂不支持远程PDF文件解析");
    }

    @Override
    public String read(InputStream inputStream) {
        Resource resource = new InputStreamResource(inputStream);
        DocumentReader reader = new TikaDocumentReader(resource);
        return (reader.get()).get(0).getText();
    }

    private boolean isLocalPath(String path) {
        // todo
        return true;
    }

    /**
     * 本地读取
     *
     * @param localPath 本地路径
     * @return String
     */
    private String localRead(String localPath) {
        // 使用TikaDocumentReader解析，只有文字，无格式。
        FileSystemResource resource = new FileSystemResource(localPath);
        DocumentReader reader = new TikaDocumentReader(resource);
        return (reader.get()).get(0).getText();
    }

    /**
     * 本地按页读取
     *
     * @param localPath 本地路径
     * @return List<String>,一项代表一页的内容
     */
    private List<String> localReadByPage(String localPath) {
        // 使用PagePdfDocumentReader解析，只有文字，无格式。一页一项
        FileSystemResource resource = new FileSystemResource(localPath);
        DocumentReader reader = new PagePdfDocumentReader(resource);
        return reader.get().stream().map(Document::getText).toList();
    }
}
