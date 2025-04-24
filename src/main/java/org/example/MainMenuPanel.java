package org.example;

import javax.swing.*;
import java.awt.*;

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

        // Start button to launch the adventure – loads a new random story.
        JButton startButton = new JButton("Start Your Adventure");
        startButton.setToolTipText("Begin the story");
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
        startButton.addActionListener(e -> {
            // On start, always load a new random story.
            mainWindow.story = mainWindow.getRandomStory();
            mainWindow.story.reset();
            mainWindow.currentGameHistory.clear();
            mainWindow.recordChapterHistory(mainWindow.story.getCurrentChapter().getText());
            mainWindow.showPanel("Story");
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}