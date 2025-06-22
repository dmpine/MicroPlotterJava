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
 * Panel for terminal output and input.
 * This code is extracted from Layout.create_term_elements.
 */
public class TerminalPanel extends JPanel {

    private JTextField messageTextField;
    private JButton sendButton;
    private JCheckBox addCRCheckBox;
    private JCheckBox addNLCheckBox;
    private JCheckBox timestampCheckBox;
    private JCheckBox autoScrollCheckBox;
    private JButton recordButton;
    private JTextArea terminalTextArea;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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

        timestampCheckBox = new JCheckBox("TimeStamp", true); // 
        topControls.add(timestampCheckBox);

        autoScrollCheckBox = new JCheckBox("AutoScroll", true); // 
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
    
    public void appendText(String data) {
        String textToAppend = data;
        if (timestampCheckBox.isSelected()) {
            textToAppend = formatter.format(new Date()) + "\t" + data; // 
        }
        terminalTextArea.append(textToAppend + "\n");
        
        if (autoScrollCheckBox.isSelected()) {
            terminalTextArea.setCaretPosition(terminalTextArea.getDocument().getLength()); // 
        }
    }

    // --- Add Getters for components ---
    public JTextField getMessageTextField() { return messageTextField; }
    public JButton getSendButton() { return sendButton; }
    public JCheckBox getAddCRCheckBox() { return addCRCheckBox; }
    public JCheckBox getAddNLCheckBox() { return addNLCheckBox; }
    public JButton getRecordButton() { return recordButton; }
}