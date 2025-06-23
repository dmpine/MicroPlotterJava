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
 * @brief A JPanel containing UI components for serial port configuration.
 * @details This class encapsulates all user controls for finding, selecting, and
 * connecting to a serial port, including baud rate selection. Its code is
 * extracted from the original Layout.create_port_conf_elements method.
 */
@SuppressWarnings("serial")
public class PortConfigPanel extends JPanel {

    /** @brief The button to trigger a search for available serial ports. */
    private JButton searchButton;
    /** @brief Combo box to display and select from the list of available serial ports. */
    private JComboBox<String> portComboBox;
    /** @brief Combo box to select the baud rate for the serial connection. */
    private JComboBox<String> baudRateComboBox;
    /** @brief The button to connect to or disconnect from the selected serial port. */
    private JButton connectButton;

    /**
     * @brief Constructs the PortConfigPanel.
     * @details Sets up the panel layout and creates and arranges all the UI controls
     * for serial port configuration.
     * @param width The initial width of the panel.
     */
    public PortConfigPanel() {
        super(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Port configuration"));
        //setPreferredSize(new Dimension(width - 2, 60));

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

    /** @brief Gets the search button. @return The search JButton instance. */
    public JButton getSearchButton() {
        return searchButton;
    }

    /** @brief Gets the port selection combo box. @return The port JComboBox instance. */
    public JComboBox<String> getPortComboBox() {
        return portComboBox;
    }

    /** @brief Gets the baud rate selection combo box. @return The baud rate JComboBox instance. */
    public JComboBox<String> getBaudRateComboBox() {
        return baudRateComboBox;
    }

    /** @brief Gets the connect/disconnect button. @return The connect JButton instance. */
    public JButton getConnectButton() {
        return connectButton;
    }
}