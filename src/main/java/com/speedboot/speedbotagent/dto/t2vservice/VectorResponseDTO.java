package com.speedboot.speedbotagent.dto.t2vservice;

import java.util.List;

public class VectorResponseDTO {
    String text;

    List<Double> vector;

    int dim;

    public VectorResponseDTO() {
    }

    public VectorResponseDTO(String text, List<Double> vector, int dim) {
        this.text = text;
        this.vector = vector;
        this.dim = dim;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Double> getVector() {
        return vector;
    }

    public void setVector(List<Double> vector) {
        this.vector = vector;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
}
