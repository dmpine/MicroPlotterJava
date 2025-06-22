package microplotter.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Panel that contains the JFreeChart plot.
 * Logic is adapted from Layout.create_plt_elements and Control.create_multiple_plot.
 */
public class PlotPanel extends JPanel {

    private final ChartPanel chartPanel;
    private final JFreeChart chart;
    private final XYLineAndShapeRenderer renderer;

    public PlotPanel(int width, int height) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Plot"));
        setPreferredSize(new Dimension(width - 2, height));

        chart = ChartFactory.createXYLineChart(null, null, null, null);
        chartPanel = new ChartPanel(chart);
        
        renderer = new XYLineAndShapeRenderer(true, false);
        chart.getXYPlot().setRenderer(renderer);
        
        chartPanel.setPreferredSize(new Dimension(width - 10, height - 40));
        chartPanel.setEnabled(false);
        
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Updates the chart with a new dataset and applies appearance settings.
     * @param dataset The collection of series to display.
     * @param lineWidth The thickness of the plot lines.
     * @param xAxisType The type of X-axis ("Dec" or "Log").
     * @param yAxisType The type of Y-axis ("Dec" or "Log").
     */
    public void updatePlot(XYSeriesCollection dataset, int lineWidth, String xAxisType, String yAxisType) {
        if (!chartPanel.isEnabled()) {
            chartPanel.setEnabled(true);
        }

        XYPlot plot = chart.getXYPlot();
        plot.setDataset(dataset);

        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(lineWidth));
        }

        if ("Log".equals(xAxisType)) {
            plot.setDomainAxis(new LogarithmicAxis("X (Log)"));
        } else {
            plot.setDomainAxis(new NumberAxis("X (Dec)"));
        }

        if ("Log".equals(yAxisType)) {
            plot.setRangeAxis(new LogarithmicAxis("Y (Log)"));
        } else {
            plot.setRangeAxis(new NumberAxis("Y (Dec)"));
        }
        
        // --- KEY CHANGE: Manually set the X-axis range for the "sliding window" effect ---
        if (dataset.getSeriesCount() > 0 && dataset.getSeries(0).getItemCount() > 0) {
            // Get the min and max X from the first series in the dataset
            double minX = dataset.getSeries(0).getMinX();
            double maxX = dataset.getSeries(0).getMaxX();
            plot.getDomainAxis().setRange(minX, maxX); // Set the exact range
        } else {
            plot.getDomainAxis().setAutoRange(true); // Fallback for empty plot
        }

        // The Y-axis can still auto-range to fit the visible data
        plot.getRangeAxis().setAutoRange(true);
    }
     
    public void clearPlot() {
        chart.getXYPlot().setDataset(null);
        chartPanel.setEnabled(false);
    }
}