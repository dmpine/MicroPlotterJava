package microplotter.model;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Manages the data for plotting.
 * @details This class holds the data series for the chart, handles adding new data points,
 * and provides methods to manipulate and retrieve the dataset for display. It encapsulates
 * the JFreeChart data structures.
 */
public class PlotDataModel {
    /** @brief A list to hold all potential XYSeries objects for the plot. */
    private final List<XYSeries> dataSeries;
    /** @brief The collection of series that is actually passed to the chart for rendering. */
    private final XYSeriesCollection dataset;
    /** @brief A counter for the X-axis value, representing discrete time steps or samples. */
    private int xCounter = 0;
    /** @brief The maximum number of data series supported by the model. */
    private final int maxSeries = 10;
    
    /**
     * @brief Constructs a new PlotDataModel.
     * @details Initializes the data structures and pre-allocates the series objects.
     */
    public PlotDataModel() {
        this.dataSeries = new ArrayList<>();
        this.dataset = new XYSeriesCollection();
        initializeSeries();
    }
    
    /**
     * @brief Initializes the fixed number of XYSeries objects.
     * @details Creates and adds the maximum number of series to the internal list,
     * each with a default name like "D0", "D1", etc.
     */
    private void initializeSeries() {
        for (int i = 0; i < maxSeries; i++) {
            XYSeries series = new XYSeries("D" + i);
            dataSeries.add(series);
        }
    }
    
    /**
     * @brief Adds a new data point to a specific series.
     * @param seriesIndex The index of the series to which the data point should be added.
     * @param value The Y-value of the data point. The current xCounter is used for the X-value.
     */
    public void addDataPoint(int seriesIndex, double value) {
        if (seriesIndex >= 0 && seriesIndex < dataSeries.size()) {
            dataSeries.get(seriesIndex).add(xCounter, value);
        }
    }
    
    /**
     * @brief Increments the global X-axis counter by one.
     */
    public void incrementXCounter() {
        xCounter++;
    }
    
    /**
     * @brief Limits the number of items in each series for dynamic plotting.
     * @details For each series, it removes the oldest data points (from the beginning)
     * until the item count is equal to or less than the specified maxSize.
     * @param maxSize The maximum number of data points to retain in each series.
     */
    public void limitSeriesSize(int maxSize) {
        for (XYSeries series : dataSeries) {
            while (series.getItemCount() > maxSize) {
                series.remove(0);
            }
        }
    }
    
    /**
     * @brief Gets the collection of currently active data series for rendering.
     * @param activeSeriesCount The number of series that should be included in the dataset.
     * @return An XYSeriesCollection containing only the active series with data.
     */
    public XYSeriesCollection getDataset(int activeSeriesCount) {
        dataset.removeAllSeries();
        for (int i = 0; i < Math.min(activeSeriesCount, dataSeries.size()); i++) {
            if (dataSeries.get(i).getItemCount() > 0) {
                dataset.addSeries(dataSeries.get(i));
            }
        }
        return dataset;
    }
    
    /**
     * @brief Counts how many series currently contain one or more data points.
     * @return The number of active (non-empty) series.
     */
    public int getActiveSeriesCount() {
        int count = 0;
        for (XYSeries series : dataSeries) {
            if (series.getItemCount() > 0) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * @brief Clears all data from all series and resets the X-counter.
     * @details This is used to prepare the model for a new plotting session.
     */
    public void clearData() {
        for (XYSeries series : dataSeries) {
            series.clear();
        }
        xCounter = 0;
    }
}