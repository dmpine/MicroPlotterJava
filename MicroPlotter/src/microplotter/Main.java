package microplotter;

import javax.swing.SwingUtilities;
import com.formdev.flatlaf.FlatIntelliJLaf;
import microplotter.controller.ApplicationController;

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
        // --- SETUP THE MODERN LOOK AND FEEL ---
        try {
            FlatIntelliJLaf.setup(); // This sets a clean, modern "IntelliJ-like" theme.
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        System.out.println("Starting MicroPlotter...");
        SwingUtilities.invokeLater(() -> {
            // We no longer need to set the L&F here, as it's already done.
            new ApplicationController();
        });
    }
}