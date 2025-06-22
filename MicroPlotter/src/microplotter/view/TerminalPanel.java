package microplotter.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @brief A JPanel that provides the user interface for the serial terminal.
 * @details This class encapsulates all UI components related to the terminal, including
 * the output text area, the input field for sending messages, and various
 * configuration checkboxes. Its code is extracted from the original
 * Layout.create_term_elements method.
 */
@SuppressWarnings("serial")
public class TerminalPanel extends JPanel {

    /** @brief The text field for user input to be sent over serial. */
    private JTextField messageTextField;
    /** @brief The button to send the message from the text field. */
    private JButton sendButton;
    /** @brief Checkbox to automatically add a carriage return ('\r') to sent messages. */
    private JCheckBox addCRCheckBox;
    /** @brief Checkbox to automatically add a newline ('\n') to sent messages. */
    private JCheckBox addNLCheckBox;
    /** @brief Checkbox to toggle the display of timestamps for received data. */
    private JCheckBox timestampCheckBox;
    /** @brief Checkbox to toggle automatic scrolling to the bottom of the terminal. */
    private JCheckBox autoScrollCheckBox;
    /** @brief The button to start or stop recording terminal data to a file. */
    private JButton recordButton;
    /** @brief The text area where received serial data is displayed. */
    private JTextArea terminalTextArea;
    /** @brief The formatter for generating timestamps. */
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * @brief Constructs the TerminalPanel.
     * @details Sets up the panel layout and creates and arranges all the UI controls
     * for the terminal interface.
     * @param width The initial width of the panel.
     * @param height The initial height of the panel.
     */
    public TerminalPanel(int width, int height) {
        setBorder(BorderFactory.createTitledBorder("Terminal"));
        setPreferredSize(new Dimension(width - 2, height));

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        topControls.add(new JLabel("Serial Message"));
        messageTextField = new JTextField(20);
        topControls.add(messageTextField);

        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        topControls.add(sendButton);

        addCRCheckBox = new JCheckBox("Add CR");
        addCRCheckBox.setEnabled(false);
        topControls.add(addCRCheckBox);

        addNLCheckBox = new JCheckBox("Add NL");
        addNLCheckBox.setEnabled(false);
        topControls.add(addNLCheckBox);

        timestampCheckBox = new JCheckBox("TimeStamp", true);
        topControls.add(timestampCheckBox);

        autoScrollCheckBox = new JCheckBox("AutoScroll", true);
        topControls.add(autoScrollCheckBox);

        recordButton = new JButton("Begin rec");
        recordButton.setEnabled(false);
        topControls.add(recordButton);

        terminalTextArea = new JTextArea();
        terminalTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(terminalTextArea);
        scrollPane.setPreferredSize(new Dimension(width - 25, 120));

        subPanel.add(topControls);
        subPanel.add(scrollPane);
        add(subPanel);
    }
    
    /**
     * @brief Appends a line of text to the terminal display area and returns the formatted string.
     * @details Optionally prepends a formatted timestamp and handles auto-scrolling.
     * It now returns the final string that was appended to the text area.
     * @param data The string data to append to the terminal.
     * @return The fully formatted string that was displayed.
     */
    public String appendText(String data) {
        String textToAppend = data;
        if (timestampCheckBox.isSelected()) {
            textToAppend = formatter.format(new Date()) + "\t" + data;
        }
        terminalTextArea.append(textToAppend + "\n");
        
        if (autoScrollCheckBox.isSelected()) {
            terminalTextArea.setCaretPosition(terminalTextArea.getDocument().getLength());
        }
        return textToAppend; // Return the final, formatted string
    }

    /**
     * @brief Gets the text field for sending messages.
     * @return The instance of the message JTextField.
     */
    public JTextField getMessageTextField() { return messageTextField; }
    /**
     * @brief Gets the "Send" button.
     * @return The instance of the send JButton.
     */
    public JButton getSendButton() { return sendButton; }
    /**
     * @brief Gets the "Add CR" checkbox.
     * @return The instance of the Add CR JCheckBox.
     */
    public JCheckBox getAddCRCheckBox() { return addCRCheckBox; }
    /**
     * @brief Gets the "Add NL" checkbox.
     * @return The instance of the Add NL JCheckBox.
     */
    public JCheckBox getAddNLCheckBox() { return addNLCheckBox; }
    /**
     * @brief Gets the record button.
     * @return The instance of the record JButton.
     */
    public JButton getRecordButton() { return recordButton; }
}