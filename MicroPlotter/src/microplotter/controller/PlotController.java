package microplotter.controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Timer;
import microplotter.model.ConfigurationModel;
import microplotter.model.DataProcessor;
import microplotter.model.PlotDataModel;
import microplotter.view.MainWindow;
import microplotter.view.PlotConfigPanel;
import microplotter.view.PlotPanel;

/**
 * @brief Controller for all plotting actions.
 * @details Manages the logic for starting, stopping, and pausing the plot, as well as handling
 * plot configuration changes from the UI. It uses a Timer to update the plot at fixed
 * intervals, processing buffered data to respect the user-defined update time.
 * Implements logic from the original Layout.java and Control.java classes.
 */
public class PlotController {

    /** @brief The model holding the plot data series. */
    private final PlotDataModel plotDataModel;
    /** @brief The model holding the application's configuration and state. */
    private final ConfigurationModel configModel;
    /** @brief The specific UI panel for plot configuration. */
    private final PlotConfigPanel plotConfigPanel;
    /** @brief The specific UI panel where the chart is rendered. */
    private final PlotPanel plotPanel;
    /** @brief The Swing Timer that triggers periodic plot updates. */
    private final Timer plotUpdateTimer;
    /** @brief A thread-safe buffer to hold incoming data strings between plot updates. */
    private final List<String> dataBuffer;

    /**
     * @brief Constructs the PlotController.
     * @details Initializes the controller, injects its model and view dependencies,
     * sets up the data buffer, and configures the plot update timer.
     * @param mainWindow The main application window.
     * @param plotDataModel The data model for the plot.
     * @param configModel The application's configuration model.
     */
    public PlotController(MainWindow mainWindow, PlotDataModel plotDataModel, ConfigurationModel configModel) {
        this.plotDataModel = plotDataModel;
        this.configModel = configModel;
        this.plotConfigPanel = mainWindow.getPlotConfigPanel();
        this.plotPanel = mainWindow.getPlotPanel();

        // Use a thread-safe list for the buffer
        this.dataBuffer = Collections.synchronizedList(new ArrayList<>());

        // Setup a Swing Timer to update the plot periodically
        this.plotUpdateTimer = new Timer(1000, e -> updatePlot());
        this.plotUpdateTimer.setInitialDelay(0);

        initListeners();
    }

    /**
     * @brief Attaches action listeners to the plot configuration UI components.
     * @details Wires up the "Start/Stop", "Pause", and presentation mode combo box
     * to their respective handler methods.
     */
    private void initListeners() {
        plotConfigPanel.getPlotButton().addActionListener(e -> togglePlotting());
        plotConfigPanel.getPauseButton().addActionListener(e -> togglePause());
        plotConfigPanel.getClearPlotButton().addActionListener(e -> clearPlotData());

        // Listener to enable/disable sample limit combo box
        plotConfigPanel.getPlotPresentationComboBox().addActionListener(e -> {
            boolean isDynamic = "Dynamic".equals(plotConfigPanel.getPlotPresentationComboBox().getSelectedItem());
            plotConfigPanel.getDynamicSampleLimitComboBox().setEnabled(isDynamic);
        });
    }

    /**
     * @brief Starts or stops the plotting process.
     * @details Toggles the plotting state, clears data models and buffers,
     * starts/stops the timer, and updates the UI component states accordingly.
     */
    private void togglePlotting() {
        if (configModel.isPlotting()) {
            // --- STOP PLOTTING ---
            configModel.setPlotting(false);
            plotUpdateTimer.stop();
            dataBuffer.clear();
            plotConfigPanel.getPlotButton().setText("Start plotting");
            plotConfigPanel.getPlotButton().setBackground(Color.gray);
            plotConfigPanel.getPauseButton().setEnabled(false);
            setPlotConfigEnabled(true);

        } else {
            // --- START PLOTTING ---
            configModel.setPlotting(true);
            plotDataModel.clearData(); // This now also resets series names
            plotPanel.clearPlot();
            dataBuffer.clear();
            
            updateConfigFromUI();
            plotUpdateTimer.setDelay((int) (configModel.getUpdateTime() * 1000));
            plotUpdateTimer.start();
            
            plotConfigPanel.getPlotButton().setText("Stop plotting");
            plotConfigPanel.getPlotButton().setBackground(Color.red);
            plotConfigPanel.getPauseButton().setEnabled(true);
            setPlotConfigEnabled(false);
        }
    }

    /**
     * @brief Pauses or resumes the plot updates.
     * @details Toggles the paused state and correspondingly starts or stops the plot update timer.
     */
    private void togglePause() {
        if (configModel.isPaused()) {
            configModel.setPaused(false);
            plotUpdateTimer.start();
            plotConfigPanel.getPauseButton().setText("Pause plotting");
        } else {
            configModel.setPaused(true);
            plotUpdateTimer.stop();
            plotConfigPanel.getPauseButton().setText("Resume plotting");
        }
    }

