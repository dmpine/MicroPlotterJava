package microplotter.controller;

import javax.swing.SwingUtilities;

import microplotter.model.ConfigurationModel;
import microplotter.model.DataProcessor.ParsedData;
import microplotter.networking.MqttManager;
import microplotter.view.MqttConfigDialog;

import microplotter.view.StatusBar;

// TODO: Add doxygen comments to this file

public class NetworkingController {
    private final ConfigurationModel configModel;
    private final MqttManager mqttManager;
    
    private final StatusBar statusBar;

    public NetworkingController(ConfigurationModel configModel, StatusBar statusBar) {
        this.configModel = configModel;
        this.mqttManager = new MqttManager();
        this.statusBar = statusBar;
    }
    
    public void handleMqttDialog(MqttConfigDialog dialog) {
        dialog.getSaveButton().addActionListener(e -> {
            dialog.saveAndClose();
            applyMqttConnectionState();
        });
    }

    /**
     * @brief Connects or disconnects the MQTT client based on the current configuration.
     * @details This method now runs the connection logic on a background thread to prevent
     * the GUI from freezing. It provides immediate feedback on the status bar.
     */
    public void applyMqttConnectionState() {
        if (!configModel.isMqttEnabled()) {
            mqttManager.disconnect();
            statusBar.setStatus("MQTT client disconnected.");
            return;
        }

        final String protocol = configModel.isMqttSslEnabled() ? "ssl" : "tcp";
        final String address = configModel.getMqttBrokerAddress();
        final String port = configModel.getMqttPort();
        final String username = configModel.getMqttUsername();
        final String password = configModel.getMqttPassword();
        final String brokerUri = address + ":" + port;

        // --- Step 1: Provide IMMEDIATE feedback on the UI thread ---
        statusBar.setStatus("MQTT: Connecting to " + brokerUri + ". Please wait...");

        // --- Step 2: Run the blocking connection logic on a new background thread ---
        new Thread(() -> {
            final boolean success = mqttManager.connect(protocol, address, port, username, password);

            // --- Step 3: Update the UI with the result on the Event Dispatch Thread ---
            SwingUtilities.invokeLater(() -> {
                if (success) {
                    statusBar.setStatus("MQTT: Successfully connected to " + brokerUri);
                } else {
                    statusBar.setStatus("MQTT: Connection failed. See console or log file for details.");
                }
            });
        }).start();
    }

    public void processAndPublishData(ParsedData parsedData) {
        // This method remains unchanged
        String baseTopic = configModel.getMqttBaseTopic();
        if (baseTopic == null || baseTopic.trim().isEmpty()) return;

        for (int i = 0; i < parsedData.columnCount; i++) {
            String tag = parsedData.tags[i] != null ? parsedData.tags[i] : "D" + i;
            String topic = baseTopic + "/" + tag;
            String payload = String.valueOf(parsedData.values[i]);
            mqttManager.publish(topic, payload);
        }
    }
    
    public void disconnectMqtt() {
        // This method remains unchanged
        mqttManager.disconnect();
    }
}