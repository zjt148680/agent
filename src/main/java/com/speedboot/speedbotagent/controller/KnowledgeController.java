package com.speedboot.speedbotagent.controller;

import com.speedboot.speedbotagent.dto.UploadFileRespDTO;
import com.speedboot.speedbotagent.dto.UploadFilesDTO;
import com.speedboot.speedbotagent.service.IKnowledgeManageService;
import com.speedboot.speedbotagent.vo.UploadFileRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/knowledge")
public class KnowledgeController {
    @Autowired
    private IKnowledgeManageService knowledgeManageService;

    @PostMapping(value = "/uploadFiles")
    public UploadFileRespVO uploadFiles(UploadFilesDTO uploadFilesDTO) {
        UploadFileRespDTO uploadFileRespDTO = knowledgeManageService.uploadFiles(uploadFilesDTO);
        return new UploadFileRespVO(uploadFileRespDTO);
    }
}
