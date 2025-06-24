package microplotter.model;

/**
 * @brief Holds the application's configuration and run-time state.
 * @details This class acts as a central repository for all user-configurable settings
 * and application state flags, such as connection status and plotting state. This data
 * was previously held as instance variables in the old Layout class. 
 */
public class ConfigurationModel {

    // --- Serial Port State ---
    /** @brief Flag indicating if the application is currently connected to a serial port. */
    private boolean isConnected = false;

    // --- Plotting State ---
    /** @brief Flag indicating if the plotting feature is currently active. */
    private boolean isPlotting = false;
    /** @brief Flag indicating if the active plot is paused. */
    private boolean isPaused = false;
    /** @brief The current plot presentation mode ("Static" or "Dynamic").  */
    private String plotPresentation = "Static";
    /** @brief The number of samples to display in "Dynamic" plot mode.  */
    private int dynamicSampleLimit = 50;
    /** @brief The time interval in seconds for plot updates.  */
    private double updateTime = 3.0;
    /** @brief The width of the lines drawn on the plot.  */
    private int lineWidth = 2;
    /** @brief The type of the X-axis ("Dec" or "Log").  */
    private String xAxisType = "Dec";
    /** @brief The type of the Y-axis ("Dec" or "Log").  */
    private String yAxisType = "Dec";
    
    /** @brief Flag to use tags from serial data as series names in the plot. */
    private boolean useTagsAsNames = false; 
    
    // --- Networking State ---
    /** @brief Flag indicating if HTTP data sending is enabled. */
    private boolean httpEnabled = false;
    /** @brief The URL for the HTTP endpoint. */
    private String httpUrl = "";
    /** @brief The number of data lines to buffer before sending a network packet. */
    private int networkBufferLimit = 10;

    /**
     * @brief Checks if the "Tags as names" feature is enabled.
     * @return True if tags should be used as series names, false otherwise.
     */
    public boolean useTagsAsNames() {
        return useTagsAsNames;
    }
    
    /**
     * @brief Sets the state of the "Tags as names" feature.
     * @param useTagsAsNames The new state.
     */
    public void setUseTagsAsNames(boolean useTagsAsNames) {
        this.useTagsAsNames = useTagsAsNames;
    }

    /**
     * @brief Checks if the serial port is connected.
     * @return True if connected, false otherwise.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * @brief Sets the connection state of the serial port.
     * @param connected The new connection state.
     */
    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }

    /**
     * @brief Checks if plotting is active.
     * @return True if plotting is active, false otherwise.
     */
    public boolean isPlotting() {
        return isPlotting;
    }

    /**
     * @brief Sets the plotting state.
     * @param plotting The new plotting state.
     */
    public void setPlotting(boolean plotting) {
        this.isPlotting = plotting;
    }

    /**
     * @brief Checks if plotting is paused.
     * @return True if plotting is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * @brief Sets the paused state of the plot.
     * @param paused The new paused state.
     */
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
    
    /**
     * @brief Gets the current plot presentation mode.
     * @return A string, either "Static" or "Dynamic".
     */
    public String getPlotPresentation() {
        return plotPresentation;
    }

    /**
     * @brief Sets the plot presentation mode.
     * @param plotPresentation The new presentation mode.
     */
    public void setPlotPresentation(String plotPresentation) {
        this.plotPresentation = plotPresentation;
    }

    /**
     * @brief Gets the sample limit for dynamic plotting.
     * @return The number of samples to display.
     */
    public int getDynamicSampleLimit() {
        return dynamicSampleLimit;
    }

    /**
     * @brief Sets the sample limit for dynamic plotting.
     * @param dynamicSampleLimit The new sample limit.
     */
    public void setDynamicSampleLimit(int dynamicSampleLimit) {
        this.dynamicSampleLimit = dynamicSampleLimit;
    }

    /**
     * @brief Gets the plot update time.
     * @return The update time in seconds.
     */
    public double getUpdateTime() {
        return updateTime;
    }

    /**
     * @brief Sets the plot update time.
     * @param updateTime The new update time in seconds.
     */
    public void setUpdateTime(double updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @brief Gets the plot line width.
     * @return The line width in pixels.
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * @brief Sets the plot line width.
     * @param lineWidth The new line width in pixels.
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
    
    /**
     * @brief Gets the X-axis type.
     * @return A string, either "Dec" or "Log".
     */
    public String getXAxisType() {
        return xAxisType;
    }

    /**
     * @brief Sets the X-axis type.
     * @param xAxisType The new X-axis type.
     */
    public void setXAxisType(String xAxisType) {
        this.xAxisType = xAxisType;
    }

    /**
     * @brief Gets the Y-axis type.
     * @return A string, either "Dec" or "Log".
     */
    public String getYAxisType() {
        return yAxisType;
    }

    /**
     * @brief Sets the Y-axis type.
     * @param yAxisType The new Y-axis type.
     */
    public void setYAxisType(String yAxisType) {
        this.yAxisType = yAxisType;
    }
    
    // Network related stuff
    public boolean isHttpEnabled() { return httpEnabled; }
    public void setHttpEnabled(boolean httpEnabled) { this.httpEnabled = httpEnabled; }

    public String getHttpUrl() { return httpUrl; }
    public void setHttpUrl(String httpUrl) { this.httpUrl = httpUrl; }

    public int getNetworkBufferLimit() { return networkBufferLimit; }
    public void setNetworkBufferLimit(int networkBufferLimit) { this.networkBufferLimit = networkBufferLimit; }
}