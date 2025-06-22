package microplotter.listeners;

public interface PlotUpdateListener {
    void onPlotDataUpdate(double[] values, int seriesCount);
    void onPlotConfigChanged();
}
