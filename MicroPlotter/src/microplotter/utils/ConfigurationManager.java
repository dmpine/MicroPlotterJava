package microplotter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import microplotter.model.ConfigurationModel;

import microplotter.utils.AppLogger;

/**
 * @brief Manages saving and loading of the application's configuration.
 * @details This utility class handles the conversion of a ConfigurationModel object
 * to and from a .properties file.
 */
public class ConfigurationManager {

    /**
     * @brief Loads configuration settings from a specified file into the model.
     * @param model The ConfigurationModel object to update.
     * @param file The File to load the properties from.
     */
	public static void loadConfiguration(ConfigurationModel model, File file) {
        Properties props = new Properties();
        if (!file.exists()) return;

        try (InputStream input = new FileInputStream(file)) {
            props.load(input);

            // Load plot settings
            model.setPlotPresentation(props.getProperty("plot.presentation", "Static"));
            model.setDynamicSampleLimit(Integer.parseInt(props.getProperty("plot.sampleLimit", "50")));
            model.setUpdateTime(Double.parseDouble(props.getProperty("plot.updateTime", "3.0")));
            model.setLineWidth(Integer.parseInt(props.getProperty("plot.lineWidth", "2")));
            model.setXAxisType(props.getProperty("plot.xAxisType", "Dec"));
            model.setYAxisType(props.getProperty("plot.yAxisType", "Dec"));
            model.setUseTagsAsNames(Boolean.parseBoolean(props.getProperty("plot.useTagsAsNames", "false")));
            
            // Load HTTP networking settings
            model.setHttpEnabled(Boolean.parseBoolean(props.getProperty("network.http.enabled", "false")));
            model.setHttpUrl(props.getProperty("network.http.url", ""));
            model.setNetworkBufferLimit(Integer.parseInt(props.getProperty("network.http.bufferLimit", "10")));

            // Load MQTT networking settings
            model.setMqttEnabled(Boolean.parseBoolean(props.getProperty("network.mqtt.enabled", "false")));
            model.setMqttSslEnabled(Boolean.parseBoolean(props.getProperty("network.mqtt.sslEnabled", "true")));
            model.setMqttBrokerAddress(props.getProperty("network.mqtt.brokerAddress", "broker.hivemq.com"));
            model.setMqttPort(props.getProperty("network.mqtt.port", "1883"));
            model.setMqttUsername(props.getProperty("network.mqtt.username", ""));
            model.setMqttPassword(props.getProperty("network.mqtt.password", ""));
            model.setMqttBaseTopic(props.getProperty("network.mqtt.baseTopic", "microplotter/data"));

            System.out.println("Configuration loaded from " + file.getAbsolutePath());
            AppLogger.info("Configuration loaded from " + file.getAbsolutePath());

        } catch (IOException | NumberFormatException e) {
        	AppLogger.severe("Error loading configuration file: " + e.getMessage(), e);
        }
    }

    /**
     * @brief Saves configuration settings from the model to a specified file.
     * @param model The ConfigurationModel object containing the settings to save.
     * @param file The File to save the properties to.
     */
	public static void saveConfiguration(ConfigurationModel model, File file) {
        Properties props = new Properties();

        // Plot properties
        props.setProperty("plot.presentation", model.getPlotPresentation());
        props.setProperty("plot.sampleLimit", String.valueOf(model.getDynamicSampleLimit()));
        props.setProperty("plot.updateTime", String.valueOf(model.getUpdateTime()));
        props.setProperty("plot.lineWidth", String.valueOf(model.getLineWidth()));
        props.setProperty("plot.xAxisType", model.getXAxisType());
        props.setProperty("plot.yAxisType", model.getYAxisType());
        props.setProperty("plot.useTagsAsNames", String.valueOf(model.useTagsAsNames()));

        // HTTP properties
        props.setProperty("network.http.enabled", String.valueOf(model.isHttpEnabled()));
        props.setProperty("network.http.url", model.getHttpUrl());
        props.setProperty("network.http.bufferLimit", String.valueOf(model.getNetworkBufferLimit()));

        // MQTT properties
        props.setProperty("network.mqtt.enabled", String.valueOf(model.isMqttEnabled()));
        props.setProperty("network.mqtt.sslEnabled", String.valueOf(model.isMqttSslEnabled()));
        props.setProperty("network.mqtt.brokerAddress", model.getMqttBrokerAddress());
        props.setProperty("network.mqtt.port", model.getMqttPort());
        props.setProperty("network.mqtt.username", model.getMqttUsername());
        props.setProperty("network.mqtt.password", model.getMqttPassword());
        props.setProperty("network.mqtt.baseTopic", model.getMqttBaseTopic());

        try (OutputStream output = new FileOutputStream(file)) {
            props.store(output, "MicroPlotter Configuration");
            System.out.println("Configuration saved to " + file.getAbsolutePath());
            AppLogger.info("Configuration saved to " + file.getAbsolutePath());
        } catch (IOException e) {
        	AppLogger.severe("Error saving configuration file: " + e.getMessage(), e);
        }
    }
}