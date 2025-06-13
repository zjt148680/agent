package com.speedboot.speedbotagent.base.entities.evaluation.t2retrieval;

import com.speedboot.speedbotagent.base.annotations.CsvBindingName;

import java.util.Objects;

public class Queries {
    @CsvBindingName(column = "_id")
    private int id;

    @CsvBindingName(column = "text")
    private String text;

    public Queries(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Queries() {
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

    // 重写equals和hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queries q = (Queries) o;
        return id == q.getId() &&
               Objects.equals(text, q.getText()); // 只比较这两个字段
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text); // 基于这两个字段生成哈希值
    }
}
