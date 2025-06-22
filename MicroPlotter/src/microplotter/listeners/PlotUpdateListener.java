package microplotter.listeners;

/**
 * @brief Defines a listener contract for plot-related events.
 * @details This interface can be implemented by any class that needs to be notified
 * when plot data is updated or when plot configurations are changed by the user.
 */
public interface PlotUpdateListener {
    /**
     * @brief Called when new data is ready to be added to the plot.
     * @param values An array of double values representing the new data points for each series.
     * @param seriesCount The number of active series (columns) in the current data update.
     */
    void onPlotDataUpdate(double[] values, int seriesCount);

    /**
     * @brief Called when a setting in the plot configuration panel has been changed.
     */
    void onPlotConfigChanged();
}