package org.example;

import java.io.Serializable;

class Decision implements Serializable {
    private String text;
    private int nextChapterIndex;

    public Decision(String text, int nextChapterIndex) {
        this.text = text;
        this.nextChapterIndex = nextChapterIndex;
    }

    public String getText() {
        return text;
    }

    public int getNextChapterIndex() {
        return nextChapterIndex;
    }
}