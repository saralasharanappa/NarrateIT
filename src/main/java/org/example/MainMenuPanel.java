package org.example;

import javax.swing.*;
import java.awt.*;

class MainMenuPanel extends JPanel {
    MainWindow mainWindow;
    private float alpha = 0.0f; // For fade-in subtitle
    private float starScale = 1.0f; // For pulsing stars
    private boolean scalingUp = true;

    public MainMenuPanel(MainWindow window) {
        this.mainWindow = window;
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel subtitle = new JLabel("Craft your journey. Choose your fate.") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        subtitle.setForeground(new Color(100, 100, 180));

        PulsingStarLabel starsLeft = new PulsingStarLabel("✨");
        PulsingStarLabel starsRight = new PulsingStarLabel("✨");

        JButton startButton = new JButton("Start Your Adventure");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        startButton.setBackground(new Color(100, 180, 250));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton.addActionListener(e -> {
            mainWindow.story = mainWindow.getRandomStory();
            mainWindow.story.reset();
            mainWindow.currentGameHistory.clear();
            mainWindow.recordChapterHistory(mainWindow.story.getCurrentChapter().getText());
            mainWindow.showPanel("Story");
            mainWindow.storyPanel.updateStory();
        });

        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(120, 200, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(100, 180, 250));
            }
        });

        JPanel starsPanel = new JPanel(new FlowLayout());
        starsPanel.setOpaque(false);
        starsPanel.add(starsLeft);
        starsPanel.add(Box.createHorizontalStrut(20));
        starsPanel.add(subtitle);
        starsPanel.add(Box.createHorizontalStrut(20));
        starsPanel.add(starsRight);

        gbc.gridy = 0;
        add(starsPanel, gbc);

        gbc.gridy++;
        add(startButton, gbc);

        // Animation Timer for Fade-In Effect
        Timer fadeTimer = new Timer(50, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer) e.getSource()).stop();
            }
            subtitle.repaint();
        });
        fadeTimer.start();

        // Timer for Pulsing Stars
        Timer starPulseTimer = new Timer(100, e -> {
            if (scalingUp) {
                starScale += 0.05f;
                if (starScale >= 1.3f) scalingUp = false;
            } else {
                starScale -= 0.05f;
                if (starScale <= 1.0f) scalingUp = true;
            }
            starsLeft.setScale(starScale);
            starsRight.setScale(starScale);
        });
        starPulseTimer.start();
    }

    // Custom JLabel for pulsing effect
    static class PulsingStarLabel extends JLabel {
        private float scale = 4.0f;

        public PulsingStarLabel(String text) {
            super(text);
            setFont(new Font("Serif", Font.PLAIN, 36));
            setForeground(new Color(255, 215, 0)); // Gold-like color
        }

        public void setScale(float scale) {
            this.scale = scale;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.translate(w / 2, h / 2);
            g2.scale(scale, scale);
            g2.translate(-w / 2, -h / 2);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}
