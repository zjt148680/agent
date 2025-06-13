package com.speedboot.speedbotagent.dto.t2vservice;

public class VectorRequestDTO {
    private String text;

    public VectorRequestDTO(String text) {
        this.text = text;
    }

    public VectorRequestDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
