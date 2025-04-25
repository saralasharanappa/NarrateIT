package neu.csye6200.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chapter implements Serializable {
    private String text;
    private List<Decision> decisions;
    private String imagePath;

    public Chapter(String text, String imagePath) {
        this.text = text;
        this.imagePath = imagePath;
        decisions = new ArrayList<>();
    }

    public Chapter(String text) {
        this(text, "");
    }

    public String getText() {
        return text;
    }

    public void setDecisions(List<Decision> decisions) {
        this.decisions = decisions;
    }

    public List<Decision> getDecisions() {
        return decisions;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }
}