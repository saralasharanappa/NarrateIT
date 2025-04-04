package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class StoryLoader {
    public static List<Story> loadStoriesFromCSV(String fileName) {
        List<Story> stories = new ArrayList<>();
        Map<String, List<ChapterWrapper>> storyMap = new HashMap<>();

        try (InputStream is = StoryLoader.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader reader = new InputStreamReader(is);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            csvReader.readNext(); // skip header
            while ((line = csvReader.readNext()) != null) {
                if (line.length < 4) continue;

                String storyId = line[0].trim();
                int chapterOrder = Integer.parseInt(line[1].trim());
                String text = line[2].trim();
                text = text.replaceFirst("^Story S\\d{3}, Chapter \\d{1,3}:\\s*", "");
                String imagePath = line[3].trim();
                Chapter chapter = new Chapter(text, imagePath);

                List<Decision> decisions = new ArrayList<>();
                if (line.length >= 6 && !line[4].isEmpty() && !line[5].isEmpty()) {
                    decisions.add(new Decision(line[4].trim(), parseIntegerOrDefault(line[5].trim(), -1)));
                }
                if (line.length >= 8 && !line[6].isEmpty() && !line[7].isEmpty()) {
                    decisions.add(new Decision(line[6].trim(), parseIntegerOrDefault(line[7].trim(), -1)));
                }
                chapter.setDecisions(decisions);

                ChapterWrapper wrapper = new ChapterWrapper(chapterOrder, chapter);
                storyMap.computeIfAbsent(storyId, k -> new ArrayList<>()).add(wrapper);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, List<ChapterWrapper>> entry : storyMap.entrySet()) {
            List<ChapterWrapper> wrappers = entry.getValue();
            wrappers.sort(Comparator.comparingInt(ChapterWrapper::getOrder));
            Story story = new Story();
            for (ChapterWrapper cw : wrappers) {
                story.addChapter(cw.getChapter());
            }
            stories.add(story);
        }
        return stories;
    }

    private static class ChapterWrapper {
        private final int order;
        private final Chapter chapter;
        public ChapterWrapper(int order, Chapter chapter) {
            this.order = order;
            this.chapter = chapter;
        }
        public int getOrder() { return order; }
        public Chapter getChapter() { return chapter; }
    }

    private static int parseIntegerOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
