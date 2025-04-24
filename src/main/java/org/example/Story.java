package org.example;

import java.io.Serializable;
import java.util.ArrayList;

class Story implements Serializable {
    private ArrayList<Chapter> chapters;
    private int currentChapterIndex;

    public Story() {
        chapters = new ArrayList<>();
        currentChapterIndex = 0;
    }

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
    }

    public Chapter getCurrentChapter() {
        if (currentChapterIndex < chapters.size())
            return chapters.get(currentChapterIndex);
        return null;
    }

    public void advanceToChapter(int chapterIndex) {
        if (chapterIndex >= 0 && chapterIndex < chapters.size()) {
            currentChapterIndex = chapterIndex;
        } else {
            currentChapterIndex = chapters.size() - 1;
        }
    }

    public void reset() {
        currentChapterIndex = 0;
    }
}