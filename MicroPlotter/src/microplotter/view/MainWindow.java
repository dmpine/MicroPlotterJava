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
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

    /** @brief The panel for serial port configuration UI components. */
    private PortConfigPanel portConfigPanel;
    /** @brief The panel for plot configuration UI components. */
    private PlotConfigPanel plotConfigPanel;
    /** @brief The panel that contains the JFreeChart plot. */
    private PlotPanel plotPanel;
    /** @brief The panel for the terminal output and message sending. */
    private TerminalPanel terminalPanel;
    /** @brief The about menu. */
    private JMenuItem aboutMenuItem; 

    /**
     * @brief Constructs the main application window.
     * @details Sets up the JFrame properties such as title, size, and icon. It then
     * creates a main content panel and instantiates and adds all the specialized
     * sub-panels in the correct vertical order.
     */
    public MainWindow() {
        setTitle(Constants.APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true); // Allow resizing

        // Set the application icon
        Image icon = ResourceLoader.loadImage(Constants.LOGO_ICO);
        if (icon != null) {
            setIconImage(icon);
        }

        // Setup Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu windowMenu = new JMenu("Window");
        this.aboutMenuItem = new JMenuItem("About");
        windowMenu.add(this.aboutMenuItem);
        menuBar.add(windowMenu);
        setJMenuBar(menuBar);

        // --- NEW RESPONSIVE LAYOUT ---
        // Main content panel now uses BorderLayout
        JPanel mainPanel = new JPanel(new java.awt.BorderLayout(5, 5));

        // Create a dedicated panel for the top configuration sections
        JPanel configSectionPanel = new JPanel();
        configSectionPanel.setLayout(new BoxLayout(configSectionPanel, BoxLayout.Y_AXIS));
        
        // Instantiate and add config panels to their own section
        portConfigPanel = new PortConfigPanel();
        plotConfigPanel = new PlotConfigPanel();
        configSectionPanel.add(portConfigPanel);
        configSectionPanel.add(plotConfigPanel);
        
        // Instantiate the main content and terminal panels
        plotPanel = new PlotPanel();
        terminalPanel = new TerminalPanel();

        // Add the sections to the main panel's regions
        mainPanel.add(configSectionPanel, java.awt.BorderLayout.NORTH);
        mainPanel.add(plotPanel, java.awt.BorderLayout.CENTER); // Center grows
        mainPanel.add(terminalPanel, java.awt.BorderLayout.SOUTH);

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
    
    /**
     * @brief Gets the 'About' menu item from the menu bar.
     * @return The JMenuItem for the 'About' action.
     */
    public JMenuItem getAboutMenuItem() { return aboutMenuItem; }
}