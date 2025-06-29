package microplotter.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import microplotter.model.ConfigurationModel;

/**
 * @brief A dialog for configuring the HTTP data sending feature.
 */
@SuppressWarnings("serial")
public class HttpConfigDialog extends JDialog {

    private final ConfigurationModel configModel;
    private JCheckBox enableCheckBox;
    private JTextField urlTextField;
    private JSpinner bufferSpinner;

    public HttpConfigDialog(JFrame parent, ConfigurationModel model) {
        super(parent, "HTTP Endpoint Configuration", true);
        this.configModel = model;

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- NEW: Use GridBagLayout for precise control ---
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4); // Add some padding around components
        gbc.anchor = GridBagConstraints.WEST; // Default alignment

        // Row 0: Enable CheckBox (spans two columns)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        enableCheckBox = new JCheckBox("Enable HTTP Sending", model.isHttpEnabled());
        fieldsPanel.add(enableCheckBox, gbc);

        // Row 1: URL Label and TextField
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to default
        gbc.anchor = GridBagConstraints.EAST; // Right-align the label
        fieldsPanel.add(new JLabel("Endpoint URL:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow field to stretch horizontally
        gbc.weightx = 1.0; // Give all extra horizontal space to this component
        urlTextField = new JTextField(model.getHttpUrl(), 60);
        fieldsPanel.add(urlTextField, gbc);

        // Row 2: Buffer Label and Spinner
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE; // Reset fill
        gbc.weightx = 0; // Reset weight
        gbc.anchor = GridBagConstraints.EAST;
        fieldsPanel.add(new JLabel("Send data every (lines):"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        bufferSpinner = new JSpinner(new SpinnerNumberModel(model.getNetworkBufferLimit(), 1, 100, 1));
        fieldsPanel.add(bufferSpinner, gbc);
        
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        // --- Buttons Panel (unchanged) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveAndClose());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack(); // pack() works great with GridBagLayout
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void saveAndClose() {
        configModel.setHttpEnabled(enableCheckBox.isSelected());
        configModel.setHttpUrl(urlTextField.getText());
        configModel.setNetworkBufferLimit((Integer) bufferSpinner.getValue());
        dispose();
    }
}