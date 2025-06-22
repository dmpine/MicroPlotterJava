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
 * @brief The main application window (JFrame).
 * @details This class extends JFrame and serves as the primary container for the entire
 * user interface. It contains and arranges all the specialized sub-panels
 * (PortConfigPanel, PlotConfigPanel, etc.). Its creation logic replaces the
 * JFrame setup from the original Layout class constructor.
 */
public class MainWindow extends JFrame {

    /** @brief The panel for serial port configuration UI components. */
    private PortConfigPanel portConfigPanel;
    /** @brief The panel for plot configuration UI components. */
    private PlotConfigPanel plotConfigPanel;
    /** @brief The panel that contains the JFreeChart plot. */
    private PlotPanel plotPanel;
    /** @brief The panel for the terminal output and message sending. */
    private TerminalPanel terminalPanel;

    /**
     * @brief Constructs the main application window.
     * @details Sets up the JFrame properties such as title, size, and icon. It then
     * creates a main content panel and instantiates and adds all the specialized
     * sub-panels in the correct vertical order.
     */
    public MainWindow() {
        setTitle(Constants.APP_TITLE);
        setSize(Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT);
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

    /**
     * @brief Gets the port configuration panel.
     * @return The instance of the PortConfigPanel.
     */
    public PortConfigPanel getPortConfigPanel() { return portConfigPanel; }
    /**
     * @brief Gets the plot configuration panel.
     * @return The instance of the PlotConfigPanel.
     */
    public PlotConfigPanel getPlotConfigPanel() { return plotConfigPanel; }
    /**
     * @brief Gets the plot display panel.
     * @return The instance of the PlotPanel.
     */
    public PlotPanel getPlotPanel() { return plotPanel; }
    /**
     * @brief Gets the terminal panel.
     * @return The instance of the TerminalPanel.
     */
    public TerminalPanel getTerminalPanel() { return terminalPanel; }
}