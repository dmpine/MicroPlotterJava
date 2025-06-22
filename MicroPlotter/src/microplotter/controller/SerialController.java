package microplotter.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortDataListener;
import java.awt.Color;
import java.io.InputStream;
import javax.swing.SwingUtilities;
import microplotter.model.ConfigurationModel;
import microplotter.model.SerialPortManager;
import microplotter.utils.FileManager;
import microplotter.view.MainWindow;
import microplotter.view.PlotConfigPanel;
import microplotter.view.PortConfigPanel;
import microplotter.view.TerminalPanel;

/**
 * Controller for all serial port related actions.
 * Manages port searching, connection, data sending, and receiving.
 * Implements logic from Layout.java and Control.java.
 */
public class SerialController implements SerialPortDataListener {

	private final MainWindow mainWindow;
    private final SerialPortManager serialManager;
    private final ConfigurationModel configModel;
    private final FileManager fileManager;
    private final PlotController plotController; // Reference to PlotController
    private final PortConfigPanel portConfigPanel;
    private final TerminalPanel terminalPanel;

    private String receivedDataBuffer = "";

    public SerialController(MainWindow mainWindow, SerialPortManager serialManager, ConfigurationModel configModel, FileManager fileManager, PlotController plotController) {
        this.mainWindow = mainWindow;
        this.serialManager = serialManager;
        this.configModel = configModel;
        this.fileManager = fileManager;
        this.plotController = plotController; // Store the reference
        this.portConfigPanel = mainWindow.getPortConfigPanel();
        this.terminalPanel = mainWindow.getTerminalPanel();
        initListeners();
    }

    private void initListeners() {
        // Add listeners for components in PortConfigPanel
        portConfigPanel.getSearchButton().addActionListener(e -> searchPorts());
        portConfigPanel.getConnectButton().addActionListener(e -> toggleConnection());

        // Add listeners for components in TerminalPanel
        terminalPanel.getSendButton().addActionListener(e -> sendMessage());
        terminalPanel.getMessageTextField().addActionListener(e -> sendMessage()); // Allow sending with Enter key
        terminalPanel.getRecordButton().addActionListener(e -> toggleRecording());
    }

    /**
     * Searches for available serial ports and updates the combo box.
     * Logic from Layout.search_port. 
     */
    private void searchPorts() {
        portConfigPanel.getPortComboBox().removeAllItems(); // 
        java.util.List<String> portNames = serialManager.getAvailablePorts(); // 
        for (String portName : portNames) {
            portConfigPanel.getPortComboBox().addItem(portName);
        }

        if (!"No port found".equals(portConfigPanel.getPortComboBox().getItemAt(0))) {
            portConfigPanel.getConnectButton().setEnabled(true); // 
            portConfigPanel.getSearchButton().setText("Update port list"); // 
        } else {
            portConfigPanel.getConnectButton().setEnabled(false); // 
        }
    }

    /**
     * Toggles the serial port connection state.
     * Logic from Layout.connect_port. 
     */
    private void toggleConnection() {
        if (configModel.isConnected()) {
            disconnect();
        } else {
            connect();
        }
    }

    private void connect() {
        String selectedPort = (String) portConfigPanel.getPortComboBox().getSelectedItem();
        int baudRate = Integer.parseInt((String) portConfigPanel.getBaudRateComboBox().getSelectedItem());

        if (serialManager.connect(selectedPort, baudRate)) { // 
            configModel.setConnected(true);
            serialManager.addDataListener(this); // 
            updateUIForConnectionState();
        }
    }

    private void disconnect() {
        serialManager.disconnect(); // 
        configModel.setConnected(false);
        if(fileManager.isRecording()){
            fileManager.stopRecording();
        }
        updateUIForConnectionState();
    }
    
    /**
     * Sends a message from the terminal text field over the serial port.
     * Logic from Control.send_message. 
     */
    private void sendMessage() {
        if (!configModel.isConnected()) return;

        String message = terminalPanel.getMessageTextField().getText(); // 
        if (terminalPanel.getAddCRCheckBox().isSelected()) {
            message += "\r"; // 
        }
        if (terminalPanel.getAddNLCheckBox().isSelected()) {
            message += "\n"; // 
        }

        serialManager.writeData(message); // 
        terminalPanel.getMessageTextField().setText(""); // 
    }
    
    private void toggleRecording() {
        if (fileManager.isRecording()) {
            fileManager.stopRecording();
            terminalPanel.getRecordButton().setText("Begin rec"); // 
            terminalPanel.getRecordButton().setBackground(Color.gray); // 
        } else {
            if (fileManager.startRecording(mainWindow)) {
                terminalPanel.getRecordButton().setText("Stop rec"); // 
                terminalPanel.getRecordButton().setBackground(Color.red); // 
            }
        }
    }

    /**
     * Updates the enabled/disabled state of UI elements based on connection status.
     * Logic from Layout.connect_port and Control.plotting_form_actions.
     */
    private void updateUIForConnectionState() {
        boolean connected = configModel.isConnected();
        
        // Panels that depend on connection state
        PlotConfigPanel plotConfigPanel = mainWindow.getPlotConfigPanel();

        // Update button text and color
        portConfigPanel.getConnectButton().setText(connected ? "Disconnect" : "Connect"); // 
        portConfigPanel.getConnectButton().setBackground(connected ? Color.RED : Color.gray); // 

        // Enable/disable components
        portConfigPanel.getSearchButton().setEnabled(!connected); // 
        portConfigPanel.getPortComboBox().setEnabled(!connected); // 
        portConfigPanel.getBaudRateComboBox().setEnabled(!connected); // 

        terminalPanel.getSendButton().setEnabled(connected); // 
        terminalPanel.getAddCRCheckBox().setEnabled(connected); // 
        terminalPanel.getAddNLCheckBox().setEnabled(connected); // 
        terminalPanel.getRecordButton().setEnabled(connected); // 
        
        plotConfigPanel.getPlotButton().setEnabled(connected); // 

        // If disconnected, reset the record button state
        if (!connected) {
            terminalPanel.getRecordButton().setText("Begin rec"); // 
            terminalPanel.getRecordButton().setBackground(Color.gray); // 
        }
    }


    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; // 
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return; // 
        }

        try {
            InputStream in = serialManager.getCurrentPort().getInputStream(); // 
            byte[] readBuffer = new byte[in.available()]; // 
            in.read(readBuffer); // 
            String chunk = new String(readBuffer); // 
            receivedDataBuffer += chunk;

            // Process data line by line
            if (receivedDataBuffer.contains("\n")) {
                String[] lines = receivedDataBuffer.split("\n", -1);
                for (int i = 0; i < lines.length - 1; i++) {
                    String line = lines[i].replace("\r", ""); // 
                    if (!line.isEmpty()) { // 
                        processReceivedLine(line);
                    }
                }
                receivedDataBuffer = lines[lines.length - 1]; // Keep the last partial line
            }
        } catch (Exception e) {
            System.err.println("Serial event error: " + e.getMessage()); // 
        }
    }

    private void processReceivedLine(final String line) {
        // UI updates must run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> { // 
            terminalPanel.appendText(line);
            if (fileManager.isRecording()) {
                fileManager.writeData(line);
            }
            if (configModel.isPlotting()) {
                plotController.processData(line);
            }
        });
    }
}