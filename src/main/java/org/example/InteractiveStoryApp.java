package org.example;

import javax.swing.*;
import java.net.URISyntaxException;

public class InteractiveStoryApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = null;
            try {
                window = new MainWindow();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            window.setVisible(true);
        });
    }
}