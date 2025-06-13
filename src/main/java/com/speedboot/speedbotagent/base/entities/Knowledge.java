package com.speedboot.speedbotagent.base.entities;

public class Knowledge {
    private String text;

    public Knowledge(String text) {
        this.text = text;
    }

    public Knowledge() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
