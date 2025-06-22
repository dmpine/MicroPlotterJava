package microplotter;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import microplotter.controller.ApplicationController;

/**
 *
 * @author danielpineda
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting MicroPlotter..."); // 
        // Ensure UI operations are on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the native look and feel for the UI
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // 
            } catch (Exception e) {
                System.err.println("Could not set look and feel: " + e.getMessage()); // 
            }
            // Start the application by creating the main controller
            new ApplicationController();
        });
    }
}