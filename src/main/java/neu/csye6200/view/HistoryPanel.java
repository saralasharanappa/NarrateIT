package neu.csye6200.view;

import neu.csye6200.controller.MainWindow;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {
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