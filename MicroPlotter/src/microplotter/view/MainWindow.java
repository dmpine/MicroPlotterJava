package microplotter.view;

import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import microplotter.utils.Constants;
import microplotter.utils.ResourceLoader;

/**
 * The main application window (JFrame).
 * It contains and arranges all the sub-panels.
 * Replaces the JFrame setup from the Layout class constructor.
 */
public class MainWindow extends JFrame {

    private PortConfigPanel portConfigPanel;
    private PlotConfigPanel plotConfigPanel;
    private PlotPanel plotPanel;
    private TerminalPanel terminalPanel;

    public MainWindow() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT); // 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Set the application icon 
        Image icon = ResourceLoader.loadImage(Constants.LOGO_ICO);
        if (icon != null) {
            setIconImage(icon);
        }

        // Setup Menu Bar 
        JMenuBar menuBar = new JMenuBar();
        JMenu windowMenu = new JMenu("Window");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        windowMenu.add(aboutMenuItem);
        menuBar.add(windowMenu);
        setJMenuBar(menuBar);
        // Note: The ActionListener for 'aboutMenuItem' will be added by a controller.

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Instantiate and add all the sub-panels
        portConfigPanel = new PortConfigPanel(Constants.DEFAULT_WIDTH);
        plotConfigPanel = new PlotConfigPanel(Constants.DEFAULT_WIDTH);
        plotPanel = new PlotPanel(Constants.DEFAULT_WIDTH, 350);
        terminalPanel = new TerminalPanel(Constants.DEFAULT_WIDTH, 200);

        mainPanel.add(portConfigPanel);
        mainPanel.add(plotConfigPanel);
        mainPanel.add(plotPanel);
        mainPanel.add(terminalPanel);

        add(mainPanel);
    }
    
    // --- Getters for the panels so controllers can access them ---
    public PortConfigPanel getPortConfigPanel() { return portConfigPanel; }
    public PlotConfigPanel getPlotConfigPanel() { return plotConfigPanel; }
    public PlotPanel getPlotPanel() { return plotPanel; }
    public TerminalPanel getTerminalPanel() { return terminalPanel; }
}