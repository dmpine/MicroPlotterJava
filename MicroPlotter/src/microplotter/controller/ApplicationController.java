package microplotter.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import microplotter.model.ConfigurationModel;
import microplotter.model.PlotDataModel;
import microplotter.model.SerialPortManager;
import microplotter.utils.ConfigurationManager;
import microplotter.utils.Constants;
import microplotter.utils.FileManager;
import microplotter.view.MainWindow;

import microplotter.view.HttpConfigDialog;

import microplotter.controller.NetworkingController;
import microplotter.view.MqttConfigDialog;

/**
 * @brief The main controller that initializes and wires together all components of the application.
 * @details This class acts as the application's entry point after the Main class.
 * It follows the orchestrator pattern, creating instances of all model, view, and
 * controller classes and injecting the necessary dependencies to connect them.
 */
public class ApplicationController {

	/** @brief The controller responsible for plotting logic. Stored as a field for accessibility. */
    private PlotController plotController;
    /** @brief The application's configuration model. */
    private ConfigurationModel configModel;
    /** @brief The controller responsible for mqtt networking logic. */
    private NetworkingController networkingController;
	
    /**
     * @brief Constructs the ApplicationController, which builds and launches the entire application.
     * @details The constructor performs the following steps in order:
     * 1. Instantiates all data models (SerialPortManager, ConfigurationModel, etc.). 
     * 2. Instantiates the main UI window (MainWindow). 
     * 3. Instantiates the other controllers (PlotController, SerialController), injecting the models and views they depend on. 
     * 4. Makes the main window visible to the user. 
     */
    public ApplicationController() {
    	// 1. Instantiate Models
        SerialPortManager serialManager = new SerialPortManager();
        this.configModel = new ConfigurationModel();
        
        File sessionFile = new File(System.getProperty("user.home"), Constants.SESSION_CONFIG_FILE);
        ConfigurationManager.loadConfiguration(configModel, sessionFile);

        PlotDataModel plotDataModel = new PlotDataModel();
        FileManager fileManager = new FileManager();

        // 2. Instantiate Main View
        MainWindow mainWindow = new MainWindow();

        // 3. Instantiate ALL Controllers
        this.plotController = new PlotController(mainWindow, plotDataModel, configModel);
        this.networkingController = new NetworkingController(configModel);
        new SerialController(mainWindow, serialManager, configModel, fileManager, plotController, networkingController);
        
        // 4. Wire up UI and sync model
        plotController.syncViewToModel();
        addMenuListeners(mainWindow);
        
        // Window Listener to Save on Exit
        mainWindow.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                plotController.updateConfigFromUI();
                ConfigurationManager.saveConfiguration(configModel, sessionFile);
                networkingController.disconnectMqtt(); // Gracefully disconnect
                e.getWindow().dispose();
                System.exit(0);
            }
        });
        
        // 5. Pack, Center, and Show the application
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setMinimumSize(mainWindow.getSize());
        networkingController.applyMqttConnectionState(); // Attempt to connect on startup
        mainWindow.setVisible(true);
    }
	
    /**
     * @brief Wires up the ActionListeners for the File menu.
     */
    private void addMenuListeners(MainWindow mainWindow) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Properties Files", "properties"));

        mainWindow.getSaveConfigMenuItem().addActionListener(e -> {
            plotController.updateConfigFromUI(); // First, ensure the model is current
            if (fc.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (!file.getName().endsWith(".properties")) {
                    file = new File(file.getAbsolutePath() + ".properties");
                }
                ConfigurationManager.saveConfiguration(configModel, file);
            }
        });

        mainWindow.getLoadConfigMenuItem().addActionListener(e -> {
            if (fc.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
                ConfigurationManager.loadConfiguration(configModel, fc.getSelectedFile());
                // --- THIS IS THE SIMPLIFIED AND CORRECTED LINE ---
                // After loading, we MUST update the UI to reflect the new model state
                plotController.syncViewToModel();
            }
        });

        // Add the listener for the About menu here too for good organization
        mainWindow.getAboutMenuItem().addActionListener(e -> {
            ImageIcon icon = microplotter.utils.ResourceLoader.loadIcon(Constants.LOGO_PNG);
            JOptionPane.showMessageDialog(
                mainWindow,
                Constants.ABOUT_MESSAGE,
                "About MicroPlotter",
                JOptionPane.INFORMATION_MESSAGE,
                icon
            );
        });
        
        // Networking Menu Action
        mainWindow.getHttpConfigMenuItem().addActionListener(e -> {
            HttpConfigDialog httpDialog = new HttpConfigDialog(mainWindow, configModel);
            httpDialog.setVisible(true); // This dialog will save the changes to the model
        });
        
        mainWindow.getMqttConfigMenuItem().addActionListener(e -> {
            MqttConfigDialog mqttDialog = new MqttConfigDialog(mainWindow, configModel);
            networkingController.handleMqttDialog(mqttDialog);
            mqttDialog.setVisible(true);
        });
    }
}