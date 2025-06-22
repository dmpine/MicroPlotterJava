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
 * @brief A JPanel that encapsulates and manages a JFreeChart ChartPanel.
 * @details This class is responsible for displaying the data plot. It contains methods
 * to update the plot with new data and to configure its visual appearance, such as
 * line thickness and axis types. Its logic is adapted from the original Layout and Control classes.
 */
@SuppressWarnings("serial")
public class PlotPanel extends JPanel {

    /** @brief The JFreeChart panel that hosts the chart itself. */
    private final ChartPanel chartPanel;
    /** @brief The main JFreeChart object that controls the plot's properties. */
    private final JFreeChart chart;
    /** @brief The renderer used to customize the appearance of the plot lines. */
    private final XYLineAndShapeRenderer renderer;

    /**
     * @brief Constructs the PlotPanel.
     * @details Sets up the panel layout and initializes an empty JFreeChart instance,
     * preparing it for future data.
     * @param width The initial width of the panel.
     * @param height The initial height of the panel.
     */
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
     * @brief Updates the chart with a new dataset and applies appearance settings.
     * @details This is the main rendering method. It takes a dataset and configuration options,
     * then re-configures and repaints the chart to reflect the new state. It handles
     * setting line thickness, axis types (logarithmic or decimal), and axis ranges.
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

        // --- FIX: Configure LogarithmicAxis correctly ---
        if ("Log".equals(xAxisType)) {
            LogarithmicAxis logDomainAxis = new LogarithmicAxis("X (Log)");
            logDomainAxis.setAllowNegativesFlag(true); // This prevents the crash for x=0
            plot.setDomainAxis(logDomainAxis);
        } else {
            plot.setDomainAxis(new NumberAxis("X (Dec)"));
        }

        // --- FIX: Configure LogarithmicAxis correctly ---
        if ("Log".equals(yAxisType)) {
            LogarithmicAxis logRangeAxis = new LogarithmicAxis("Y (Log)");
            logRangeAxis.setAllowNegativesFlag(true); // This prevents the crash for y<=0
            plot.setRangeAxis(logRangeAxis);
        } else {
            plot.setRangeAxis(new NumberAxis("Y (Dec)"));
        }
        
        if (dataset.getSeriesCount() > 0 && dataset.getSeries(0).getItemCount() > 0) {
            double minX = dataset.getSeries(0).getMinX();
            double maxX = dataset.getSeries(0).getMaxX();
            plot.getDomainAxis().setRange(minX, maxX);
        } else {
            plot.getDomainAxis().setAutoRange(true);
        }

        plot.getRangeAxis().setAutoRange(true);
    }
     
    /**
     * @brief Clears all data from the plot and disables it.
     * @details Resets the chart to its initial empty state.
     */
    public void clearPlot() {
        chart.getXYPlot().setDataset(null);
        chartPanel.setEnabled(false);
    }
}