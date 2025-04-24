package org.example;

import javax.swing.*;
import java.awt.*;

public class UIFactory {
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Theme.BUTTON_FONT);
        button.setBackground(Theme.BUTTON_BG);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return button;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(Theme.TITLE_FONT);
        label.setForeground(new Color(50, 50, 150));
        return label;
    }
}
