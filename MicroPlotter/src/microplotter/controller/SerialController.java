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

import java.util.ArrayList;
import java.util.List;
import microplotter.networking.HttpRequestManager;

import microplotter.model.DataProcessor;

/**
 * @brief Controller for all serial port related actions.
 * @details This class manages all user interactions related to serial communication.
 * It handles port searching, connecting/disconnecting, and sending data from the terminal.
 * It also implements the SerialPortDataListener to receive incoming data asynchronously,
 * process it, and forward it to other parts of the application like the terminal and the plot controller.
 * Its logic is derived from the original Layout.java and Control.java classes.
 */
public class SerialController implements SerialPortDataListener {

    /** @brief The main application window, used to access UI panels. */
	private final MainWindow mainWindow;
    /** @brief The model responsible for managing the serial port connection. */
    private final SerialPortManager serialManager;
    /** @brief The model holding the application's configuration and state. */
    private final ConfigurationModel configModel;
    /** @brief The utility class for handling file recording. */
    private final FileManager fileManager;
    /** @brief The controller responsible for plotting logic. */
    private final PlotController plotController;
    /** @brief The specific UI panel for port configuration. */
    private final PortConfigPanel portConfigPanel;
    /** @brief The specific UI panel for the terminal. */
    private final TerminalPanel terminalPanel;

    /** @brief A buffer to accumulate incoming serial data until a newline is received. */
    private String receivedDataBuffer = "";
    
    /** @brief A buffer to hold data lines before sending them over the network. */
    private final List<String> networkDataBuffer = new ArrayList<>();
    
    private final NetworkingController networkingController;

    /**
     * @brief Constructs the SerialController.
     * @details Initializes the controller and injects all its dependencies. It retrieves specific
     * view panels from the main window and then calls initListeners() to attach event handlers.
     * @param mainWindow The main application window.
     * @param serialManager The serial port management model.
     * @param configModel The application configuration model.
     * @param fileManager The file management utility.
     * @param plotController The plot controller, for forwarding data.
     */
    public SerialController(MainWindow mainWindow, SerialPortManager serialManager, ConfigurationModel configModel, FileManager fileManager, PlotController plotController, NetworkingController networkingController) {
        this.mainWindow = mainWindow;
        this.serialManager = serialManager;
        this.configModel = configModel;
        this.fileManager = fileManager;
        this.plotController = plotController;
        this.networkingController = networkingController;
        this.portConfigPanel = mainWindow.getPortConfigPanel();
        this.terminalPanel = mainWindow.getTerminalPanel();
        initListeners();
    }

    /**
     * @brief Attaches action listeners to all relevant UI components.
     * @details This method centralizes the wiring of UI events (button clicks, etc.)
     * to their corresponding handler methods in this controller.
     */
    private void initListeners() {
        // Add listeners for components in PortConfigPanel
        portConfigPanel.getSearchButton().addActionListener(e -> searchPorts());
        portConfigPanel.getConnectButton().addActionListener(e -> toggleConnection());
        terminalPanel.getClearTerminalButton().addActionListener(e -> terminalPanel.clearTerminal());

        // Add listeners for components in TerminalPanel
        terminalPanel.getSendButton().addActionListener(e -> sendMessage());
        terminalPanel.getMessageTextField().addActionListener(e -> sendMessage()); // Allow sending with Enter key
        terminalPanel.getRecordButton().addActionListener(e -> toggleRecording());
    }

    /**
     * @brief Searches for available serial ports and updates the UI combo box.
     * @details Logic is derived from the original Layout.search_port method.
     */
    private void searchPorts() {
        portConfigPanel.getPortComboBox().removeAllItems();
        java.util.List<String> portNames = serialManager.getAvailablePorts();
        for (String portName : portNames) {
            portConfigPanel.getPortComboBox().addItem(portName);
        }

        if (!"No port found".equals(portConfigPanel.getPortComboBox().getItemAt(0))) {
            portConfigPanel.getConnectButton().setEnabled(true);
            portConfigPanel.getSearchButton().setText("Update port list");
        } else {
            portConfigPanel.getConnectButton().setEnabled(false);
        }
    }

    /**
     * @brief Toggles the serial port connection state by calling connect() or disconnect().
     * @details Logic is derived from the original Layout.connect_port method.
     */
    private void toggleConnection() {
        if (configModel.isConnected()) {
            disconnect();
        } else {
            connect();
        }
    }

    /**
     * @brief Connects to the selected serial port with the chosen baud rate.
     */
    private void connect() {
        String selectedPort = (String) portConfigPanel.getPortComboBox().getSelectedItem();
        int baudRate = Integer.parseInt((String) portConfigPanel.getBaudRateComboBox().getSelectedItem());

        if (serialManager.connect(selectedPort, baudRate)) {
            configModel.setConnected(true);
            serialManager.addDataListener(this);
            updateUIForConnectionState();
        }
    }

    /**
     * @brief Disconnects from the current serial port.
     */
    private void disconnect() {
        serialManager.disconnect();
        configModel.setConnected(false);
        if(fileManager.isRecording()){
            fileManager.stopRecording();
        }
        updateUIForConnectionState();
    }
    
    /**
     * @brief Sends a message from the terminal text field over the serial port.
     * @details Appends CR and/or NL characters based on checkbox selections. Logic is
     * derived from the original Control.send_message method.
     */
    private void sendMessage() {
        if (!configModel.isConnected()) return;

        String message = terminalPanel.getMessageTextField().getText();
        if (terminalPanel.getAddCRCheckBox().isSelected()) {
            message += "\r";
        }
        if (terminalPanel.getAddNLCheckBox().isSelected()) {
            message += "\n";
        }

        serialManager.writeData(message);
        terminalPanel.getMessageTextField().setText("");
    }
    
