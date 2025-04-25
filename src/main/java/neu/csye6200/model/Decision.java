package neu.csye6200.model;

import java.io.Serializable;

public class Decision implements Serializable {
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