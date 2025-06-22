package microplotter.model;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.ArrayList;
import java.util.List;

public class PlotDataModel {
    private final List<XYSeries> dataSeries;
    private final XYSeriesCollection dataset;
    private int xCounter = 0;
    private final int maxSeries = 10;
    
    public PlotDataModel() {
        this.dataSeries = new ArrayList<>();
        this.dataset = new XYSeriesCollection();
        initializeSeries();
    }
    
    private void initializeSeries() {
        for (int i = 0; i < maxSeries; i++) {
            XYSeries series = new XYSeries("D" + i);
            dataSeries.add(series);
        }
    }
    
    public void addDataPoint(int seriesIndex, double value) {
        if (seriesIndex >= 0 && seriesIndex < dataSeries.size()) {
            dataSeries.get(seriesIndex).add(xCounter, value);
        }
    }
    
    public void incrementXCounter() {
        xCounter++;
    }
    
    public void limitSeriesSize(int maxSize) {
        for (XYSeries series : dataSeries) {
            while (series.getItemCount() > maxSize) {
                series.remove(0);
            }
        }
    }
    
    public XYSeriesCollection getDataset(int activeSeriesCount) {
        dataset.removeAllSeries();
        for (int i = 0; i < Math.min(activeSeriesCount, dataSeries.size()); i++) {
            if (dataSeries.get(i).getItemCount() > 0) {
                dataset.addSeries(dataSeries.get(i));
            }
        }
        return dataset;
    }
    
    public int getActiveSeriesCount() {
        int count = 0;
        for (XYSeries series : dataSeries) {
            if (series.getItemCount() > 0) {
                count++;
            }
        }
        return count;
    }
    
    public void clearData() {
        dataSeries.forEach(XYSeries::clear);
        xCounter = 0;
    }
}
