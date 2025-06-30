package microplotter.controller;

import microplotter.model.ConfigurationModel;
import microplotter.model.DataProcessor.ParsedData;
import microplotter.networking.MqttManager;
import microplotter.view.MqttConfigDialog;

public class NetworkingController {
    private final ConfigurationModel configModel;
    private final MqttManager mqttManager;

    public NetworkingController(ConfigurationModel configModel) {
        this.configModel = configModel;
        this.mqttManager = new MqttManager();
    }

    public void handleMqttDialog(MqttConfigDialog dialog) {
        dialog.getSaveButton().addActionListener(e -> {
            dialog.saveAndClose();
            applyMqttConnectionState();
        });
    }

    /**
     * @brief Connects or disconnects the MQTT client based on the current configuration.
     * @details This method contains the core logic to handle both standard MQTT connections
     * and the special authentication required for the Arduino IoT Cloud.
     */
    public void applyMqttConnectionState() {
        if (!configModel.isMqttEnabled()) {
            mqttManager.disconnect();
            return;
        }

        // This is now a simple, general-purpose connection logic
        String protocol = configModel.isMqttSslEnabled() ? "ssl" : "tcp";
        
        mqttManager.connect(
            protocol,
            configModel.getMqttBrokerAddress(),
            configModel.getMqttPort(),
            configModel.getMqttUsername(),
            configModel.getMqttPassword()
        );
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