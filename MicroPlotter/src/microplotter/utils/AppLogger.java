package microplotter.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @brief Provides a centralized, static logging service for the application.
 * @details Configures a file-based logger with rotation to prevent log files
 * from growing indefinitely. It provides simple static methods for different
 * log levels.
 */
public class AppLogger {
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());
    private static final long MAX_LOG_SIZE = 20 * 1024 * 1024; // 20 MB
    private static final int LOG_ROTATION_COUNT = 5;

    /**
     * @brief Initializes the static logger instance.
     * @details This static block is executed once when the class is loaded.
     * It sets up the FileHandler for logging to a rotating file set.
     */
    static {
        try {
            // Prevent the logger from sending output to the console
            logger.setUseParentHandlers(false);

            // Configure the FileHandler
            // %h expands to the user's home directory
            // %g is the generation number for the rotating log files
            FileHandler fileHandler = new FileHandler(
                "%h/microplotter%g.log",
                (int) MAX_LOG_SIZE,
                LOG_ROTATION_COUNT,
                true // Append to existing file
            );

            // Set a simple, human-readable format
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            // Add the handler to our logger
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not initialize logger file handler.", e);
        }
    }

    /**
     * @brief Logs an informational message.
     * @param message The message to log.
     */
    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    /**
     * @brief Logs a warning message.
     * @param message The message to log.
     */
    public static void warning(String message) {
        logger.log(Level.WARNING, message);
    }

    /**
     * @brief Logs a severe error message, typically with an exception.
     * @param message The error description.
     * @param thrown The exception that was caught.
     */
    public static void severe(String message, Throwable thrown) {
        logger.log(Level.SEVERE, message, thrown);
    }
}