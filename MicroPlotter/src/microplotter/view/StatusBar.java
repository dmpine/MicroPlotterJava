package microplotter.view;

import javax.swing.*;
import java.awt.*;

/**
 * @brief A simple panel to display application status messages.
 * @details This panel sits at the bottom of the main window and provides
 * a single line of text for feedback on operations like connections.
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel {
    private final JLabel statusLabel;

    public StatusBar() {
        super(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createLoweredBevelBorder());

        statusLabel = new JLabel("Ready.");
        statusLabel.setPreferredSize(new Dimension(950, 16)); // Give it some default size
        add(statusLabel);
    }

    /**
     * @brief Sets the message to be displayed on the status bar.
     * @param message The text to display.
     */
    public void setStatus(String message) {
        statusLabel.setText(" " + message); // Add a little padding
    }
}