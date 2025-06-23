package microplotter.view;

import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;

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
	public TerminalPanel() {
	    // The main panel will stack its children vertically
	    super(new BorderLayout(5, 5));
	    setBorder(BorderFactory.createTitledBorder("Terminal"));
	
	    // --- NEW, ROBUST LAYOUT STRUCTURE ---
	
	    // 1. Create a panel for the top input line using BorderLayout
	    JPanel inputLinePanel = new JPanel(new BorderLayout(5, 5));
	    
	    inputLinePanel.add(new JLabel(" Serial Message:"), BorderLayout.WEST);
	
	    // This text field is now in the CENTER, so it will stretch horizontally
	    messageTextField = new JTextField(); // No need for a column count
	    inputLinePanel.add(messageTextField, BorderLayout.CENTER);
	    
	    sendButton = new JButton("Send");
	    sendButton.setEnabled(false);
	    inputLinePanel.add(sendButton, BorderLayout.EAST);
	
	    // 2. Create a separate panel for the checkboxes using FlowLayout
	    JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
	
	    addCRCheckBox = new JCheckBox("Add CR");
	    addCRCheckBox.setEnabled(false);
	    optionsPanel.add(addCRCheckBox);
	
	    addNLCheckBox = new JCheckBox("Add NL");
	    addNLCheckBox.setEnabled(false);
	    optionsPanel.add(addNLCheckBox);
	
	    timestampCheckBox = new JCheckBox("TimeStamp", true);
	    optionsPanel.add(timestampCheckBox);
	
	    autoScrollCheckBox = new JCheckBox("AutoScroll", true);
	    optionsPanel.add(autoScrollCheckBox);
	
	    recordButton = new JButton("Begin rec");
	    recordButton.setEnabled(false);
	    optionsPanel.add(recordButton);
	    
	    // 3. Combine the top panels into one container
	    JPanel topSectionPanel = new JPanel(new BorderLayout());
	    topSectionPanel.add(inputLinePanel, BorderLayout.NORTH);
	    topSectionPanel.add(optionsPanel, BorderLayout.CENTER);
	
	    // 4. The main terminal output area
	    terminalTextArea = new JTextArea(9, 0); // 8 rows, flexible columns
	    terminalTextArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(terminalTextArea);
	
	    // 5. Add the new structure to the main TerminalPanel
	    add(topSectionPanel, BorderLayout.NORTH);
	    add(scrollPane, BorderLayout.CENTER);
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