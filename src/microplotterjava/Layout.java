/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package microplotterjava;

// Serial imports
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

// Form imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

// JFreeChart imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

// Data management imports
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author danielpineda
 */
public class Layout implements SerialPortDataListener {

    // Frame dimensions variables
    private final int fr_w;
    private final int fr_h;

    JFrame fr = new JFrame();

    // Global variables
    // Port configuration widgets
    JButton btn_search;
    JComboBox cmb_port;
    JComboBox cmb_baud;
    JButton btn_connect;

    // Plot configuration widgets
    JComboBox cmb_lineWidth;
    JComboBox cmb_plotPresentation;
    JComboBox cmb_dynamicSampleLimit;
    JComboBox cmb_updateTime;
    JComboBox cmb_xAxisType;
    JComboBox cmb_yAxisType;
    JButton btn_plot;
    boolean plotting = false;
    double updateTime = 0;
    JButton btn_pause;
    boolean pause = false;

    // Terminal elements
    JTextField txt_msg;
    JButton btn_send;
    JCheckBox chk_addCR;
    JCheckBox chk_addNL;
    JCheckBox chk_scroll;
    JButton btn_record;
    boolean fileRecording = false;
    String fileName = "";
    JTextArea txt_terminal;
    long startTime = 0;
    double elapsedTime = 0;

    SerialPort port;
    InputStream inputStream;
    String data = "";
    int x_data = 0;
    JPanel pane_plt = new JPanel();
    XYSeries dataSeries;
    // Possible data series
    XYSeries dataSeries0;
    XYSeries dataSeries1;
    XYSeries dataSeries2;
    XYSeries dataSeries3;
    XYSeries dataSeries4;
    XYSeries dataSeries5;
    XYSeries dataSeries6;
    XYSeries dataSeries7;
    XYSeries dataSeries8;
    XYSeries dataSeries9;