    /**
     * @brief Toggles the data recording state.
     * @details Starts or stops recording data to a text file using the FileManager.
     */
    private void toggleRecording() {
        if (fileManager.isRecording()) {
            fileManager.stopRecording();
            terminalPanel.getRecordButton().setText("Begin rec");
            terminalPanel.getRecordButton().setBackground(Color.gray);
        } else {
            if (fileManager.startRecording(mainWindow)) {
                terminalPanel.getRecordButton().setText("Stop rec");
                terminalPanel.getRecordButton().setBackground(Color.red);
            }
        }
    }

    /**
     * @brief Updates the enabled/disabled state of UI elements based on connection status.
     * @details This centralizes all UI state changes that depend on whether the application
     * is connected to a serial port.
     */
    private void updateUIForConnectionState() {
        boolean connected = configModel.isConnected();
        
        PlotConfigPanel plotConfigPanel = mainWindow.getPlotConfigPanel();

        // Update button text and color
        portConfigPanel.getConnectButton().setText(connected ? "Disconnect" : "Connect");
        portConfigPanel.getConnectButton().setBackground(connected ? Color.RED : Color.gray);

        // Enable/disable components
        portConfigPanel.getSearchButton().setEnabled(!connected);
        portConfigPanel.getPortComboBox().setEnabled(!connected);
        portConfigPanel.getBaudRateComboBox().setEnabled(!connected);

        terminalPanel.getSendButton().setEnabled(connected);
        terminalPanel.getAddCRCheckBox().setEnabled(connected);
        terminalPanel.getAddNLCheckBox().setEnabled(connected);
        terminalPanel.getRecordButton().setEnabled(connected);
        
        plotConfigPanel.getPlotButton().setEnabled(connected);

        // If disconnected, reset the record button state
        if (!connected) {
            terminalPanel.getRecordButton().setText("Begin rec");
            terminalPanel.getRecordButton().setBackground(Color.gray);
        }
    }

    /**
     * @override
     * @brief Specifies which serial port events this listener is interested in.
     * @return The event mask for data availability.
     */
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    /**
     * @override
     * @brief The callback method invoked by jSerialComm when a serial event occurs.
     * @details Reads all available data from the input stream, adds it to a buffer,
     * and processes the buffer line by line, passing each complete line to processReceivedLine.
     * @param event The serial port event that occurred.
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        }

        try {
            InputStream in = serialManager.getCurrentPort().getInputStream();
            byte[] readBuffer = new byte[in.available()];
            in.read(readBuffer);
            String chunk = new String(readBuffer);
            receivedDataBuffer += chunk;

            // Process data line by line
            if (receivedDataBuffer.contains("\n")) {
                String[] lines = receivedDataBuffer.split("\n", -1);
                for (int i = 0; i < lines.length - 1; i++) {
                    String line = lines[i].replace("\r", "");
                    if (!line.isEmpty()) {
                        processReceivedLine(line);
                    }
                }
                receivedDataBuffer = lines[lines.length - 1]; // Keep the last partial line
            }
        } catch (Exception e) {
            System.err.println("Serial event error: " + e.getMessage());
        }
    }

    /**
     * @brief Processes a single, complete line of data received from the serial port.
     * @details This method ensures that all UI updates happen on the Event Dispatch Thread (EDT).
     * It forwards the data line to the terminal, the file manager, and the plot controller.
     * @param line The complete data string, without newline characters.
     */
    private void processReceivedLine(final String line) {
        SwingUtilities.invokeLater(() -> {
            String displayedLine = terminalPanel.appendText(line);
            if (fileManager.isRecording()) {
                fileManager.writeData(displayedLine);
            }
            if (configModel.isPlotting()) {
                plotController.processData(line);
            }

            DataProcessor.ParsedData parsedData = DataProcessor.parseSerialData(line, true);
            if (configModel.isHttpEnabled()) {
                networkDataBuffer.add(line);
                
                if (networkDataBuffer.size() >= configModel.getNetworkBufferLimit()) {
                    // Use a StringBuilder for efficient string creation
                    StringBuilder payloadBuilder = new StringBuilder();
                    payloadBuilder.append("["); // Start of JSON array

                    for (int i = 0; i < networkDataBuffer.size(); i++) {
                        String dataLine = networkDataBuffer.get(i);
                        DataProcessor.ParsedData parsed = DataProcessor.parseSerialData(dataLine, true);

                        if (parsed.columnCount > 0) {
                            payloadBuilder.append("{"); // Start of JSON object for this line
                            for (int j = 0; j < parsed.columnCount; j++) {
                                payloadBuilder.append("\"").append(parsed.tags[j]).append("\":");
                                payloadBuilder.append(parsed.values[j]);
                                if (j < parsed.columnCount - 1) {
                                    payloadBuilder.append(",");
                                }
                            }
                            payloadBuilder.append("}"); // End of JSON object
                            
                            if (i < networkDataBuffer.size() - 1) {
                                payloadBuilder.append(",");
                            }
                        }
                    }
                    payloadBuilder.append("]"); // End of JSON array

                    // Send the data
                    HttpRequestManager.sendPostRequest(configModel.getHttpUrl(), payloadBuilder.toString());
                    
                    // Clear the buffer
                    networkDataBuffer.clear();
                }
            }
            if (configModel.isMqttEnabled()) {
                networkingController.processAndPublishData(parsedData);
            }
        });
    }
}