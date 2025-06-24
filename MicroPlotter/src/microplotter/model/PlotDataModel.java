package microplotter.model;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @brief Manages the data for plotting using a dynamic tag-based system.
 * @details This class holds the data series for the chart. It uses a map to
 * dynamically assign data to a specific series based on a string tag, ensuring
 * that data for the same tag always goes to the same series.
 */
public class PlotDataModel {
    /** @brief The collection of series that is actually passed to the chart for rendering. */
    private final XYSeriesCollection dataset;
    /** @brief The maximum number of concurrent data series supported by the model. */
    private final int maxSeries = 10;
    
    /** @brief A pool of reusable XYSeries objects. */
    private final List<XYSeries> seriesPool;
    /** @brief A map that links a string tag (e.g., "SINE") to a specific XYSeries object from the pool. */
    private final Map<String, XYSeries> activeTaggedSeries;

    /**
     * @brief Constructs a new PlotDataModel.
     * @details Initializes the data structures, including the series pool and the map for active series.
     */
    public PlotDataModel() {
        this.dataset = new XYSeriesCollection();
        this.seriesPool = new ArrayList<>();
        this.activeTaggedSeries = new LinkedHashMap<>(); // Maintains insertion order for consistent legend
        initializeSeriesPool();
    }
    
    /**
     * @brief Initializes the fixed number of XYSeries objects in the pool.
     * @details Creates and adds the maximum number of series, each configured to allow
     * duplicate X-values to prevent crashes from rapid data arrival.
     */
    private void initializeSeriesPool() {
        for (int i = 0; i < maxSeries; i++) {
            // The last 'true' argument allows duplicate X-values, increasing stability.
            seriesPool.add(new XYSeries("D" + i, true, true));
        }
    }
    
    /**
     * @brief Adds a data point (x,y) to a series identified by a tag.
     * @details If the tag is encountered for the first time, it assigns the next available
     * series from the pool to that tag. It then adds the new data point to the correct series.
     * @param tag The string tag for the data series (e.g., "SINE").
     * @param x The X-value of the data point.
     * @param y The Y-value of the data point.
     */
    public void addDataPoint(String tag, double x, double y) {
        XYSeries series = activeTaggedSeries.get(tag);

        // If this is a new tag and we have a series available in the pool...
        if (series == null && activeTaggedSeries.size() < maxSeries) {
            series = seriesPool.get(activeTaggedSeries.size());
            series.setKey(tag); // Assign the new tag as its name for the legend
            activeTaggedSeries.put(tag, series); // Add to our map of active series
        }
        
        // If the series exists (either it was found or just assigned), add the data point.
        if (series != null) {
            series.add(x, y);
        }
    }
    
    /**
     * @brief Limits the number of items in each active series for dynamic plotting.
     * @details For each active series, it removes the oldest data points until the item
     * count is equal to or less than the specified maxSize.
     * @param maxSize The maximum number of data points to retain in each series.
     */
    public void limitSeriesSize(int maxSize) {
        for (XYSeries series : activeTaggedSeries.values()) {
            while (series.getItemCount() > maxSize) {
                series.remove(0);
            }
        }
    }
    
    /**
     * @brief Gets the collection of currently active data series for rendering.
     * @return An XYSeriesCollection containing only the series that have received data.
     */
    public XYSeriesCollection getDataset() {
        dataset.removeAllSeries();
        for (XYSeries series : activeTaggedSeries.values()) {
            dataset.addSeries(series);
        }
        return dataset;
    }
    
    /**
     * @brief Clears all data and resets the model to its initial state.
     * @details This clears the map of active series, removes all series from the dataset,
     * and resets each series in the pool to be empty and have its default name (e.g., "D0").
     */
    public void clearData() {
        activeTaggedSeries.clear();
        dataset.removeAllSeries();
        
        for (int i = 0; i < maxSeries; i++) {
            seriesPool.get(i).clear();
            seriesPool.get(i).setKey("D" + i);
        }
    }
}