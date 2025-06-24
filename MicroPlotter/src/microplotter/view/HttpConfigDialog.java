package microplotter.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

        // --- Configuration Fields ---
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        enableCheckBox = new JCheckBox("Enable HTTP Sending", model.isHttpEnabled());
        urlTextField = new JTextField(model.getHttpUrl(), 40);
        bufferSpinner = new JSpinner(new SpinnerNumberModel(model.getNetworkBufferLimit(), 1, 100, 1));

        fieldsPanel.add(enableCheckBox);
        fieldsPanel.add(new JLabel()); // Spacer
        fieldsPanel.add(new JLabel("Endpoint URL:"));
        fieldsPanel.add(urlTextField);
        fieldsPanel.add(new JLabel("Send data every (lines):"));
        fieldsPanel.add(bufferSpinner);
        
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveAndClose());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void saveAndClose() {
        configModel.setHttpEnabled(enableCheckBox.isSelected());
        configModel.setHttpUrl(urlTextField.getText());
        configModel.setNetworkBufferLimit((Integer) bufferSpinner.getValue());
        dispose(); // Close the dialog
    }
}