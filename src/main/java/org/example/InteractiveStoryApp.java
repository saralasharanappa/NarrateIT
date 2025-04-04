package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class InteractiveStoryApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}

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
    List<Story> storyList;   // List of available stories
    List<String> currentGameHistory; // Chapters from current game
    List<String> overallHistory;     // All finished games' chapters

    public MainWindow() {
        setTitle("Interactive Storytelling App");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize stories, character, and histories
        storyList = createStories();
        story = storyList.get(0); // start with the first story (or randomize)
        character = new Character("Hero", "Male", "Adventurer", new ArrayList<>());
        currentGameHistory = new ArrayList<>();
        overallHistory = new ArrayList<>();

        // Create a persistent navigation bar at the top
        navBar = createNavBar();
        add(navBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this);
        storyPanel = new StoryPanel(this);
        customizationPanel = new CustomizationPanel(this);
        saveLoadPanel = new SaveLoadPanel(this);
        historyPanel = new HistoryPanel(this);

        mainPanel.add(mainMenuPanel, "MainMenu");
        mainPanel.add(storyPanel, "Story");
        mainPanel.add(customizationPanel, "Customization");
        mainPanel.add(saveLoadPanel, "SaveLoad");
        mainPanel.add(historyPanel, "History");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "MainMenu");
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
            newRandomStory();
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

    // Create list of stories – here we extend the Forest story to 16 chapters.
    private List<Story> createStories() {
        List<Story> stories = new ArrayList<>();
        stories.add(createForestStory());
        stories.add(createSpaceStory());
        stories.add(createMansionStory());
        return stories;
    }

    // Extended Forest Adventure with 16 chapters and branching decisions.
    private Story createForestStory() {
        Story story = new Story();

        Chapter ch1 = new Chapter("You wake up in a dark forest. What do you do?", "forest1.jpg");
        ch1.setDecisions(Arrays.asList(
                new Decision("Explore deeper", 1),
                new Decision("Stay put", 2)
        ));

        Chapter ch2 = new Chapter("You venture deeper and discover a sparkling stream.", "forest2.jpg");
        ch2.setDecisions(Arrays.asList(
                new Decision("Drink water", 3),
                new Decision("Follow the stream", 4)
        ));

        Chapter ch3 = new Chapter("Staying put, you sense a mysterious presence.", "forest3.jpg");
        ch3.setDecisions(Arrays.asList(
                new Decision("Investigate the presence", 5),
                new Decision("Ignore it", 6)
        ));

        Chapter ch4 = new Chapter("The water rejuvenates you, filling you with vitality.", "forest4.jpg");
        ch4.setDecisions(Arrays.asList(
                new Decision("Set up camp", 7),
                new Decision("Continue exploring", 8)
        ));

        Chapter ch5 = new Chapter("Your investigation leads you to ancient ruins.", "forest5.jpg");
        ch5.setDecisions(Arrays.asList(
                new Decision("Enter the ruins", 9),
                new Decision("Examine from afar", 10)
        ));

        Chapter ch6 = new Chapter("Ignoring the presence, you fall asleep under the stars.", "forest6.jpg");
        ch6.setDecisions(Arrays.asList(
                new Decision("Wake up early", 11),
                new Decision("Continue sleeping", 12)
        ));

        Chapter ch7 = new Chapter("At your camp, you find clues about a lost treasure.", "forest7.jpg");
        // Both decisions lead to the deep finale.
        ch7.setDecisions(Arrays.asList(
                new Decision("Follow the clues", 15),
                new Decision("Rest for the night", 15)
        ));

        Chapter ch8 = new Chapter("Exploring further, you encounter a wild animal.", "forest8.jpg");
        ch8.setDecisions(Arrays.asList(
                new Decision("Tame the animal", 15),
                new Decision("Run away", 15)
        ));

        Chapter ch9 = new Chapter("At the stream, you find a hidden waterfall.", "forest9.jpg");
        ch9.setDecisions(Arrays.asList(
                new Decision("Approach the waterfall", 15),
                new Decision("Collect water samples", 15)
        ));

        Chapter ch10 = new Chapter("Mysterious symbols carved in stone catch your eye.", "forest10.jpg");
        ch10.setDecisions(Arrays.asList(
                new Decision("Decode the symbols", 15),
                new Decision("Ignore them", 15)
        ));

        Chapter ch11 = new Chapter("You discover an ancient artifact near the ruins.", "forest11.jpg");
        ch11.setDecisions(Arrays.asList(
                new Decision("Study the artifact", 15),
                new Decision("Pocket it for later", 15)
        ));

        Chapter ch12 = new Chapter("A breathtaking sunrise fills the forest with golden light.", "forest12.jpg");
        ch12.setDecisions(Arrays.asList(
                new Decision("Begin a new path", 15),
                new Decision("Stay and reflect", 15)
        ));

        Chapter ch13 = new Chapter("Dreams of legendary heroes stir your soul.", "forest13.jpg");
        ch13.setDecisions(Arrays.asList(
                new Decision("Embrace the dream", 15),
                new Decision("Wake with resolve", 15)
        ));

        Chapter ch14 = new Chapter("The forest whispers ancient secrets as you tread lightly.", "forest14.jpg");
        ch14.setDecisions(Arrays.asList(
                new Decision("Listen closely", 15),
                new Decision("Move on silently", 15)
        ));

        Chapter ch15 = new Chapter("Every step feels fated as mysterious forces guide you.", "forest15.jpg");
        ch15.setDecisions(Arrays.asList(
                new Decision("Follow your destiny", 15),
                new Decision("Question the forces", 15)
        ));

        // Final chapter: no decisions – game ends.
        Chapter ch16 = new Chapter("Your journey through the forest comes to an epic conclusion, full of wisdom and wonder. The adventure may end, but the memories will last forever.", "forest16.jpg");
        // No decisions means this chapter is an ending.

        story.addChapter(ch1);
        story.addChapter(ch2);
        story.addChapter(ch3);
        story.addChapter(ch4);
        story.addChapter(ch5);
        story.addChapter(ch6);
        story.addChapter(ch7);
        story.addChapter(ch8);
        story.addChapter(ch9);
        story.addChapter(ch10);
        story.addChapter(ch11);
        story.addChapter(ch12);
        story.addChapter(ch13);
        story.addChapter(ch14);
        story.addChapter(ch15);
        story.addChapter(ch16);

        return story;
    }

    // A simple Space Odyssey story (for brevity)
    private Story createSpaceStory() {
        Story story = new Story();
        Chapter ch1 = new Chapter("You awaken aboard a drifting spaceship. What do you do?", "spaceship.jpg");
        ch1.setDecisions(Arrays.asList(
                new Decision("Explore the ship", 1),
                new Decision("Send a distress signal", 2)
        ));
        Chapter ch2 = new Chapter("You encounter an alien lifeform with a mysterious aura.", "alien.jpg");
        ch2.setDecisions(Arrays.asList(
                new Decision("Engage with the alien", 3),
                new Decision("Retreat to the control room", 4)
        ));
        Chapter ch3 = new Chapter("The alien shares secrets of the cosmos with you.", "cosmos.jpg");
        ch3.setDecisions(Arrays.asList(
                new Decision("Accept the wisdom", 4),
                new Decision("Search for escape", 4)
        ));
        // Final chapter for Space Story.
        Chapter ch4 = new Chapter("Your journey through the stars concludes as you are rescued. The universe awaits another adventure.", "rescue.jpg");
        story.addChapter(ch1);
        story.addChapter(ch2);
        story.addChapter(ch3);
        story.addChapter(ch4);
        return story;
    }

    // A simple Mystery Mansion story (for brevity)
    private Story createMansionStory() {
        Story story = new Story();
        Chapter ch1 = new Chapter("You arrive at an old mansion rumored to be haunted.", "mansion.jpg");
        ch1.setDecisions(Arrays.asList(
                new Decision("Enter the mansion", 1),
                new Decision("Walk around the mansion", 2)
        ));
        Chapter ch2 = new Chapter("Inside, eerie sounds and flickering lights unsettle you.", "ghost.jpg");
        ch2.setDecisions(Arrays.asList(
                new Decision("Investigate the noise", 3),
                new Decision("Retreat outside", 3)
        ));
        // Final chapter for Mansion Story.
        Chapter ch3 = new Chapter("The mansion reveals its secrets slowly, leaving you with haunting memories.", "mansion_end.jpg");
        story.addChapter(ch1);
        story.addChapter(ch2);
        story.addChapter(ch3);
        return story;
    }

    // Load a new random story; also archive current game history if not empty.
    public void newRandomStory() {
        if (!currentGameHistory.isEmpty()) {
            finishGame(); // Archive current game if it's in progress.
        }
        Random rand = new Random();
        int index = rand.nextInt(storyList.size());
        story = storyList.get(index);
        story.reset();
        currentGameHistory.clear();
    }

    // Record a chapter text in the current game's history.
    public void recordChapterHistory(String chapterText) {
        currentGameHistory.add(chapterText);
    }

    // Get overall history (combined finished games).
    public List<String> getOverallHistory() {
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
            currentGameHistory = (List<String>) in.readObject();
            overallHistory = (List<String>) in.readObject();
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

class MainMenuPanel extends JPanel {
    MainWindow mainWindow;

    public MainMenuPanel(MainWindow window) {
        this.mainWindow = window;
        setLayout(new BorderLayout());
        setBackground(new Color(220, 230, 250));

        JLabel titleLabel = new JLabel("Interactive Storytelling App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        titleLabel.setForeground(new Color(50, 50, 150));
        add(titleLabel, BorderLayout.CENTER);

        // Start button to launch the adventure
        JButton startButton = new JButton("Start Your Adventure");
        startButton.setToolTipText("Begin the story");
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
        startButton.addActionListener(e -> {
            mainWindow.showPanel("Story");
            mainWindow.recordChapterHistory(mainWindow.story.getCurrentChapter().getText());
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

class StoryPanel extends JPanel {
    MainWindow mainWindow;
    JTextArea storyTextArea;
    JPanel decisionPanel;
    JLabel imageLabel; // Displays the chapter image

    public StoryPanel(MainWindow window) {
        this.mainWindow = window;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Image display at the top
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.NORTH);

        storyTextArea = new JTextArea();
        storyTextArea.setLineWrap(true);
        storyTextArea.setWrapStyleWord(true);
        storyTextArea.setEditable(false);
        storyTextArea.setFont(new Font("Serif", Font.PLAIN, 20));
        storyTextArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(storyTextArea);
        add(scrollPane, BorderLayout.CENTER);

        decisionPanel = new JPanel();
        decisionPanel.setOpaque(false);
        add(decisionPanel, BorderLayout.SOUTH);

        updateStory();
    }

    public void updateStory() {
        // Get current chapter and update the UI accordingly
        Chapter currentChapter = mainWindow.story.getCurrentChapter();
        if (currentChapter != null) {
            String chapterText = currentChapter.getText();
            // Modify perspective based on user role
            String role = mainWindow.character.getRole();
            if (role != null && !role.isEmpty()) {
                switch (role) {
                    case "Warrior":
                        chapterText = "[Warrior Perspective] " + chapterText + " You feel a surge of strength.";
                        break;
                    case "Wizard":
                        chapterText = "[Wizard Perspective] " + chapterText + " Mystical energies swirl around you.";
                        break;
                    case "Diplomat":
                        chapterText = "[Diplomat Perspective] " + chapterText + " You carefully weigh the impact of every choice.";
                        break;
                    default:
                        // For 'Adventurer' or any other role, leave text as is.
                        break;
                }
            }
            storyTextArea.setText(chapterText);
            mainWindow.recordChapterHistory(chapterText);
            // Load and display image if available
            String imagePath = currentChapter.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(800, 300, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } else {
                imageLabel.setIcon(null);
            }
            decisionPanel.removeAll();
            List<Decision> decisions = currentChapter.getDecisions();
            if (decisions != null && !decisions.isEmpty()) {
                for (Decision d : decisions) {
                    JButton decisionButton = new JButton(d.getText());
                    decisionButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
                    decisionButton.setToolTipText("Choose this option");
                    decisionButton.addMouseListener(new MouseAdapter() {
                        Color original = decisionButton.getBackground();
                        public void mouseEntered(MouseEvent e) {
                            decisionButton.setBackground(new Color(180, 220, 250));
                        }
                        public void mouseExited(MouseEvent e) {
                            decisionButton.setBackground(original);
                        }
                    });
                    decisionButton.addActionListener(e -> {
                        mainWindow.story.advanceToChapter(d.getNextChapterIndex());
                        updateStory();
                    });
                    decisionPanel.add(decisionButton);
                }
            } else {
                // If no decisions, show Finish Game button.
                JButton finishButton = new JButton("Finish Game");
                finishButton.addActionListener(e -> {
                    mainWindow.finishGame();
                    JOptionPane.showMessageDialog(this, "Game Finished! Your journey has been recorded in History.");
                    mainWindow.showPanel("MainMenu");
                });
                decisionPanel.add(finishButton);
            }
            decisionPanel.revalidate();
            decisionPanel.repaint();
        }
    }
}

class CustomizationPanel extends JPanel {
    MainWindow mainWindow;
    JTextField nameField;
    JComboBox<String> genderCombo;
    JComboBox<String> roleCombo;
    JTextField traitsField; // comma separated list
    JButton saveButton;
    JButton backButton;

    public CustomizationPanel(MainWindow window) {
        this.mainWindow = window;
        setLayout(new GridBagLayout());
        setBackground(new Color(230, 250, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        nameField = new JTextField(15);
        nameField.setText(mainWindow.character.getName());

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        String[] genders = {"Male", "Female", "Other"};
        genderCombo = new JComboBox<>(genders);
        genderCombo.setSelectedItem(mainWindow.character.getGender());

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        String[] roles = {"Adventurer", "Warrior", "Wizard", "Diplomat"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setSelectedItem(mainWindow.character.getRole());

        JLabel traitsLabel = new JLabel("Traits (comma separated):");
        traitsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        traitsField = new JTextField(20);
        traitsField.setText(String.join(", ", mainWindow.character.getTraits()));

        saveButton = new JButton("Save Customization");
        backButton = new JButton("Back to Menu");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(genderLabel, gbc);
        gbc.gridx = 1;
        add(genderCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(roleLabel, gbc);
        gbc.gridx = 1;
        add(roleCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(traitsLabel, gbc);
        gbc.gridx = 1;
        add(traitsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(saveButton, gbc);
        gbc.gridx = 1;
        add(backButton, gbc);

        saveButton.addActionListener(e -> saveCustomization());
        backButton.addActionListener(e -> mainWindow.showPanel("MainMenu"));
    }

    private void saveCustomization() {
        String name = nameField.getText();
        String gender = (String) genderCombo.getSelectedItem();
        String role = (String) roleCombo.getSelectedItem();
        String traitsText = traitsField.getText();
        List<String> traits = new ArrayList<>();
        if (!traitsText.isEmpty()) {
            String[] splitTraits = traitsText.split(",");
            for (String t : splitTraits) {
                traits.add(t.trim());
            }
        }
        mainWindow.character.setName(name);
        mainWindow.character.setGender(gender);
        mainWindow.character.setRole(role);
        mainWindow.character.setTraits(traits);
        JOptionPane.showMessageDialog(this, "Character customization saved!");
    }
}

class SaveLoadPanel extends JPanel {
    MainWindow mainWindow;
    JButton saveButton;
    JButton loadButton;
    JButton backButton;

    public SaveLoadPanel(MainWindow window) {
        this.mainWindow = window;
        setLayout(new FlowLayout());
        setBackground(new Color(250, 240, 230));

        saveButton = new JButton("Save Game");
        loadButton = new JButton("Load Game");
        backButton = new JButton("Back to Menu");

        add(saveButton);
        add(loadButton);
        add(backButton);

        saveButton.addActionListener(e -> mainWindow.saveGame());
        loadButton.addActionListener(e -> mainWindow.loadGame());
        backButton.addActionListener(e -> mainWindow.showPanel("MainMenu"));
    }
}

class HistoryPanel extends JPanel {
    MainWindow mainWindow;
    JTextArea historyArea;
    JButton backButton;

    public HistoryPanel(MainWindow window) {
        this.mainWindow = window;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 250));

        JLabel title = new JLabel("Game History", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainWindow.showPanel("MainMenu"));
        add(backButton, BorderLayout.SOUTH);
    }

    public void updateHistory() {
        StringBuilder sb = new StringBuilder();
        for (String line : mainWindow.getOverallHistory()) {
            sb.append(line).append("\n");
        }
        historyArea.setText(sb.toString());
    }
}

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

class Chapter implements Serializable {
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

class Character implements Serializable {
    private String name;
    private String gender;
    private String role;
    private List<String> traits;

    public Character(String name, String gender, String role, List<String> traits) {
        this.name = name;
        this.gender = gender;
        this.role = role;
        this.traits = traits;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<String> getTraits() { return traits; }
    public void setTraits(List<String> traits) { this.traits = traits; }
}
