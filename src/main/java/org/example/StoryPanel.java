package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                        chapterText = "[Diplomat Perspective] " + chapterText + " You carefully weigh every decision.";
                        break;
                    default:
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
            java.util.List<Decision> decisions = currentChapter.getDecisions();
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