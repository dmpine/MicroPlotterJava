package microplotter;

import javax.swing.SwingUtilities;
import com.formdev.flatlaf.FlatIntelliJLaf;
import microplotter.controller.ApplicationController;

import microplotter.utils.AppLogger;

/**
 * @brief The main entry point class for the MicroPlotter application.
 * @author danielpineda
 */
public class Main {
    /**
     * @brief The main method that launches the application.
     * @details This is the primary entry point called by the Java Virtual Machine.
     * It sets up the modern FlatLaf look and feel and then uses
     * `SwingUtilities.invokeLater` to safely create and launch the application UI.
     * @param args Command line arguments (not used by this application).
     */
	public static void main(String[] args) {
	    AppLogger.info("Application starting up.");
	    try {
	        FlatIntelliJLaf.setup();
	    } catch( Exception ex ) {
	        AppLogger.severe("Failed to initialize LaF", ex);
	    }

	    System.out.println("Starting MicroPlotter...");
	    SwingUtilities.invokeLater(() -> {
	        new ApplicationController();
	        AppLogger.info("Application Controller initialized and UI is visible.");
	    });
	}
}