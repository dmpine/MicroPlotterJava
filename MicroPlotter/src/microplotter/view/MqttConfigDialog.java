package microplotter.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import microplotter.model.ConfigurationModel;

// TODO: Add doxygen comments to this file

@SuppressWarnings("serial")
public class MqttConfigDialog extends JDialog {

    private final ConfigurationModel configModel;
    private JCheckBox enableCheckBox;
    private JCheckBox sslCheckBox;
    private JTextField brokerField;
    private JTextField portField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField baseTopicField;
    private JButton saveButton;

    public MqttConfigDialog(JFrame parent, ConfigurationModel model) {
        super(parent, "MQTT Connection Configuration", true);
        this.configModel = model;

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Enable Checkbox
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        enableCheckBox = new JCheckBox("Enable MQTT Publishing", model.isMqttEnabled());
        fieldsPanel.add(enableCheckBox, gbc);

        // Row 1: SSL Checkbox
        gbc.gridy = 1;
        sslCheckBox = new JCheckBox("Use SSL/TLS", "ssl".equals(model.getMqttProtocol()));
        fieldsPanel.add(sslCheckBox, gbc);

        // Reset grid constraints for entries
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        // Add fields for Broker, Port, etc.
        addEntry(fieldsPanel, gbc, 2, "Broker Address:", brokerField = new JTextField(model.getMqttBrokerAddress(), 30));
        addEntry(fieldsPanel, gbc, 3, "Port:", portField = new JTextField(model.getMqttPort(), 30));
        addEntry(fieldsPanel, gbc, 4, "Username (Optional):", usernameField = new JTextField(model.getMqttUsername(), 30));
        addEntry(fieldsPanel, gbc, 5, "Password (Optional):", passwordField = new JPasswordField(model.getMqttPassword(), 30));
        addEntry(fieldsPanel, gbc, 6, "Base Topic:", baseTopicField = new JTextField(model.getMqttBaseTopic(), 30));

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.saveButton = new JButton("Save & Apply");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void addEntry(JPanel p, GridBagConstraints g, int y, String l, JComponent c) {
        g.gridy = y; g.gridx = 0; g.fill = GridBagConstraints.NONE; g.weightx = 0; p.add(new JLabel(l), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0; p.add(c, g);
    }

    public void saveAndClose() {
        configModel.setMqttEnabled(enableCheckBox.isSelected());
        configModel.setMqttProtocol(sslCheckBox.isSelected() ? "ssl" : "tcp");
        configModel.setMqttBrokerAddress(brokerField.getText());
        configModel.setMqttPort(portField.getText());
        configModel.setMqttUsername(usernameField.getText());
        configModel.setMqttPassword(new String(passwordField.getPassword()));
        configModel.setMqttBaseTopic(baseTopicField.getText());
        dispose();
    }
    
    public JButton getSaveButton() { return this.saveButton; }
}