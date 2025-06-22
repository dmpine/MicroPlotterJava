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
 * Panel for plot configuration UI components.
 * This code is extracted from Layout.create_plt_conf_elements.
 */
public class PlotConfigPanel extends JPanel {

    private JComboBox<Integer> lineWidthComboBox;
    private JComboBox<String> plotPresentationComboBox;
    private JComboBox<Integer> dynamicSampleLimitComboBox;
    private JComboBox<String> updateTimeComboBox;
    private JComboBox<String> xAxisTypeComboBox;
    private JComboBox<String> yAxisTypeComboBox;
    private JButton plotButton;
    private JButton pauseButton;

    public PlotConfigPanel(int width) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Plot configuration"));
        setPreferredSize(new Dimension(width - 2, 90));

        JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel subPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // --- Sub Panel 1 ---
        subPanel1.add(new JLabel("Line width"));
        lineWidthComboBox = new JComboBox<>(Constants.LINE_WIDTHS);
        lineWidthComboBox.setSelectedItem(Constants.DEFAULT_LINE_WIDTH); // 
        subPanel1.add(lineWidthComboBox);

        subPanel1.add(new JLabel("Presentation"));
        plotPresentationComboBox = new JComboBox<>(Constants.PLOT_PRESENTATIONS); // 
        plotPresentationComboBox.setSelectedItem("Static");
        subPanel1.add(plotPresentationComboBox);

        subPanel1.add(new JLabel("Sample limit"));
        dynamicSampleLimitComboBox = new JComboBox<>(Constants.SAMPLE_LIMITS); // 
        dynamicSampleLimitComboBox.setPreferredSize(new Dimension(100, 25));
        dynamicSampleLimitComboBox.setEnabled(false);
        dynamicSampleLimitComboBox.setSelectedItem(50);
        subPanel1.add(dynamicSampleLimitComboBox);
        
        subPanel1.add(new JLabel("Update time"));
        updateTimeComboBox = new JComboBox<>(Constants.UPDATE_TIMES); // 
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

    // --- Add Getters for all components so controllers can access them ---
    public JComboBox<Integer> getLineWidthComboBox() { return lineWidthComboBox; }
    public JComboBox<String> getPlotPresentationComboBox() { return plotPresentationComboBox; }
    public JComboBox<Integer> getDynamicSampleLimitComboBox() { return dynamicSampleLimitComboBox; }
    public JComboBox<String> getUpdateTimeComboBox() { return updateTimeComboBox; }
    public JComboBox<String> getXAxisTypeComboBox() { return xAxisTypeComboBox; }
    public JComboBox<String> getYAxisTypeComboBox() { return yAxisTypeComboBox; }
    public JButton getPlotButton() { return plotButton; }
    public JButton getPauseButton() { return pauseButton; }
}