    XYSeries dataSeriesAux0;
    XYSeries dataSeriesAux1;
    XYSeries dataSeriesAux2;
    XYSeries dataSeriesAux3;
    XYSeries dataSeriesAux4;
    XYSeries dataSeriesAux5;
    XYSeries dataSeriesAux6;
    XYSeries dataSeriesAux7;
    XYSeries dataSeriesAux8;
    XYSeries dataSeriesAux9;
    XYSeriesCollection dataset = new XYSeriesCollection(dataSeries);
    XYSeriesCollection finalDataset = new XYSeriesCollection();
    JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, null);
    ChartPanel CP = new ChartPanel(chart);// new ChartPanel(chart);

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    Control cont = new Control();

    /**
     * Constructor for objects of class Layout
     *
     * @param afr_w
     * @param afr_h
     */
    public Layout(int afr_w, int afr_h) {

        fr_w = afr_w;
        fr_h = afr_h;

        fr.setSize(fr_w, fr_h);
        fr.setResizable(false);
        fr.setTitle("MicroPlotter V1.0 - By DMPT");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding a menu bar
        JMenuBar menubar = new JMenuBar();
        fr.setJMenuBar(menubar);
        JMenu window = new JMenu("Window");
        menubar.add(window);
        JMenuItem about = new JMenuItem("About");
        window.add(about);
        
        Image iconApp;
        try {
            iconApp = ImageIO.read(getClass().getClassLoader().getResource("Images/smallLogoMP.png"));
            fr.setIconImage(iconApp);
        } catch (IOException e) {
            // BU
        }

        about.addActionListener(new java.awt.event.ActionListener() {
            String msg = "Hi! My name is Daniel Pineda. I hope MicroPlotter turns out to be useful\r\n"
                    + "for you. I developed this software as a fun personal exercise.\r\n"
                    + "Hence, here is a little disclaimer: this software comes with no guarantee,\r\n"
                    + "as is usual for most of free opensource applications. You are using this\r\n"
                    + "software under your own risk. That is all for the disclaimer. Please, feel\r\n"
                    + "free to use this software as you like.\r\n"
                    + "Contact email:dmpinedat@outlook.com\r\n"
                    + "Have a nice day.\r\n"
                    + "By the way, I also designed the logo, turned out cool didn't it?\r\n";

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImageIcon icon;
                icon = new ImageIcon(getClass().getClassLoader().getResource("Images/smallLogoMP.png"));
                JOptionPane.showMessageDialog(null, msg, "Please read!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        });

        // Main panel
        JPanel mp = new JPanel();
        mp.setLayout(new BoxLayout(mp, BoxLayout.Y_AXIS));

        // Port configuration panel
        JPanel pane_port_conf = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pane_port_conf.setPreferredSize(new Dimension(fr_w - 2, (int) (fr_h / 10)));
        create_port_conf_elements(pane_port_conf);

        // Plot configuration panel
        JPanel pane_plt_conf = new JPanel();
        pane_plt_conf.setLayout(new BoxLayout(pane_plt_conf, BoxLayout.Y_AXIS));
        pane_plt_conf.setPreferredSize(new Dimension(fr_w - 2, (int) (1.5 * fr_h / 10)));
        create_plt_conf_elements(pane_plt_conf);

        // Plot panel
        pane_plt.setPreferredSize(new Dimension(fr_w - 2, (int) (5.5 * fr_h / 10)));
        create_plt_elements(pane_plt);

        // Terminal panel
        JPanel pane_term = new JPanel();
        pane_term.setPreferredSize(new Dimension(fr_w - 2, (int) (3 * fr_h / 10)));
        create_term_elements(pane_term);

        // Status panel
        //JPanel pane_status = new JPanel();
        //create_status_elements(pane_status);
        // Adding the actions
        add_actions();

        // Adding the components and show
        mp.add(pane_port_conf);
        mp.add(pane_plt_conf);
        mp.add(pane_plt);
        mp.add(pane_term);
        fr.add(mp);
        fr.show();
    }

    // Method to create the port configuration elements
    private void create_port_conf_elements(JPanel panel) {
        panel.setBorder(BorderFactory.createTitledBorder("Port configuration"));

        // pane_conf elements:
        // Button for searching available port
        btn_search = new JButton("Search port");
        btn_search.setPreferredSize(new Dimension(150, 25));
        panel.add(btn_search);
        // ComboBox for choosing from found ports
        cmb_port = new JComboBox<String>();
        cmb_port.addItem("No Port");
        cmb_port.setPreferredSize(new Dimension(250, 25));
        panel.add(cmb_port);

        // Label and ComboBox for choosing the baud rate
        JLabel lbl_baud = new JLabel("Baud rate:");
        panel.add(lbl_baud);
        cmb_baud = new JComboBox();
        cmb_baud.addItem("300");
        cmb_baud.addItem("600");
        cmb_baud.addItem("1200");
        cmb_baud.addItem("2400");
        cmb_baud.addItem("4800");
        cmb_baud.addItem("9600");
        cmb_baud.addItem("14400");
        cmb_baud.addItem("19200");
        cmb_baud.addItem("28800");
        cmb_baud.addItem("38400");
        cmb_baud.addItem("57600");
        cmb_baud.addItem("115200");
        cmb_baud.setSelectedItem("9600");

        panel.add(cmb_baud);
        // Button for connecting to port
        btn_connect = new JButton("Connect");
        btn_connect.setPreferredSize(new Dimension(100, 25));
        btn_connect.setOpaque(true);
        btn_connect.setBackground(Color.gray);
        btn_connect.setEnabled(false);
        panel.add(btn_connect);

    }

    // Method to create the plot configuration elements
    private void create_plt_conf_elements(JPanel panel) {
        panel.setBorder(BorderFactory.createTitledBorder("Plot configuration"));

        JPanel sub_panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sub_panel1.setPreferredSize(new Dimension(fr_w, 25));
        JPanel sub_panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sub_panel2.setPreferredSize(new Dimension(fr_w, 25));

        JLabel lbl_lineWidth = new JLabel("Line width");
        sub_panel1.add(lbl_lineWidth);
        cmb_lineWidth = new JComboBox();
        cmb_lineWidth.addItem(1);
        cmb_lineWidth.addItem(2);
        cmb_lineWidth.addItem(3);
        cmb_lineWidth.addItem(4);
        cmb_lineWidth.setSelectedItem(2);
        sub_panel1.add(cmb_lineWidth);

        JLabel lbl_plotPresentation = new JLabel("Presentation");
        sub_panel1.add(lbl_plotPresentation);
        cmb_plotPresentation = new JComboBox<String>();
        cmb_plotPresentation.addItem("Dynamic");
        cmb_plotPresentation.addItem("Static");
        cmb_plotPresentation.setSelectedItem("Static");
        sub_panel1.add(cmb_plotPresentation);

        JLabel lbl_dynamicSampleLimit = new JLabel("Sample limit");
        sub_panel1.add(lbl_dynamicSampleLimit);
        cmb_dynamicSampleLimit = new JComboBox();
        cmb_dynamicSampleLimit.setPreferredSize(new Dimension(100, 25));
        cmb_dynamicSampleLimit.setEnabled(false);
        cmb_dynamicSampleLimit.addItem(20);
        cmb_dynamicSampleLimit.addItem(30);
        cmb_dynamicSampleLimit.addItem(40);
        cmb_dynamicSampleLimit.addItem(50);
        cmb_dynamicSampleLimit.addItem(75);
        cmb_dynamicSampleLimit.addItem(100);
        cmb_dynamicSampleLimit.addItem(500);
        cmb_dynamicSampleLimit.addItem(1000);
        cmb_dynamicSampleLimit.setSelectedItem(50);
        sub_panel1.add(cmb_dynamicSampleLimit);
        JLabel lbl_updateTime = new JLabel("Update time");
        cmb_updateTime = new JComboBox<String>();
        cmb_updateTime.addItem("1s");
        cmb_updateTime.addItem("2s");
        cmb_updateTime.addItem("3s");
        cmb_updateTime.addItem("4s");
        cmb_updateTime.addItem("5s");
        cmb_updateTime.addItem("10s");
        cmb_updateTime.addItem("20s");
        cmb_updateTime.setSelectedItem("3s");
        sub_panel1.add(lbl_updateTime);
        sub_panel1.add(cmb_updateTime);

        JLabel lbl_xAxisType = new JLabel("X Axis type");
        sub_panel2.add(lbl_xAxisType);
        cmb_xAxisType = new JComboBox<String>();
        cmb_xAxisType.addItem("Dec");
        cmb_xAxisType.addItem("Log");
        cmb_xAxisType.setSelectedItem("Dec");
        sub_panel2.add(cmb_xAxisType);

        JLabel lbl_yAxisType = new JLabel("Y Axis type");
        sub_panel2.add(lbl_yAxisType);
        cmb_yAxisType = new JComboBox<String>();
        cmb_yAxisType.addItem("Dec");
        cmb_yAxisType.addItem("Log");
        cmb_yAxisType.setSelectedItem("Dec");
        sub_panel2.add(cmb_yAxisType);

        btn_plot = new JButton("Start plotting");
        btn_plot.setPreferredSize(new Dimension(125, 25));
        btn_plot.setOpaque(true);
        btn_plot.setBackground(Color.gray);
        btn_plot.setEnabled(false);
        sub_panel2.add(btn_plot);
        btn_pause = new JButton("Pause plotting");
        btn_pause.setPreferredSize(new Dimension(140, 25));
        btn_pause.setEnabled(false);
        sub_panel2.add(btn_pause);

        panel.add(sub_panel1);
        panel.add(sub_panel2);

    }

    // Method to create the plot elements
    private void create_plt_elements(JPanel panel) {
        panel.setBorder(BorderFactory.createTitledBorder("Plot"));

        // Creating an empty plot
        cont.create_empty_plot(panel, CP, chart, fr_w, fr_h);
    }

    // Method to create the terminal elements
    private void create_term_elements(JPanel panel) {
        panel.setBorder(BorderFactory.createTitledBorder("Terminal"));
        JPanel sub_panel = new JPanel();
        sub_panel.setLayout(new BoxLayout(sub_panel, BoxLayout.Y_AXIS));
        sub_panel.setPreferredSize(new Dimension(fr_w - 20, 160));

        JPanel term_elements_p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        term_elements_p1.setPreferredSize(new Dimension(fr_w - 20, 30));
        JLabel lbl_send = new JLabel("Serial Message");
        txt_msg = new JTextField();
        txt_msg.setPreferredSize(new Dimension(250, 20));
        JLabel lbl_addCR = new JLabel("Add CR");
        chk_addCR = new JCheckBox();
        chk_addCR.setEnabled(false);
        JLabel lbl_addNL = new JLabel("Add NL");
        chk_addNL = new JCheckBox();
        chk_addNL.setEnabled(false);
        btn_send = new JButton("Send");
        btn_send.setPreferredSize(new Dimension(75, 25));
        btn_send.setEnabled(false);
        JLabel lbl_scroll = new JLabel("Auto-scroll");
        chk_scroll = new JCheckBox();
        chk_scroll.setSelected(true);
        chk_scroll.setEnabled(false);
        // Button for recording incomming data
        btn_record = new JButton("Begin rec");
        btn_record.setPreferredSize(new Dimension(100, 25));
        btn_record.setOpaque(true);
        btn_record.setBackground(Color.gray);
        btn_record.setEnabled(false);

        term_elements_p1.add(lbl_send);
        term_elements_p1.add(txt_msg);
        term_elements_p1.add(btn_send);
        term_elements_p1.add(lbl_addCR);
        term_elements_p1.add(chk_addCR);
        term_elements_p1.add(lbl_addNL);
        term_elements_p1.add(chk_addNL);
        term_elements_p1.add(lbl_scroll);
        term_elements_p1.add(chk_scroll);
        term_elements_p1.add(btn_record);

        JPanel term_elements_p2 = new JPanel();
        term_elements_p2.setPreferredSize(new Dimension(fr_w - 20, (int) (1.6 * fr_h / 10)));
        txt_terminal = new JTextArea();
        txt_terminal.setEditable(false);
        txt_terminal.setColumns(20);
        txt_terminal.setRows(6);
        JScrollPane scroll_panel_term = new JScrollPane();
        scroll_panel_term.setPreferredSize(new Dimension(fr_w - 25, (int) (1.6 * fr_h / 10 - 5)));
        scroll_panel_term.setViewportView(txt_terminal);
        term_elements_p2.add(scroll_panel_term);

        sub_panel.add(term_elements_p1);
        sub_panel.add(term_elements_p2);
        panel.add(sub_panel);
    }

    // Method to create the status bar elements
    public void create_status_elements(JPanel panel) {
        panel.setBorder(BorderFactory.createTitledBorder("Status"));
    }

    // Method containing all the form actions
    private void add_actions() {
        // Defining actions

        // Searchin ports
        btn_search.addActionListener((ActionEvent evt) -> {
            search_port(btn_search, cmb_port, btn_connect, txt_terminal);
        });

        // Connecting to port
        btn_connect.addActionListener((ActionEvent e) -> {
            connect_port(btn_search, cmb_port, cmb_baud, btn_connect, btn_plot, btn_send, chk_scroll, btn_record, chk_addCR, chk_addNL);
        });

        // Changing to dynamic plotting
        cmb_plotPresentation.addActionListener((ActionEvent e) -> {
            if ("Static".equals((String) cmb_plotPresentation.getSelectedItem())) {
                cmb_dynamicSampleLimit.setEnabled(false);
            }
            if ("Dynamic".equals((String) cmb_plotPresentation.getSelectedItem())) {
                cmb_dynamicSampleLimit.setEnabled(true);
            }
        });

        // Send message to microcontroller (to both text field and send button
        btn_send.addActionListener((ActionEvent e) -> {
            cont.send_message(port, chk_addCR, chk_addNL, txt_msg);
        });

        txt_msg.addActionListener((ActionEvent e) -> {
            cont.send_message(port, chk_addCR, chk_addNL, txt_msg);
        });

        btn_record.addActionListener((ActionEvent e) -> {
            // TODO
            
            if (fileRecording == false) {
                // Creating the file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Put a name to your file");
                FileNameExtensionFilter txtFilter = new FileNameExtensionFilter(".txt files", "txt");
                fileChooser.setFileFilter(txtFilter);
                int userSelection = fileChooser.showSaveDialog(fr);
                File fileToSave;
                
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    fileToSave = fileChooser.getSelectedFile();
                    fileName = fileToSave.getAbsolutePath();
                    
                    fileRecording = true;
                    cont.record_actions(btn_record);
                } else {
                    fileRecording = false;
                }
                
            } else if (fileRecording == true) {
                fileRecording = false;
                cont.record_actions(btn_record);
            }
        });

        // Plotting data action
        btn_plot.addActionListener((ActionEvent e) -> {
            if ("Start plotting".equals(btn_plot.getText())) {
                x_data = 0;
                dataSeries = new XYSeries("Data");
                new XYSeries("Data");
                dataset = new XYSeriesCollection();

                // Possible data series
                dataSeries0 = new XYSeries("D0");
                dataSeries1 = new XYSeries("D1");
                dataSeries2 = new XYSeries("D2");
                dataSeries3 = new XYSeries("D3");
                dataSeries4 = new XYSeries("D4");
                dataSeries5 = new XYSeries("D5");
                dataSeries6 = new XYSeries("D6");
                dataSeries7 = new XYSeries("D7");
                dataSeries8 = new XYSeries("D8");
                dataSeries9 = new XYSeries("D9");

                dataSeriesAux0 = new XYSeries("D0");
                dataSeriesAux1 = new XYSeries("D1");
                dataSeriesAux2 = new XYSeries("D2");
                dataSeriesAux3 = new XYSeries("D3");
                dataSeriesAux4 = new XYSeries("D4");
                dataSeriesAux5 = new XYSeries("D5");
                dataSeriesAux6 = new XYSeries("D6");
                dataSeriesAux7 = new XYSeries("D7");
                dataSeriesAux8 = new XYSeries("D8");
                dataSeriesAux9 = new XYSeries("D9");

                startTime = System.currentTimeMillis();
                
            }

            cont.plotting_form_actions(cmb_lineWidth, cmb_plotPresentation,
                    cmb_dynamicSampleLimit, cmb_updateTime, cmb_xAxisType,
                    cmb_yAxisType, btn_plot, btn_pause);
            if ("Start plotting".equals(btn_plot.getText())) {
                plotting = false;
            }
            if ("Stop plotting".equals(btn_plot.getText())) {
                plotting = true;
            }

            switch (cmb_updateTime.getSelectedIndex()) {
                case 0: // 1s
                    updateTime = 1.0;
                    break;

                case 1: // 2s
                    updateTime = 2.0;
                    break;

                case 2: // 3s
                    updateTime = 3.0;
                    break;

                case 3: // 4s
                    updateTime = 4.0;
                    break;

                case 4: // 5s
                    updateTime = 5.0;
                    break;

                case 5: // 10s
                    updateTime = 10.0;
                    break;

                case 6: // 20s
                    updateTime = 20.0;
                    break;
            }
        });

        btn_pause.addActionListener((ActionEvent e) -> {
            cont.pause_form_actions(btn_pause);
            if ("Pause plotting".equals(btn_pause.getText())) {
                pause = false;
            }
            if ("Resume plotting".equals(btn_pause.getText())) {
                pause = true;
            }
        });

    }

    // Other methods
    // Method to get the available ports
    public void search_port(JButton btn_search, JComboBox cmb_port, JButton btn_connect, JTextArea txt_terminal) {

        // Reseting the list
        cmb_port.removeAllItems();

        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort prt : ports) {

            if (prt == null) {
                cmb_port.addItem("No port found");
                btn_connect.setEnabled(false);
            }
            if (!prt.getDescriptivePortName().contains("Dial")) {
                cmb_port.addItem(prt.getSystemPortName());
                btn_connect.setEnabled(true);
                btn_search.setText("Update port list");
            }

        }

    }

    // Method to connect to serial device
    public void connect_port(JButton btn_search, JComboBox cmb_port,
            JComboBox cmb_baud, JButton btn_connect,
            JButton btn_plot, JButton btn_send, JCheckBox chk_scroll,
            JButton btn_record, JCheckBox chk_addCR, JCheckBox chk_addNL) {

        if ("Connect".equals(btn_connect.getText())) {

            btn_connect.setText("Disconnect");
            btn_connect.setOpaque(true);
            btn_connect.setBackground(Color.RED);
            //btn_connect.setForeground(Color.RED);

            // Disabling some elements
            btn_search.setEnabled(false);
            cmb_port.setEnabled(false);
            cmb_baud.setEnabled(false);

            // Enabling some elements
            btn_plot.setEnabled(true);
            btn_send.setEnabled(true);
            chk_scroll.setEnabled(true);
            btn_record.setEnabled(true);
            chk_addCR.setEnabled(true);
            chk_addNL.setEnabled(true);

            // Configuring and connecting to port
            port = SerialPort.getCommPort((String) cmb_port.getSelectedItem());
            port.setBaudRate(Integer.valueOf((String) cmb_baud.getSelectedItem()));
            if (port.isOpen()) {
                port.closePort();
            }
            port.openPort();
            port.addDataListener(this);

        } else if ("Disconnect".equals(btn_connect.getText())) {

            btn_connect.setText("Connect");
            btn_connect.setOpaque(true);
            btn_connect.setBackground(Color.gray);

            // Disabling some elements
            btn_plot.setEnabled(false);
            btn_send.setEnabled(false);
            chk_scroll.setEnabled(false);
            btn_record.setEnabled(false);
            btn_record.setText("Begin rec");
            btn_record.setBackground(Color.gray);
            fileRecording = false;
            chk_addCR.setEnabled(false);
            chk_addNL.setEnabled(false);

            // Enabling some elements
            btn_search.setEnabled(true);
            cmb_port.setEnabled(true);
            cmb_baud.setEnabled(true);

            // Close the port
            port.closePort();

        }

    }

    // Method to manage data printed to terminal
    public void update_terminal(String data) {

        Date date = new Date();
        
        txt_terminal.append(formatter.format(date) + "\t" + data + "\r\n");
        if (chk_scroll.isSelected()) {
            txt_terminal.setCaretPosition(txt_terminal.getDocument().getLength());
        }

        if (fileRecording) {
            try ( FileWriter fw = new FileWriter(fileName, true);  PrintWriter out = new PrintWriter(fw)) {
                out.println(formatter.format(date) + "\t" + data);

            } catch (IOException e) {
                // Bu
            }
        }

    }

    // Method to update plot
    public void update_plot(String data) {
        String delimiters = "\t" + "\r" + "\n" + " ";
        StringTokenizer str = new StringTokenizer(data, delimiters);
        double series[] = new double[200];
        double auxf = 0;
        int columns = 0;
        while (str.hasMoreElements()) {
            String auxStr = str.nextToken();
            try{
                auxf = Double.parseDouble(auxStr);
                series[columns] = auxf;
                columns++;
            } catch (NumberFormatException e){
                // Nothing
            }
            
            // More than 9 columns are prohibited
            if(columns > 9) {
                columns = 9;
                break;
            }
        }

        x_data++;

        try {
            for (int j = 0; j < columns; j++) {
                auxf = series[j];

                switch (j) {
                    case 0:
                        dataSeriesAux0.add(x_data, auxf);
                        break;
                    case 1:
                        dataSeriesAux1.add(x_data, auxf);
                        break;
                    case 2:
                        dataSeriesAux2.add(x_data, auxf);
                        break;
                    case 3:
                        dataSeriesAux3.add(x_data, auxf);
                        break;
                    case 4:
                        dataSeriesAux4.add(x_data, auxf);
                        break;
                    case 5:
                        dataSeriesAux5.add(x_data, auxf);
                        break;
                    case 6:
                        dataSeriesAux6.add(x_data, auxf);
                        break;
                    case 7:
                        dataSeriesAux7.add(x_data, auxf);
                        break;
                    case 8:
                        dataSeriesAux8.add(x_data, auxf);
                        break;
                    case 9:
                        dataSeriesAux9.add(x_data, auxf);
                        break;
                }
            }

            elapsedTime = cont.time_counter(startTime);

            // Check if it is a dynamic or static plot
            if (cmb_plotPresentation.getSelectedIndex() == 0) {
                int dataCount = dataSeriesAux0.getItemCount();
                for (int j = 0; j < columns; j++) {
                    
                    if (dataCount > (Integer) cmb_dynamicSampleLimit.getSelectedItem()) {
                        switch (j) {
                            case 0:
                                dataSeriesAux0.remove(0);
                                break;
                            case 1:
                                dataSeriesAux1.remove(0);
                                break;
                            case 2:
                                dataSeriesAux2.remove(0);
                                break;
                            case 3:
                                dataSeriesAux3.remove(0);
                                break;
                            case 4:
                                dataSeriesAux4.remove(0);
                                break;
                            case 5:
                                dataSeriesAux5.remove(0);
                                break;
                            case 6:
                                dataSeriesAux6.remove(0);
                                break;
                            case 7:
                                dataSeriesAux7.remove(0);
                                break;
                            case 8:
                                dataSeriesAux8.remove(0);
                                break;
                            case 9:
                                dataSeriesAux9.remove(0);
                                break;
                        }
                    }

                }
            }

            if (elapsedTime >= updateTime && !pause) {

                dataset.removeAllSeries();
                
                dataset = new XYSeriesCollection();
                for (int j = 0; j < columns; j++) {
                    switch (j) {
                        case 0:
                            Object cloneSeries0 = dataSeriesAux0.clone();
                            dataSeries0 = (XYSeries) cloneSeries0;
                            dataset.addSeries(dataSeries0);
                            break;
                        case 1:
                            Object cloneSeries1 = dataSeriesAux1.clone();
                            dataSeries1 = (XYSeries) cloneSeries1;
                            dataset.addSeries(dataSeries1);
                            break;
                        case 2:
                            Object cloneSeries2 = dataSeriesAux2.clone();
                            dataSeries2 = (XYSeries) cloneSeries2;
                            dataset.addSeries(dataSeries2);
                            break;
                        case 3:
                            Object cloneSeries3 = dataSeriesAux3.clone();
                            dataSeries3 = (XYSeries) cloneSeries3;
                            dataset.addSeries(dataSeries3);
                            break;
                        case 4:
                            Object cloneSeries4 = dataSeriesAux4.clone();
                            dataSeries4 = (XYSeries) cloneSeries4;
                            dataset.addSeries(dataSeries4);
                            break;
                        case 5:
                            Object cloneSeries5 = dataSeriesAux5.clone();
                            dataSeries5 = (XYSeries) cloneSeries5;
                            dataset.addSeries(dataSeries5);
                            break;
                        case 6:
                            Object cloneSeries6 = dataSeriesAux6.clone();
                            dataSeries6 = (XYSeries) cloneSeries6;
                            dataset.addSeries(dataSeries6);
                            break;
                        case 7:
                            Object cloneSeries7 = dataSeriesAux7.clone();
                            dataSeries7 = (XYSeries) cloneSeries7;
                            dataset.addSeries(dataSeries7);
                            break;
                        case 8:
                            Object cloneSeries8 = dataSeriesAux8.clone();
                            dataSeries8 = (XYSeries) cloneSeries8;
                            dataset.addSeries(dataSeries8);
                            break;
                        case 9:
                            Object cloneSeries9 = dataSeriesAux9.clone();
                            dataSeries9 = (XYSeries) cloneSeries9;
                            dataset.addSeries(dataSeries9);
                            break;
                    }
                }

                Object cloneDataset = dataset.clone();
                finalDataset = (XYSeriesCollection) cloneDataset;

                cont.create_multiple_plot(finalDataset, CP, chart, pane_plt, cmb_lineWidth, cmb_xAxisType, cmb_yAxisType, fr_w, fr_h);
                startTime = System.currentTimeMillis();
            }

        } catch (CloneNotSupportedException | NumberFormatException e) {
            //System.out.println(e);
        }

    }

    /**
     *
     * @return
     */
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        
        inputStream = port.getInputStream();
        String dataAux = "";

        try {
            byte[] readBuffer = new byte[inputStream.available()];
            while (inputStream.available() > 0) {
                inputStream.read(readBuffer);
                dataAux += new String(readBuffer);
            }

            data += dataAux;
            
            if (data.length() == 0 || data.isEmpty()) {
                // Nothing
            } else if (dataAux.contains("\n") || dataAux.contains("\r")) {

                // Remove some characters
                data = data.replace("\n", "").replace("\r", "");

                // Last verification after cleaning the data
                if(data.length() == 0 || data.isEmpty()){
                    // Do nothing
                } else{
                    if (plotting) {
                        update_plot(data);
                    }
                    update_terminal(data);
                }
                
                // Always reset variable
                data = "";
            }

        } catch (IOException | NumberFormatException e) {
            // Bu
        }

    }

    // Method to append data to array
    public void append(float[] array, float number) {
        if (array == null) {
            float[] aux_array = new float[1];
            aux_array[0] = number;
            array = aux_array;
        } else {
            array = Arrays.copyOf(array, array.length + 1);
            array[array.length - 1] = number;
        }
    }

}
