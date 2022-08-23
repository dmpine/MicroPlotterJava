/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package microplotterjava;

// JFreeChart imports
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;

import javax.swing.*;

// Serial port imports
import com.fazecast.jSerialComm.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.InputStream;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author danielpineda
 */
public class Control {
    
    public Control() {
        // Buu
    }

    public double time_counter(long startTime) {
        long endTime = System.currentTimeMillis();
        double secs = (endTime - startTime) / 1000F;

        return secs;
    }

    // Method to modify the form on pause action
    public void pause_form_actions(JButton btn_pause) {
        if ("Pause plotting".equals(btn_pause.getText())) {
            btn_pause.setText("Resume plotting");
        } else if ("Resume plotting".equals(btn_pause.getText())) {
            btn_pause.setText("Pause plotting");
        }
    }

    // Method to modify the form on start plotting
    public void plotting_form_actions(JComboBox cmb_lineWidth, JComboBox cmb_plotPresentation,
            JComboBox cmb_dynamicSampleLimit, JComboBox cmb_refreshRate, JComboBox cmb_xAxisType,
            JComboBox cmb_yAxisType, JButton btn_plot, JButton btn_pause) {

        if ("Start plotting".equals(btn_plot.getText())) {
            btn_plot.setText("Stop plotting");
            btn_plot.setBackground(Color.red);
            // Enabling some elements
            btn_pause.setEnabled(true);

            // Disabling some elements
            cmb_lineWidth.setEnabled(false);
            cmb_plotPresentation.setEnabled(false);
            cmb_dynamicSampleLimit.setEnabled(false);
            cmb_refreshRate.setEnabled(false);
            cmb_xAxisType.setEnabled(false);
            cmb_yAxisType.setEnabled(false);

        } else if ("Stop plotting".equals(btn_plot.getText())) {
            btn_plot.setText("Start plotting");
            btn_plot.setBackground(Color.gray);
            // Disabling some elements
            btn_pause.setEnabled(false);

            // Enabling some elements
            cmb_lineWidth.setEnabled(true);
            cmb_plotPresentation.setEnabled(true);
            if ("Dynamic".equals((String) cmb_plotPresentation.getSelectedItem())) {
                cmb_dynamicSampleLimit.setEnabled(true);
            }
            cmb_refreshRate.setEnabled(true);
            cmb_xAxisType.setEnabled(true);
            cmb_yAxisType.setEnabled(true);
        }

    }

    // Method to modify the form on start recording
    public void record_actions(JButton btn_record) {
        if ("Begin rec".equals(btn_record.getText())) {
            btn_record.setText("Stop rec");
            btn_record.setBackground(Color.red);

            // Disabling some elements
        } else if ("Stop rec".equals(btn_record.getText())) {
            btn_record.setText("Begin rec");
            btn_record.setBackground(Color.gray);

            // Enabling some elements
        }
    }

    // Method to create an empty plot
    public void create_empty_plot(JPanel panel, ChartPanel CP, JFreeChart chart, int fr_w, int fr_h) {
        CP.setPreferredSize(new Dimension(fr_w - 10, (int) (5.2 * fr_h / 10 - 105)));
        CP.setEnabled(false);
        panel.add(CP);
        panel.validate();
    }

