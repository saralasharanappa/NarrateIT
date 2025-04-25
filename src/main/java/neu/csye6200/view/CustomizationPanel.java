package neu.csye6200.view;


import neu.csye6200.controller.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CustomizationPanel extends JPanel {
    MainWindow mainWindow;
    JTextField nameField;
    JComboBox<String> genderCombo;
    JComboBox<String> roleCombo;
    JTextField traitsField;
    JButton saveButton;
    JButton backButton;
    JLabel previewLabel;

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

        previewLabel = new JLabel();
        previewLabel.setFont(Theme.BODY_FONT);
        updatePreview();

        gbc.gridx = 0; gbc.gridy = 0; add(nameLabel, gbc);
        gbc.gridx = 1; add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(genderLabel, gbc);
        gbc.gridx = 1; add(genderCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(roleLabel, gbc);
        gbc.gridx = 1; add(roleCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(traitsLabel, gbc);
        gbc.gridx = 1; add(traitsField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(saveButton, gbc);
        gbc.gridx = 1; add(backButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(previewLabel, gbc);

        saveButton.addActionListener(e -> saveCustomization());
        backButton.addActionListener(e -> mainWindow.showPanel("MainMenu"));
    }

    private void saveCustomization() {
        String name = nameField.getText();
        String gender = (String) genderCombo.getSelectedItem();
        String role = (String) roleCombo.getSelectedItem();
        String traitsText = traitsField.getText();
        java.util.List<String> traits = new ArrayList<>();
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
        updatePreview();
        JOptionPane.showMessageDialog(this, "Character customization saved!");
    }

    private void updatePreview() {
        String preview = String.format("Preview - Name: %s, Gender: %s, Role: %s, Traits: %s",
                nameField.getText(), genderCombo.getSelectedItem(), roleCombo.getSelectedItem(), traitsField.getText());
        previewLabel.setText(preview);
    }
}
