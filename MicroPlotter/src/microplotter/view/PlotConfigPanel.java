package microplotter.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import microplotter.utils.Constants;

/**
 * @brief A JPanel containing UI components for plot configuration.
 * @details This class encapsulates all the user controls for customizing the plot,
 * such as line width, presentation mode, update time, and axis types. Its code
 * is extracted from the original Layout.create_plt_conf_elements method.
 */
public class PlotConfigPanel extends JPanel {

    /** @brief Combo box for selecting the plot line width. */
    private JComboBox<Integer> lineWidthComboBox;
    /** @brief Combo box for selecting the plot presentation mode (Dynamic/Static). */
    private JComboBox<String> plotPresentationComboBox;
    /** @brief Combo box for selecting the sample limit in Dynamic mode. */
    private JComboBox<Integer> dynamicSampleLimitComboBox;
    /** @brief Combo box for selecting the plot's visual update interval. */
    private JComboBox<String> updateTimeComboBox;
    /** @brief Combo box for selecting the X-axis type (Decimal/Logarithmic). */
    private JComboBox<String> xAxisTypeComboBox;
    /** @brief Combo box for selecting the Y-axis type (Decimal/Logarithmic). */
    private JComboBox<String> yAxisTypeComboBox;
    /** @brief The button to start or stop the plotting process. */
    private JButton plotButton;
    /** @brief The button to pause or resume the plotting process. */
    private JButton pauseButton;

    /**
     * @brief Constructs the PlotConfigPanel.
     * @details Sets up the panel layout and creates and arranges all the UI controls
     * for plot configuration into two vertically stacked sub-panels.
     * @param width The initial width of the panel.
     */
    public PlotConfigPanel(int width) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Plot configuration"));
        setPreferredSize(new Dimension(width - 2, 90));

        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel subPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // --- Sub Panel 1 ---
        subPanel1.add(new JLabel("Line width"));
        lineWidthComboBox = new JComboBox<>(Constants.LINE_WIDTHS);
        lineWidthComboBox.setSelectedItem(Constants.DEFAULT_LINE_WIDTH);
        subPanel1.add(lineWidthComboBox);

        subPanel1.add(new JLabel("Presentation"));
        plotPresentationComboBox = new JComboBox<>(Constants.PLOT_PRESENTATIONS);
        plotPresentationComboBox.setSelectedItem("Static");
        subPanel1.add(plotPresentationComboBox);

        subPanel1.add(new JLabel("Sample limit"));
        dynamicSampleLimitComboBox = new JComboBox<>(Constants.SAMPLE_LIMITS);
        dynamicSampleLimitComboBox.setPreferredSize(new Dimension(100, 25));
        dynamicSampleLimitComboBox.setEnabled(false);
        dynamicSampleLimitComboBox.setSelectedItem(50);
        subPanel1.add(dynamicSampleLimitComboBox);
        
        subPanel1.add(new JLabel("Update time"));
        updateTimeComboBox = new JComboBox<>(Constants.UPDATE_TIMES);
        updateTimeComboBox.setSelectedItem("3s");
        subPanel1.add(updateTimeComboBox);

        // --- Sub Panel 2 ---
        subPanel2.add(new JLabel("X Axis type"));
        xAxisTypeComboBox = new JComboBox<>(new String[]{"Dec", "Log"});
        subPanel2.add(xAxisTypeComboBox);

        subPanel2.add(new JLabel("Y Axis type"));
        yAxisTypeComboBox = new JComboBox<>(new String[]{"Dec", "Log"});
        subPanel2.add(yAxisTypeComboBox);

        plotButton = new JButton("Start plotting");
        plotButton.setPreferredSize(new Dimension(125, 25));
        plotButton.setEnabled(false);
        subPanel2.add(plotButton);

        pauseButton = new JButton("Pause plotting");
        pauseButton.setPreferredSize(new Dimension(140, 25));
        pauseButton.setEnabled(false);
        subPanel2.add(pauseButton);
        
        add(subPanel1);
        add(subPanel2);
    }

    /** @brief Gets the line width combo box. @return The line width JComboBox. */
    public JComboBox<Integer> getLineWidthComboBox() { return lineWidthComboBox; }
    /** @brief Gets the plot presentation combo box. @return The plot presentation JComboBox. */
    public JComboBox<String> getPlotPresentationComboBox() { return plotPresentationComboBox; }
    /** @brief Gets the dynamic sample limit combo box. @return The dynamic sample limit JComboBox. */
    public JComboBox<Integer> getDynamicSampleLimitComboBox() { return dynamicSampleLimitComboBox; }
    /** @brief Gets the update time combo box. @return The update time JComboBox. */
    public JComboBox<String> getUpdateTimeComboBox() { return updateTimeComboBox; }
    /** @brief Gets the X-axis type combo box. @return The X-axis type JComboBox. */
    public JComboBox<String> getXAxisTypeComboBox() { return xAxisTypeComboBox; }
    /** @brief Gets the Y-axis type combo box. @return The Y-axis type JComboBox. */
    public JComboBox<String> getYAxisTypeComboBox() { return yAxisTypeComboBox; }
    /** @brief Gets the plot button. @return The plot JButton. */
    public JButton getPlotButton() { return plotButton; }
    /** @brief Gets the pause button. @return The pause JButton. */
    public JButton getPauseButton() { return pauseButton; }
}