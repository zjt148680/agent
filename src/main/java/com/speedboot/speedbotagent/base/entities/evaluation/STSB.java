package com.speedboot.speedbotagent.base.entities.evaluation;

import com.speedboot.speedbotagent.base.annotations.CsvBindingName;

public class STSB {
    @CsvBindingName(column = "sentence1")
    private String sentence1;

    @CsvBindingName(column = "sentence2")
    private String sentence2;

    @CsvBindingName(column = "score")
    private double score;

    public STSB(String sentence1, String sentence2, int score) {
        this.sentence1 = sentence1;
        this.sentence2 = sentence2;
        this.score = score;
    }

    public STSB() {
    }

    public String getSentence2() {
        return sentence2;
    }

    public void setSentence2(String sentence2) {
        this.sentence2 = sentence2;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSentence1() {
        return sentence1;
    }

    public void setSentence1(String sentence1) {
        this.sentence1 = sentence1;
    }
}
