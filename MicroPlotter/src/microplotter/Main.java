package microplotter;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import microplotter.controller.ApplicationController;

/**
 * @brief The main entry point class for the MicroPlotter application.
 * @author danielpineda
 */
public class Main {
    /**
     * @brief The main method that launches the application.
     * @details This method is the primary entry point called by the Java Virtual Machine.
     * It ensures that the GUI is created and managed on the AWT Event Dispatch Thread (EDT)
     * by using `SwingUtilities.invokeLater`. It also attempts to set the application's look
     * and feel to match the native operating system before creating an instance of the
     * `ApplicationController` to start the program.
     * @param args Command line arguments (not used by this application).
     */
    public static void main(String[] args) {
        System.out.println("Starting MicroPlotter...");
        
        // Ensure all UI operations happen on the Event Dispatch Thread (EDT) for thread safety.
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the native look and feel for UI components (e.g., Windows, macOS style).
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Could not set look and feel: " + e.getMessage());
            }
            // Start the application by creating the main controller, which builds the entire app.
            new ApplicationController();
        });
    }
}