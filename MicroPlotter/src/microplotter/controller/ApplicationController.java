package microplotter.controller;

import microplotter.model.ConfigurationModel;
import microplotter.model.PlotDataModel;
import microplotter.model.SerialPortManager;
import microplotter.utils.FileManager;
import microplotter.view.MainWindow;

/**
 * The main controller that initializes and wires together all components of the application.
 */
public class ApplicationController {

    public ApplicationController() {
        // 1. Instantiate Models
        SerialPortManager serialManager = new SerialPortManager();
        ConfigurationModel configModel = new ConfigurationModel();
        PlotDataModel plotDataModel = new PlotDataModel();
        FileManager fileManager = new FileManager();

        // 2. Instantiate Main View
        MainWindow mainWindow = new MainWindow();

        // 3. Instantiate Controllers
        PlotController plotController = new PlotController(mainWindow, plotDataModel, configModel);
        SerialController serialController = new SerialController(mainWindow, serialManager, configModel, fileManager, plotController);

        // 4. Make the application visible
        mainWindow.setVisible(true);
    }
}