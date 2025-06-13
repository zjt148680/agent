package com.speedboot.speedbotagent.base.entities.evaluation.t2retrieval;

import com.speedboot.speedbotagent.base.annotations.CsvBindingName;

import java.util.Objects;

public class Data {
    @CsvBindingName(column = "query-id")
    private int queryId;

    @CsvBindingName(column = "corpus-id")
    private int corpusId;

    @CsvBindingName(column = "score")
    private int score;

    public Data(int queryId, int corpusId, int score) {
        this.queryId = queryId;
        this.corpusId = corpusId;
        this.score = score;
    }

    public Data() {
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public int getCorpusId() {
        return corpusId;
    }

    public void setCorpusId(int corpusId) {
        this.corpusId = corpusId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // 重写equals和hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return queryId == data.getQueryId() &&
               corpusId == data.getCorpusId(); // 只比较这两个字段
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, corpusId); // 基于这两个字段生成哈希值
    }
}
