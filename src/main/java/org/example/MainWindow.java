package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class MainWindow extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    JPanel navBar;

    MainMenuPanel mainMenuPanel;
    StoryPanel storyPanel;
    CustomizationPanel customizationPanel;
    SaveLoadPanel saveLoadPanel;
    HistoryPanel historyPanel;

    Story story;
    Character character;
    java.util.List<Story> storyList;            // List of available stories loaded from CSV
    java.util.List<String> currentGameHistory;  // Chapters from current game
    java.util.List<String> overallHistory;      // All finished games' chapters

    public MainWindow() throws URISyntaxException {
        setTitle("Interactive Storytelling App");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Load stories from CSV; fallback to a hardcoded story if CSV not found.
        storyList = createStories();
        story = getRandomStory(); // load a random story at startup
        character = new Character("Hero", "Male", "Adventurer", new ArrayList<>());
        currentGameHistory = new ArrayList<>();
        overallHistory = new ArrayList<>();

        // Create persistent navigation bar at the top
        navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this);
        storyPanel = new StoryPanel(this);
        customizationPanel = new CustomizationPanel(this);
        saveLoadPanel = new SaveLoadPanel(this);
        historyPanel = new HistoryPanel(this);

//        mainPanel.add(wrapWithBackground(mainMenuPanel, getClass().getClassLoader().getResource("backgrounds/start.jpeg").getPath()), "MainMenu");
        mainMenuPanel.setOpaque(false);
        mainPanel.add(wrapWithBackground(mainMenuPanel, "backgrounds/start.jpeg"), "MainMenu");
        mainPanel.add(storyPanel, "Story");
        mainPanel.add(customizationPanel, "Customization");
        mainPanel.add(saveLoadPanel, "SaveLoad");
        mainPanel.add(wrapWithBackground(historyPanel, "backgrounds/start.jpeg"), "History");
        mainPanel.setOpaque(false);

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "MainMenu");
    }

    private JPanel wrapWithBackground(JPanel panel, String imagePath) {
        try {
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            System.out.println("Image URL: " + imageUrl);
            if (imageUrl == null) {
                System.err.println("Background image not found: " + imagePath);
                return panel;
            }

            ImageIcon icon = new ImageIcon(imageUrl);
            JLabel bgLabel = new JLabel(icon);
            bgLabel.setLayout(new BorderLayout());
            bgLabel.add(panel, BorderLayout.CENTER);

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.add(bgLabel, BorderLayout.CENTER);
            return wrapper;
        } catch (Exception e) {
            e.printStackTrace();
            return panel;
        }
    }



    // Creates the global top navigation bar
    private JPanel createNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nav.setBackground(new Color(200, 220, 240));
        nav.setPreferredSize(new Dimension(1100, 50));

        JButton homeButton = new JButton("Home");
        homeButton.setToolTipText("Go to Main Menu");
        homeButton.addActionListener(e -> showPanel("MainMenu"));

        JButton newStoryButton = new JButton("New Story");
        newStoryButton.setToolTipText("Load a new random story");
        newStoryButton.addActionListener(e -> {
            story = getRandomStory();
            story.reset();
            currentGameHistory.clear();
            storyPanel.updateStory();
            showPanel("Story");
        });

        JButton customizationButton = new JButton("Customization");
        customizationButton.setToolTipText("Customize your character");
        customizationButton.addActionListener(e -> showPanel("Customization"));

        JButton historyButton = new JButton("History");
        historyButton.setToolTipText("View all games played");
        historyButton.addActionListener(e -> {
            historyPanel.updateHistory();
            showPanel("History");
        });

        JButton saveGameButton = new JButton("Save Game");
        saveGameButton.setToolTipText("Save your current progress");
        saveGameButton.addActionListener(e -> saveGame());

        nav.add(homeButton);
        nav.add(newStoryButton);
        nav.add(customizationButton);
        nav.add(historyButton);
        nav.add(saveGameButton);

        return nav;
    }

    // Loads stories from the CSV file using StoryLoader.
    private java.util.List<Story> createStories() {
        java.util.List<Story> stories = StoryLoader.loadStoriesFromCSV("stories.csv");
        if (stories.isEmpty()) {
            // Fallback to a hardcoded story if CSV load fails.
            stories.add(createFallbackForestStory());
        }
        return stories;
    }

    // Returns a random story from the loaded list.
    Story getRandomStory() {
        Random rand = new Random();
        int index = rand.nextInt(storyList.size());
        return storyList.get(index);
    }

    // Fallback hardcoded story if CSV not found (must be at least 15 chapters).
    private Story createFallbackForestStory() {
        Story story = new Story();
        for (int i = 1; i <= 16; i++) {
            String text = "Fallback Chapter " + i + ": This is a fallback story chapter to ensure a deep narrative.";
            String image = "forest" + i + ".jpg";
            Chapter ch = new Chapter(text, image);
            if (i < 16) {
                // For simplicity, offer one decision to go to the next chapter.
                ch.setDecisions(Arrays.asList(new Decision("Continue", i + 1)));
            }
            story.addChapter(ch);
        }
        return story;
    }

    // Record a chapter text in the current game's history.
    public void recordChapterHistory(String chapterText) {
        currentGameHistory.add(chapterText);
    }

    // Get overall history (combined finished games).
    public java.util.List<String> getOverallHistory() {
        return overallHistory;
    }

    // Finish the current game: archive its chapters in overall history.
    public void finishGame() {
        overallHistory.add("=== Game Start ===");
        overallHistory.addAll(currentGameHistory);
        overallHistory.add("=== End of Game ===\n");
        currentGameHistory.clear();
    }

    // Navigate to a specific panel.
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    // Save game state using serialization.
    public void saveGame() {
        try {
            FileOutputStream fileOut = new FileOutputStream("savegame.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(story);
            out.writeObject(character);
            out.writeObject(currentGameHistory);
            out.writeObject(overallHistory);
            out.close();
            fileOut.close();
            JOptionPane.showMessageDialog(this, "Game saved successfully!");
        } catch (IOException i) {
            i.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving game.");
        }
    }

    // Load game state using serialization.
    public void loadGame() {
        try {
            FileInputStream fileIn = new FileInputStream("savegame.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            story = (Story) in.readObject();
            character = (Character) in.readObject();
            currentGameHistory = (java.util.List<String>) in.readObject();
            overallHistory = (java.util.List<String>) in.readObject();
            in.close();
            fileIn.close();
            JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            storyPanel.updateStory();
            showPanel("Story");
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load game.");
        }
    }
}