    // Method to create a plot
    public void create_simple_plot(XYSeries dataSeries, XYSeriesCollection dataset, ChartPanel CP,
            JFreeChart chart, JPanel panel, JComboBox cmb_lineWidth,
            JComboBox cmb_xAxisType, JComboBox cmb_yAxisType, int fr_w, int fr_h) {

        // Creating the plot
        // Using a renderer to improve plot quality
        XYPlot plot = (XYPlot) chart.getXYPlot();

        // Setting log format if activated
        if ("Log".equals((String) cmb_xAxisType.getSelectedItem())) {
            LogarithmicAxis xLogAxis = new LogarithmicAxis("XLog");
            xLogAxis.setAllowNegativesFlag(true);
            plot.setDomainAxis(0, xLogAxis);
            plot.getDomainAxis().setRange(dataSeries.getMinX(), dataSeries.getMaxX());
            
        } else {
            NumberAxis xDecAxis = new NumberAxis("XDec");
            xDecAxis.setAutoRange(true);
            plot.setDomainAxis(0, xDecAxis);
            plot.getDomainAxis().setRange(dataSeries.getMinX(), dataSeries.getMaxX());
        }

        if ("Log".equals((String) cmb_yAxisType.getSelectedItem())) {
            LogarithmicAxis yLogAxis = new LogarithmicAxis("YLog");
            yLogAxis.setAllowNegativesFlag(true);
            plot.setRangeAxis(yLogAxis);
            plot.getRangeAxis().setRange(dataSeries.getMinY(), dataSeries.getMaxY());
        } else {
            NumberAxis yDecAxis = new NumberAxis("YDec");
            yDecAxis.setAutoRange(true);
            plot.setRangeAxis(yDecAxis);
            plot.getRangeAxis().setRange(dataSeries.getMinY(), dataSeries.getMaxY());
        }

        chart.getXYPlot().setDataset(dataset);
        
        CP.repaint();
        CP.setPreferredSize(new Dimension(fr_w - 10, (int) (5.2 * fr_h / 10 - 105)));
        panel.setLayout(new java.awt.BorderLayout());
        
        panel.add(CP);
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        int numSeries = plot.getSeriesCount();
        for (int i = 0; i < numSeries; i++) {
            renderer.setSeriesStroke(i, new BasicStroke((Integer) cmb_lineWidth.getSelectedItem()));
        } 
        
        panel.validate();
    }
    
    // Method to create a plot
    public void create_multiple_plot(XYSeriesCollection dataset, ChartPanel CP,
            JFreeChart chart, JPanel panel, JComboBox cmb_lineWidth,
            JComboBox cmb_xAxisType, JComboBox cmb_yAxisType, int fr_w, int fr_h) {
        
        // Using a renderer to improve plot quality
        XYPlot plot = (XYPlot) chart.getXYPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        // Setting log format if activated
        if (cmb_xAxisType.getSelectedIndex() == 1) {
            LogarithmicAxis xLogAxis = new LogarithmicAxis("XLog");
            xLogAxis.setAllowNegativesFlag(true);
            xLogAxis.setAutoRangeIncludesZero(false);
            plot.setDomainAxis(xLogAxis);
        } else {
            NumberAxis xDecAxis = new NumberAxis();
            xDecAxis.setAutoRangeIncludesZero(false);
            plot.setDomainAxis(xDecAxis);
        }

        if (cmb_yAxisType.getSelectedIndex() == 1) {
            LogarithmicAxis yLogAxis = new LogarithmicAxis("YLog");
            yLogAxis.setAllowNegativesFlag(true);
            yLogAxis.setAutoRangeIncludesZero(false);
            plot.setRangeAxis(yLogAxis);
        } else {
            NumberAxis yDecAxis = new NumberAxis();
            yDecAxis.setAutoRangeIncludesZero(false);
            plot.setRangeAxis(yDecAxis);
        }

        chart.getXYPlot().setDataset(dataset);
        CP.repaint();
        CP.setPreferredSize(new Dimension(fr_w - 10, (int) (5.2 * fr_h / 10 - 105)));
        panel.setLayout(new java.awt.BorderLayout());
        
        // Setting plot line width
        int numSeries = plot.getSeriesCount();
        for (int i = 0; i < numSeries; i++) {
            renderer.setSeriesStroke(i, new BasicStroke((Integer) cmb_lineWidth.getSelectedItem()));
        }
        
        panel.add(CP);
        panel.validate();
    }

    // Method to send message to microcontroller
    public void send_message(SerialPort port, JCheckBox chk_addCR, JCheckBox chk_addNL, JTextField txt_msg) {
        String message = (String) txt_msg.getText();

        if (chk_addCR.isSelected() && chk_addNL.isSelected()) {
            message = message + "\r\n";
        }

        if (!chk_addCR.isSelected() && chk_addNL.isSelected()) {
            message = message + "\n";
        }

        if (chk_addCR.isSelected() && !chk_addNL.isSelected()) {
            message = message + "\r";
        }

        if (!chk_addCR.isSelected() && !chk_addNL.isSelected()) {
            message = message;
        }

        port.writeBytes(message.getBytes(), message.length());
        txt_msg.setText("");
    }

}
