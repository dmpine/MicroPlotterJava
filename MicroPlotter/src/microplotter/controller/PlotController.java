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
 * Controller for all plotting actions. Manages plot configuration and data updates.
 * Implements logic from Layout.java, Control.java, and the Timer concept.
 */
public class PlotController {

    private final MainWindow mainWindow;
    private final PlotDataModel plotDataModel;
    private final ConfigurationModel configModel;
    private final PlotConfigPanel plotConfigPanel;
    private final PlotPanel plotPanel;
    private final Timer plotUpdateTimer;
    
    // --- KEY CHANGE: Buffer to hold incoming data between updates ---
    private final List<String> dataBuffer;

    public PlotController(MainWindow mainWindow, PlotDataModel plotDataModel, ConfigurationModel configModel) {
        this.mainWindow = mainWindow;
        this.plotDataModel = plotDataModel;
        this.configModel = configModel;
        this.plotConfigPanel = mainWindow.getPlotConfigPanel();
        this.plotPanel = mainWindow.getPlotPanel();
        
        // Use a thread-safe list for the buffer
        this.dataBuffer = Collections.synchronizedList(new ArrayList<>());

        // Setup a Swing Timer to update the plot periodically
        this.plotUpdateTimer = new Timer(1000, e -> updatePlot()); // Default 1s, will be updated
        this.plotUpdateTimer.setInitialDelay(0);

        initListeners();
    }

    private void initListeners() {
        plotConfigPanel.getPlotButton().addActionListener(e -> togglePlotting());
        plotConfigPanel.getPauseButton().addActionListener(e -> togglePause());

        // Listener to enable/disable sample limit combo box
        plotConfigPanel.getPlotPresentationComboBox().addActionListener(e -> {
            boolean isDynamic = "Dynamic".equals(plotConfigPanel.getPlotPresentationComboBox().getSelectedItem());
            plotConfigPanel.getDynamicSampleLimitComboBox().setEnabled(isDynamic);
        });
    }

    private void togglePlotting() {
        if (configModel.isPlotting()) {
            // --- STOP PLOTTING ---
            configModel.setPlotting(false);
            plotUpdateTimer.stop();
            dataBuffer.clear(); // Clear buffer on stop
            plotConfigPanel.getPlotButton().setText("Start plotting");
            plotConfigPanel.getPlotButton().setBackground(Color.gray);
            plotConfigPanel.getPauseButton().setEnabled(false);
            setPlotConfigEnabled(true);

        } else {
            // --- START PLOTTING ---
            configModel.setPlotting(true);
            plotDataModel.clearData();
            plotPanel.clearPlot();
            dataBuffer.clear(); // Clear buffer on start
            
            updateConfigFromUI(); // Capture current settings
            plotUpdateTimer.setDelay((int) (configModel.getUpdateTime() * 1000));
            plotUpdateTimer.start();
            
            plotConfigPanel.getPlotButton().setText("Stop plotting");
            plotConfigPanel.getPlotButton().setBackground(Color.red);
            plotConfigPanel.getPauseButton().setEnabled(true);
            setPlotConfigEnabled(false);
        }
    }

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
     * Called by SerialController. Now only adds data to a buffer.
     * @param dataLine A line of text received from the serial port.
     */
    public void processData(String dataLine) {
        if (!configModel.isPlotting() || configModel.isPaused()) {
            return;
        }
        // --- KEY CHANGE: Just add data to the buffer, don't process it yet ---
        dataBuffer.add(dataLine);
    }
    
    /**
     * This method is called by the timer to refresh the chart display.
     */
    private void updatePlot() {
        // --- KEY CHANGE: Process all buffered data at once ---
        synchronized (dataBuffer) {
            if (dataBuffer.isEmpty()) {
                return; // Nothing to do
            }
            for (String line : dataBuffer) {
                DataProcessor.ParsedData parsedData = DataProcessor.parseSerialData(line);
                if (parsedData.columnCount > 0) {
                    for (int i = 0; i < parsedData.columnCount; i++) {
                        plotDataModel.addDataPoint(i, parsedData.values[i]);
                    }
                    plotDataModel.incrementXCounter();
                }
            }
            dataBuffer.clear(); // Clear the buffer after processing
        }

        updateConfigFromUI(); // Get latest settings before redrawing

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
    
    /** Reads all settings from the UI and stores them in the ConfigurationModel. */
    private void updateConfigFromUI() {
        configModel.setPlotPresentation((String) plotConfigPanel.getPlotPresentationComboBox().getSelectedItem());
        configModel.setDynamicSampleLimit((Integer) plotConfigPanel.getDynamicSampleLimitComboBox().getSelectedItem());
        configModel.setLineWidth((Integer) plotConfigPanel.getLineWidthComboBox().getSelectedItem());
        configModel.setXAxisType((String) plotConfigPanel.getXAxisTypeComboBox().getSelectedItem());
        configModel.setYAxisType((String) plotConfigPanel.getYAxisTypeComboBox().getSelectedItem());
        
        String timeStr = (String) plotConfigPanel.getUpdateTimeComboBox().getSelectedItem();
        configModel.setUpdateTime(Double.parseDouble(timeStr.replace("s", "")));
    }
    
    /** Enables or disables the plot configuration controls. */
    private void setPlotConfigEnabled(boolean enabled) {
        plotConfigPanel.getLineWidthComboBox().setEnabled(enabled);
        plotConfigPanel.getPlotPresentationComboBox().setEnabled(enabled);
        plotConfigPanel.getUpdateTimeComboBox().setEnabled(enabled);
        plotConfigPanel.getXAxisTypeComboBox().setEnabled(enabled);
        plotConfigPanel.getYAxisTypeComboBox().setEnabled(enabled);
        
        if (enabled && "Dynamic".equals(plotConfigPanel.getPlotPresentationComboBox().getSelectedItem())) {
             plotConfigPanel.getDynamicSampleLimitComboBox().setEnabled(true);
        } else {
             plotConfigPanel.getDynamicSampleLimitComboBox().setEnabled(false);
        }
    }
}