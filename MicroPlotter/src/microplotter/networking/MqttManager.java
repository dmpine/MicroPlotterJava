package microplotter.networking;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import microplotter.utils.AppLogger;

// TODO: Add doxygen comments to this file

public class MqttManager {
    private MqttClient client;

    public boolean connect(String protocol, String address, String port, String username, String password) {
        String brokerUri = protocol + "://" + address + ":" + port;
        try {
            if (client != null && client.isConnected()) disconnect();
            
            String clientId = "MicroPlotter_" + System.currentTimeMillis();
            client = new MqttClient(brokerUri, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            if (username != null && !username.trim().isEmpty()) {
                options.setUserName(username);
            }
            if (password != null && !password.trim().isEmpty()) {
                options.setPassword(password.toCharArray());
            }
            options.setCleanSession(true);

            AppLogger.info("Connecting to MQTT broker: " + brokerUri);
            client.connect(options);
            AppLogger.info("MQTT connected successfully.");
            return true;
        } catch (Exception e) {
        	AppLogger.severe("MQTT Connection failed: " + e.getMessage(), e);
            return false;
        }
    }

    public void publish(String topic, String payload) {
        if (client == null || !client.isConnected()) return;
        new Thread(() -> {
            try {
                client.publish(topic, new MqttMessage(payload.getBytes()));
            } catch (Exception e) { 
                AppLogger.severe("MQTT Publish failed: " + e.getMessage(), e);
            }
        }).start();
    }

    public void disconnect() {
        try {
            if (client != null && client.isConnected()) client.disconnect();
        } catch (Exception e) { /* Ignore */ }
    }
}