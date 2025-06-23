package microplotter.controller;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import microplotter.model.ConfigurationModel;
import microplotter.model.PlotDataModel;
import microplotter.model.SerialPortManager;
import microplotter.utils.FileManager;
import microplotter.view.MainWindow;
/**
 * @brief The main controller that initializes and wires together all components of the application.
 * @details This class acts as the application's entry point after the Main class.
 * It follows the orchestrator pattern, creating instances of all model, view, and
 * controller classes and injecting the necessary dependencies to connect them.
 */
public class ApplicationController {

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
        ConfigurationModel configModel = new ConfigurationModel();
        PlotDataModel plotDataModel = new PlotDataModel();
        FileManager fileManager = new FileManager();

        // 2. Instantiate Main View
        MainWindow mainWindow = new MainWindow();

        // 3. Wire up UI Actions
        mainWindow.getAboutMenuItem().addActionListener(e -> {
            // Load the icon using our resource loader
            ImageIcon icon = microplotter.utils.ResourceLoader.loadIcon(microplotter.utils.Constants.LOGO_PNG);
            // Show the JOptionPane with the message from our constants file
            JOptionPane.showMessageDialog(
                mainWindow,
                microplotter.utils.Constants.ABOUT_MESSAGE,
                "About MicroPlotter",
                JOptionPane.INFORMATION_MESSAGE,
                icon
            );
        });

        // 4. Instantiate Controllers
        PlotController plotController = new PlotController(mainWindow, plotDataModel, configModel);
        new SerialController(mainWindow, serialManager, configModel, fileManager, plotController);

        // 5. Make the application visible
        mainWindow.setVisible(true);
    }
}