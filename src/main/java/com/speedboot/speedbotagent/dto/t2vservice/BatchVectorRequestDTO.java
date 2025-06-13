package com.speedboot.speedbotagent.dto.t2vservice;

import java.util.List;

public class BatchVectorRequestDTO {
    private List<String> texts;

    public BatchVectorRequestDTO(List<String> texts) {
        this.texts = texts;
    }

    public BatchVectorRequestDTO() {
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }
}
