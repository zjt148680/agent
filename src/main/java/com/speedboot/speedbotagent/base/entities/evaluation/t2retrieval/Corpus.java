package com.speedboot.speedbotagent.base.entities.evaluation.t2retrieval;

import com.speedboot.speedbotagent.base.annotations.CsvBindingName;

import java.util.Map;

public class Corpus {
    @CsvBindingName(column = "_id")
    private int id;

    @CsvBindingName(column = "text")
    private String text;

    @CsvBindingName(column = "title")
    private String title;

    public Corpus(int id, String text, String title) {
        this.id = id;
        this.text = text;
         this.title = title;
    }

    public Corpus(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Corpus() {
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "uid", id,
                "text", text
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
