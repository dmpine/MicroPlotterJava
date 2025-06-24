package microplotter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import microplotter.model.ConfigurationModel;

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

            // Load plot settings with defaults
            model.setPlotPresentation(props.getProperty("plot.presentation", "Static"));
            model.setDynamicSampleLimit(Integer.parseInt(props.getProperty("plot.sampleLimit", "50")));
            model.setUpdateTime(Double.parseDouble(props.getProperty("plot.updateTime", "3.0")));
            model.setLineWidth(Integer.parseInt(props.getProperty("plot.lineWidth", "2")));
            model.setXAxisType(props.getProperty("plot.xAxisType", "Dec"));
            model.setYAxisType(props.getProperty("plot.yAxisType", "Dec"));
            model.setUseTagsAsNames(Boolean.parseBoolean(props.getProperty("plot.useTagsAsNames", "false")));
            
            // Load networking settings with defaults
            model.setHttpEnabled(Boolean.parseBoolean(props.getProperty("network.http.enabled", "false")));
            model.setHttpUrl(props.getProperty("network.http.url", ""));
            model.setNetworkBufferLimit(Integer.parseInt(props.getProperty("network.http.bufferLimit", "10")));

            System.out.println("Configuration loaded from " + file.getAbsolutePath());

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
        }
    }

    /**
     * @brief Saves configuration settings from the model to a specified file.
     * @param model The ConfigurationModel object containing the settings to save.
     * @param file The File to save the properties to.
     */
    public static void saveConfiguration(ConfigurationModel model, File file) {
        Properties props = new Properties();

        // Set plot properties from the model
        props.setProperty("plot.presentation", model.getPlotPresentation());
        props.setProperty("plot.sampleLimit", String.valueOf(model.getDynamicSampleLimit()));
        props.setProperty("plot.updateTime", String.valueOf(model.getUpdateTime()));
        props.setProperty("plot.lineWidth", String.valueOf(model.getLineWidth()));
        props.setProperty("plot.xAxisType", model.getXAxisType());
        props.setProperty("plot.yAxisType", model.getYAxisType());
        props.setProperty("plot.useTagsAsNames", String.valueOf(model.useTagsAsNames()));

        // Set networking properties from the model
        props.setProperty("network.http.enabled", String.valueOf(model.isHttpEnabled()));
        props.setProperty("network.http.url", model.getHttpUrl());
        props.setProperty("network.http.bufferLimit", String.valueOf(model.getNetworkBufferLimit()));


        try (OutputStream output = new FileOutputStream(file)) {
            props.store(output, "MicroPlotter Configuration");
            System.out.println("Configuration saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving configuration file: " + e.getMessage());
        }
    }
}