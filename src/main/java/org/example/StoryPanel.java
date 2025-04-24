package org.example;

import javax.swing.*;
import java.awt.*;

class StoryPanel extends JPanel {
    MainWindow mainWindow;
    JTextArea storyTextArea;
    JPanel decisionPanel;
    JLabel imageLabel;

    public StoryPanel(MainWindow window) {
        this.mainWindow = window;

        // Layout with gradient background
        setLayout(new BorderLayout());

        // Gradient background rendering
        setOpaque(false);

        // Top optional chapter image
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(800, 200));
        add(imageLabel, BorderLayout.NORTH);

        // Center story text area with padding
        storyTextArea = new JTextArea();
        storyTextArea.setLineWrap(true);
        storyTextArea.setWrapStyleWord(true);
        storyTextArea.setEditable(false);
        storyTextArea.setFont(new Font("Serif", Font.PLAIN, 20));
        storyTextArea.setMargin(new Insets(20, 20, 20, 20));
        storyTextArea.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(storyTextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Bottom decision buttons area
        decisionPanel = new JPanel();
        decisionPanel.setOpaque(false);
        decisionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        add(decisionPanel, BorderLayout.SOUTH);

        updateStory();
    }

    // Override paintComponent to draw gradient
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        Color color1 = new Color(240, 248, 255);
        Color color2 = new Color(220, 230, 250);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        super.paintComponent(g);
    }

    public void updateStory() {
        Chapter currentChapter = mainWindow.story.getCurrentChapter();
        if (currentChapter != null) {
            String chapterText = currentChapter.getText();
            String role = mainWindow.character.getRole();
            if (role != null && !role.isEmpty()) {
                switch (role) {
                    case "Warrior":
                        chapterText = "[Warrior] " + chapterText + " Strength courses through you.";
                        break;
                    case "Wizard":
                        chapterText = "[Wizard] " + chapterText + " You sense hidden magic.";
                        break;
                    case "Diplomat":
                        chapterText = "[Diplomat] " + chapterText + " Your mind weighs each word.";
                        break;
                }
            }
            storyTextArea.setText(chapterText);
            mainWindow.recordChapterHistory(chapterText);

            String imagePath = currentChapter.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(800, 200, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } else {
                imageLabel.setIcon(null);
            }

            decisionPanel.removeAll();
            java.util.List<Decision> decisions = currentChapter.getDecisions();
            if (decisions != null && !decisions.isEmpty()) {
                for (Decision d : decisions) {
                    JButton decisionButton = createStyledDecisionButton(d.getText());
                    decisionButton.addActionListener(e -> {
                        mainWindow.story.advanceToChapter(d.getNextChapterIndex());
                        updateStory();
                    });
                    decisionPanel.add(decisionButton);
                }
            } else {
                JButton finishButton = createStyledDecisionButton("🏁 Finish Game");
                finishButton.addActionListener(e -> {
                    mainWindow.finishGame();
                    JOptionPane.showMessageDialog(this, "Game Finished! Check History.");
                    mainWindow.showPanel("MainMenu");
                });
                decisionPanel.add(finishButton);
            }

            decisionPanel.revalidate();
            decisionPanel.repaint();
        }
    }

    private JButton createStyledDecisionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18)); // Larger font for visibility
        button.setBackground(new Color(100, 180, 250)); // Base background color
        button.setForeground(Color.WHITE); // Text color
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Padding inside the button (top, left, bottom, right)
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Make button stretch horizontally, with fixed height
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect - lighter blue when hovered
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(120, 200, 255)); // Lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 180, 250)); // Original color
            }
        });

        return button;
    }

}

