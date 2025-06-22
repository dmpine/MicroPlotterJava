package microplotter.model;

import microplotter.utils.Constants;

/**
 * Holds the application's configuration and state.
 * This data was previously held as instance variables in the Layout class.
 */
public class ConfigurationModel {

    // Serial Port State
    private boolean isConnected = false;

    // Plotting State
    private boolean isPlotting = false;
    private boolean isPaused = false; 
    private String plotPresentation = "Static"; 
    private int dynamicSampleLimit = 50; 
    private double updateTime = 3.0; 
    private int lineWidth = 2; 
    private String xAxisType = "Dec"; 
    private String yAxisType = "Dec"; 

    // Terminal and Recording State
    private boolean isFileRecording = false;
    private String recordingFileName = "";  
    private boolean addCR = false; 
    private boolean addNL = false; 
    private boolean showTimestamp = true; 
    private boolean autoScroll = true;

    // --- Getters and Setters ---

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isPlotting() {
        return isPlotting;
    }

    public void setPlotting(boolean plotting) {
        isPlotting = plotting;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
    
    public String getPlotPresentation() {
        return plotPresentation;
    }

    public void setPlotPresentation(String plotPresentation) {
        this.plotPresentation = plotPresentation;
    }

    public int getDynamicSampleLimit() {
        return dynamicSampleLimit;
    }

    public void setDynamicSampleLimit(int dynamicSampleLimit) {
        this.dynamicSampleLimit = dynamicSampleLimit;
    }

    public double getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(double updateTime) {
        this.updateTime = updateTime;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
    
    public String getXAxisType() {
        return xAxisType;
    }

    public void setXAxisType(String xAxisType) {
        this.xAxisType = xAxisType;
    }

    public String getYAxisType() {
        return yAxisType;
    }

    public void setYAxisType(String yAxisType) {
        this.yAxisType = yAxisType;
    }
}