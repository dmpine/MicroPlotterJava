package microplotter.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import microplotter.utils.Constants;

/**
 * Panel for serial port configuration UI components.
 * This code is extracted from Layout.create_port_conf_elements.
 */
public class PortConfigPanel extends JPanel {

    private JButton searchButton;
    private JComboBox<String> portComboBox;
    private JComboBox<String> baudRateComboBox;
    private JButton connectButton;

    public PortConfigPanel(int width) {
        super(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Port configuration")); 
        setPreferredSize(new Dimension(width - 2, 60)); // Adjusted height

        // Button for searching available port
        searchButton = new JButton("Search port");
        searchButton.setPreferredSize(new Dimension(150, 25)); 
        add(searchButton);

        // ComboBox for choosing from found ports
        portComboBox = new JComboBox<>();
        portComboBox.addItem("No Port"); 
        portComboBox.setPreferredSize(new Dimension(250, 25));
        add(portComboBox);

        // Label and ComboBox for choosing the baud rate
        add(new JLabel("Baud rate:"));
        baudRateComboBox = new JComboBox<>(Constants.BAUD_RATES); 
        baudRateComboBox.setSelectedItem(Constants.DEFAULT_BAUD_RATE); 
        add(baudRateComboBox);

        // Button for connecting to port
        connectButton = new JButton("Connect"); 
        connectButton.setPreferredSize(new Dimension(100, 25)); 
        connectButton.setOpaque(true);
        connectButton.setBackground(Color.gray); 
        connectButton.setEnabled(false);
        add(connectButton);
    }

    // --- Getters for Controllers to access UI components ---

    public JButton getSearchButton() {
        return searchButton;
    }

    public JComboBox<String> getPortComboBox() {
        return portComboBox;
    }

    public JComboBox<String> getBaudRateComboBox() {
        return baudRateComboBox;
    }

    public JButton getConnectButton() {
        return connectButton;
    }
}