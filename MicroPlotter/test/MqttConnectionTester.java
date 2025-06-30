//package microplotter;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnectionTester {

    public static void main(String[] args) {
        // --- This test connects to a public, open MQTT broker ---
        // It requires NO username or password.
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = "PublicTest_" + System.currentTimeMillis();

        try {
            System.out.println("Attempting to connect to PUBLIC test broker: " + broker);
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            // No username, password, or SSL is set for this simple test.

            client.connect(options);

            System.out.println("----------------------------------------");
            System.out.println("SUCCESS! Connection to public broker established.");
            System.out.println("This proves your network allows MQTT traffic and the Paho library is working.");
            System.out.println("----------------------------------------");

            client.disconnect();
            System.out.println("Disconnected.");

        } catch (Exception e) {
            System.err.println("----------------------------------------");
            System.err.println("CONNECTION FAILED. Error details:");
            System.err.println("This likely indicates a firewall is blocking MQTT traffic on port 1883.");
            System.err.println("----------------------------------------");
            e.printStackTrace();
        }
    }
}