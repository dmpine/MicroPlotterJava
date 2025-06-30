package microplotter.controller;

import microplotter.model.ConfigurationModel;
import microplotter.model.DataProcessor.ParsedData;
import microplotter.networking.MqttManager;
import microplotter.view.MqttConfigDialog;

// TODO: Add doxygen comments to this file

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

    public void applyMqttConnectionState() {
        if (configModel.isMqttEnabled()) {
            mqttManager.connect(
                configModel.getMqttProtocol(),
                configModel.getMqttBrokerAddress(),
                configModel.getMqttPort(),
                configModel.getMqttUsername(),
                configModel.getMqttPassword()
            );
        } else {
            mqttManager.disconnect();
        }
    }

    public void processAndPublishData(ParsedData parsedData) {
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
        mqttManager.disconnect();
    }
}