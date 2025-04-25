package neu.csye6200.controller;



import com.formdev.flatlaf.FlatDarculaLaf;
import neu.csye6200.model.Chapter;
import neu.csye6200.model.Decision;
import neu.csye6200.model.Story;
import neu.csye6200.view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainWindow extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    JPanel navBar;

    MainMenuPanel mainMenuPanel;
    public StoryPanel storyPanel;
    CustomizationPanel customizationPanel;
    SaveLoadPanel saveLoadPanel;
    HistoryPanel historyPanel;

    public Story story;
    public neu.csye6200.model.Character character;
    java.util.List<Story> storyList;
    public java.util.List<String> currentGameHistory;
    java.util.List<String> overallHistory;

    private boolean isDarkMode = false;

    public MainWindow() throws URISyntaxException {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Interactive Storytelling App");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        storyList = createStories();
        story = getRandomStory();
        character = new neu.csye6200.model.Character("Hero", "Male", "Adventurer", new ArrayList<>());
        currentGameHistory = new ArrayList<>();
        overallHistory = new ArrayList<>();

        navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this);
        storyPanel = new StoryPanel(this);
        customizationPanel = new CustomizationPanel(this);
        saveLoadPanel = new SaveLoadPanel(this);
        historyPanel = new HistoryPanel(this);

        mainPanel.add(wrapWithGradientBackground(mainMenuPanel), "MainMenu");
        mainPanel.add(storyPanel, "Story");
        mainPanel.add(customizationPanel, "Customization");
        mainPanel.add(saveLoadPanel, "SaveLoad");
        mainPanel.add(wrapWithGradientBackground(historyPanel), "History");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "MainMenu");
    }

    private JPanel wrapWithGradientBackground(JPanel panel) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(200, 220, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createNavBar() {
        JPanel nav = new JPanel();
//        nav.setLayout(new BoxLayout(nav, BoxLayout.X_AXIS));
//        nav.setBackground(new Color(240, 245, 255));
        nav.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Modern styled buttons with icons
        nav.add(createStyledNavButton("🏠 Home", e -> showPanel("MainMenu")));
        nav.add(Box.createHorizontalStrut(15));
        nav.add(createStyledNavButton("📖 New Story", e -> {
            story = getRandomStory();
            story.reset();
            currentGameHistory.clear();
            storyPanel.updateStory();
            showPanel("Story");
        }));
        nav.add(Box.createHorizontalStrut(15));
        nav.add(createStyledNavButton("🎨 Customization", e -> showPanel("Customization")));
        nav.add(Box.createHorizontalStrut(15));
        nav.add(createStyledNavButton("🕒 History", e -> {
            historyPanel.updateHistory();
            showPanel("History");
        }));
        nav.add(Box.createHorizontalStrut(15));
        nav.add(createStyledNavButton("💾 Save Game", e -> saveGame()));

        // New Theme Toggle Button
        nav.add(Box.createHorizontalStrut(15));
        nav.add(createStyledNavButton("🌓 Toggle Theme", e -> toggleTheme()));

        return nav;
    }

    private JButton createStyledNavButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(new Color(50, 50, 100));
        button.setBackground(new Color(220, 230, 250));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(50, 50, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 250), 10, true));
        button.addActionListener(action);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 230, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 230, 250));
            }
        });

        return button;
    }


    private JButton createNavButton(String text, String tooltip, ActionListener action) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(180, 220, 250));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }

    private java.util.List<Story> createStories() {
        java.util.List<Story> stories = StoryLoader.loadStoriesFromCSV("stories.csv");
        if (stories.isEmpty()) {
            stories.add(createFallbackForestStory());
        }
        return stories;
    }

    public Story getRandomStory() {
        Random rand = new Random();
        int index = rand.nextInt(storyList.size());
        return storyList.get(index);
    }

    private Story createFallbackForestStory() {
        Story story = new Story();
        for (int i = 1; i <= 16; i++) {
            String text = "Fallback Chapter " + i + ": This is a fallback story chapter to ensure a deep narrative.";
            String image = "forest" + i + ".jpg";
            Chapter ch = new Chapter(text, image);
            if (i < 16) {
                ch.setDecisions(Arrays.asList(new Decision("Continue", i + 1)));
            }
            story.addChapter(ch);
        }
        return story;
    }

    public void recordChapterHistory(String chapterText) {
        currentGameHistory.add(chapterText);
    }

    public java.util.List<String> getOverallHistory() {
        return overallHistory;
    }

    public void finishGame() {
        overallHistory.add("=== Game Start ===");
        overallHistory.addAll(currentGameHistory);
        overallHistory.add("=== End of Game ===\n");
        currentGameHistory.clear();
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

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

    public void loadGame() {
        try {
            FileInputStream fileIn = new FileInputStream("savegame.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            story = (Story) in.readObject();
            character = (neu.csye6200.model.Character) in.readObject();
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

    private void toggleTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } else {
                UIManager.setLookAndFeel(new FlatDarculaLaf()); // More contrast
            }
            isDarkMode = !isDarkMode;

            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }


}