    /**
     * @brief Buffers a line of data received from the SerialController.
     * @details This method is called by the SerialController for each line of data. It simply
     * adds the data to a temporary buffer to await processing by the timer.
     * @param dataLine A line of text received from the serial port.
     */
    public void processData(String dataLine) {
        if (!configModel.isPlotting() || configModel.isPaused()) {
            return;
        }
        dataBuffer.add(dataLine);
    }

    /**
     * @brief Processes buffered data and redraws the chart.
     * @details This method is called by the plot update timer. It processes all data
     * accumulated in the buffer, updates the plot data model, applies dynamic limits,
     * and finally calls the PlotPanel's update method to render the changes.
     */
    private void updatePlot() {
        // Process all buffered data at once
        synchronized (dataBuffer) {
            if (dataBuffer.isEmpty()) {
                return;
            }
            for (String line : dataBuffer) {
            	DataProcessor.ParsedData parsedData = DataProcessor.parseSerialData(line, configModel.useTagsAsNames());
            	if (parsedData.columnCount > 0) {
                    for (int i = 0; i < parsedData.columnCount; i++) {
                        // If a tag was found, update the series name
                        if (parsedData.tags[i] != null) {
                            plotDataModel.setSeriesName(i, parsedData.tags[i]);
                        }
                        plotDataModel.addDataPoint(i, parsedData.values[i]);
                    }
                    plotDataModel.incrementXCounter();
                }
            }
            dataBuffer.clear();
        }

        updateConfigFromUI();

        if ("Dynamic".equals(configModel.getPlotPresentation())) {
            plotDataModel.limitSeriesSize(configModel.getDynamicSampleLimit());
        }

        int activeSeriesCount = plotDataModel.getActiveSeriesCount();
        plotPanel.updatePlot(
            plotDataModel.getDataset(activeSeriesCount),
            configModel.getLineWidth(),
            configModel.getXAxisType(),
            configModel.getYAxisType()
        );
    }

    /**
     * @brief Reads all settings from the UI and stores them in the ConfigurationModel.
     * @details This ensures the plotting logic uses the most up-to-date configuration
     * values selected by the user.
     */
    private void updateConfigFromUI() {
        configModel.setPlotPresentation((String) plotConfigPanel.getPlotPresentationComboBox().getSelectedItem());
        configModel.setDynamicSampleLimit((Integer) plotConfigPanel.getDynamicSampleLimitComboBox().getSelectedItem());
        configModel.setLineWidth((Integer) plotConfigPanel.getLineWidthComboBox().getSelectedItem());
        configModel.setXAxisType((String) plotConfigPanel.getXAxisTypeComboBox().getSelectedItem());
        configModel.setYAxisType((String) plotConfigPanel.getYAxisTypeComboBox().getSelectedItem());

        String timeStr = (String) plotConfigPanel.getUpdateTimeComboBox().getSelectedItem();
        configModel.setUpdateTime(Double.parseDouble(timeStr.replace("s", "")));
        
        configModel.setUseTagsAsNames(plotConfigPanel.getTagsAsNamesCheckBox().isSelected());
    }

    /**
     * @brief Enables or disables the plot configuration controls on the UI.
     * @details This is used to prevent the user from changing plot settings while plotting is active.
     * @param enabled True to enable the controls, false to disable them.
     */
    private void setPlotConfigEnabled(boolean enabled) {
        plotConfigPanel.getLineWidthComboBox().setEnabled(enabled);
        plotConfigPanel.getPlotPresentationComboBox().setEnabled(enabled);
        plotConfigPanel.getUpdateTimeComboBox().setEnabled(enabled);
        plotConfigPanel.getXAxisTypeComboBox().setEnabled(enabled);
        plotConfigPanel.getYAxisTypeComboBox().setEnabled(enabled);
        
        plotConfigPanel.getTagsAsNamesCheckBox().setEnabled(enabled);

        if (enabled && "Dynamic".equals(plotConfigPanel.getPlotPresentationComboBox().getSelectedItem())) {
             plotConfigPanel.getDynamicSampleLimitComboBox().setEnabled(true);
        } else {
             plotConfigPanel.getDynamicSampleLimitComboBox().setEnabled(false);
        }
    }
    
    /**
     * @brief Clears all data from the plot and the underlying data model.
     */
    private void clearPlotData() {
        plotDataModel.clearData(); // Clears the data and resets names
        plotPanel.clearPlot();     // Clears the visual chart
    }
}