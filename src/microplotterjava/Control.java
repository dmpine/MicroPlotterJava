/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package microplotterjava;

// JFreeChart imports
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 *
 * @author danielpineda
 */
public class Control {

    // Global variables
    SerialPort port;
    InputStream inputStream;

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
        if (btn_pause.getText() == "Pause plotting") {
            btn_pause.setText("Resume plotting");
        } else if (btn_pause.getText() == "Resume plotting") {
            btn_pause.setText("Pause plotting");
        }
    }

    // Method to modify the form on start plotting
    public void plotting_form_actions(JComboBox cmb_lineWidth, JComboBox cmb_plotPresentation,
            JComboBox cmb_dynamicSampleLimit, JComboBox cmb_refreshRate, JComboBox cmb_xAxisType,
            JComboBox cmb_yAxisType, JButton btn_plot, JButton btn_pause) {

        if (btn_plot.getText() == "Start plotting") {
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

        } else if (btn_plot.getText() == "Stop plotting") {
            btn_plot.setText("Start plotting");
            btn_plot.setBackground(Color.gray);
            // Disabling some elements
            btn_pause.setEnabled(false);

            // Enabling some elements
            cmb_lineWidth.setEnabled(true);
            cmb_plotPresentation.setEnabled(true);
            if ((String) cmb_plotPresentation.getSelectedItem() == "Dynamic") {
                cmb_dynamicSampleLimit.setEnabled(true);
            }
            cmb_refreshRate.setEnabled(true);
            cmb_xAxisType.setEnabled(true);
            cmb_yAxisType.setEnabled(true);
        }

    }

    // Method to modify the form on start recording
    public void record_actions(JButton btn_record) {
        if (btn_record.getText() == "Begin rec") {
            btn_record.setText("Stop rec");
            btn_record.setBackground(Color.red);

            // Disabling some elements
        } else if (btn_record.getText() == "Stop rec") {
            btn_record.setText("Begin rec");
            btn_record.setBackground(Color.gray);

            // Enabling some elements
        }
    }

    // Method to create an empty plot
    public void create_empty_plot(JPanel panel, ChartPanel CP, JFreeChart chart, int fr_w, int fr_h) {
        // Creating a data series
        XYSeries dataSeries = new XYSeries("Data");
        // Creating a dataset
        XYSeriesCollection dataset = new XYSeriesCollection();

        // Creating the plot
        //JFreeChart chart = ChartFactory.createXYLineChart(
        //        null,//title,
        //        null,//xAxisLabel,
        //        null,//yAxisLabel,
        //        null//dataset,
        //        //PlotOrientation.VERTICAL, //Orientación del gráfico
        //        //false, //Show legends
        //);
        // Editing the plot for aestetic purposes
        /*
        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setBackgroundPaint(Color.white);
         */
        // Using a renderer to improve plot quality
        //XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        // Adding the plot to the panel
        //CP = new ChartPanel(chart);
        CP.setPreferredSize(new Dimension(fr_w - 10, (int) (5.2 * fr_h / 10 - 105)));
        CP.setEnabled(false);
        //pnlGraficas.setLayout(new java.awt.BorderLayout());
        //CP.setPreferredSize(new Dimension( (panel.getWidth()-10), (panel.getHeight()-10) ));
        //CP.setSize(new Dimension(panel.getWidth()-100, panel.getHeight()-100));
        //panel.setLayout(new java.awt.BorderLayout());
        panel.add(CP);
        panel.validate();
    }

    // Method to create a plot
    public void create_simple_plot(XYSeries dataSeries, XYSeriesCollection dataset, ChartPanel CP,
            JFreeChart chart, JPanel panel, JComboBox cmb_lineWidth,
            JComboBox cmb_xAxisType, JComboBox cmb_yAxisType, int fr_w, int fr_h) {

        // Creating the plot
        //JFreeChart chart = ChartFactory.createXYLineChart(
        //        null,//title,
        //        null,//xAxisLabel,
        //        null,//yAxisLabel,
        //        dataSet//dataset,
        //);
        // Editing the plot for aestetic purposes
        //XYPlot plot = (XYPlot) chart.getXYPlot();
        //XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        /*
        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setBackgroundPaint(Color.white);
         */
        // Using a renderer to improve plot quality
        XYPlot plot = (XYPlot) chart.getXYPlot();

        // Setting log format if activated
        if ((String) cmb_xAxisType.getSelectedItem() == "Log") {
            LogarithmicAxis xLogAxis = new LogarithmicAxis("XLog");
            //LogarithmicAxis xLogAxis = (LogarithmicAxis) chart.getXYPlot().getDomainAxis();
            xLogAxis.setAllowNegativesFlag(true);
            //xLogAxis.autoAdjustRange();
            plot.setDomainAxis(0, xLogAxis);
            plot.getDomainAxis().setRange(dataSeries.getMinX(), dataSeries.getMaxX());
            
        } else {
            NumberAxis xDecAxis = new NumberAxis("XDec");
            //NumberAxis xDecAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
            xDecAxis.setAutoRange(true);
            plot.setDomainAxis(0, xDecAxis);
            plot.getDomainAxis().setRange(dataSeries.getMinX(), dataSeries.getMaxX());
        }

        if ((String) cmb_yAxisType.getSelectedItem() == "Log") {
            LogarithmicAxis yLogAxis = new LogarithmicAxis("YLog");
            //LogarithmicAxis yLogAxis = (LogarithmicAxis) chart.getXYPlot().getDomainAxis();
            yLogAxis.setAllowNegativesFlag(true);
            //yLogAxis.setAutoRange(true);
            plot.setRangeAxis(yLogAxis);
            plot.getRangeAxis().setRange(dataSeries.getMinY(), dataSeries.getMaxY());
        } else {
            NumberAxis yDecAxis = new NumberAxis("YDec");
            //NumberAxis yDecAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            yDecAxis.setAutoRange(true);
            plot.setRangeAxis(yDecAxis);
            plot.getRangeAxis().setRange(dataSeries.getMinY(), dataSeries.getMaxY());
        }

        //CP = new ChartPanel(chart);
        chart.getXYPlot().setDataset(dataset);
        
        CP.repaint();
        CP.setPreferredSize(new Dimension(fr_w - 10, (int) (5.2 * fr_h / 10 - 105)));
        panel.setLayout(new java.awt.BorderLayout());
        //panel.removeAll();
        
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
        int numSeries = plot.getSeriesCount();
        for (int i = 0; i < numSeries; i++) {
            renderer.setSeriesStroke(i, new BasicStroke((Integer) cmb_lineWidth.getSelectedItem()));
        }

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

        //CP = new ChartPanel(chart);
        chart.getXYPlot().setDataset(dataset);
        CP.repaint();
        CP.setPreferredSize(new Dimension(fr_w - 10, (int) (5.2 * fr_h / 10 - 105)));
        panel.setLayout(new java.awt.BorderLayout());
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
