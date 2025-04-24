package org.example;

import javax.swing.*;
import java.awt.*;

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