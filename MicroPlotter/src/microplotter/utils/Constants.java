package microplotter.utils;

/**
 * @brief A central repository for all static application-wide constants.
 * @details This class prevents the use of magic numbers or strings throughout the codebase.
 * It provides a single, easy-to-manage location for all configurable default values
 * and fixed settings for the UI, serial communication, and plotting.
 */
public class Constants {
    // --- Application constants ---
    /** @brief The title displayed in the main application window. */
    public static final String APP_TITLE = "MicroPlotter V2.0 - By DMPT";
    /** @brief The current version of the application. */
    public static final String APP_VERSION = "1.0";
    /** @brief The default width of the main application window in pixels. */
    public static final int DEFAULT_WIDTH = 990;
    /** @brief The default height of the main application window in pixels. */
    public static final int DEFAULT_HEIGHT = 730;

    // --- Serial port constants ---
    /** @brief An array of standard serial port baud rates offered to the user. */
    public static final String[] BAUD_RATES = {
        "300", "600", "1200", "2400", "4800", "9600",
        "14400", "19200", "28800", "38400", "57600", "115200"
    };
    /** @brief The default baud rate selected when the application starts. */
    public static final String DEFAULT_BAUD_RATE = "9600";

    // --- Plot constants ---
    /** @brief An array of line widths (in pixels) for the plot, offered to the user. */
    public static final Integer[] LINE_WIDTHS = {1, 2, 3, 4};
    /** @brief The default line width selected when the application starts. */
    public static final Integer DEFAULT_LINE_WIDTH = 2;
    /** @brief The available plot presentation modes. */
    public static final String[] PLOT_PRESENTATIONS = {"Dynamic", "Static"};
    /** @brief The available sample limits for the "Dynamic" plot mode. */
    public static final Integer[] SAMPLE_LIMITS = {20, 30, 40, 50, 75, 100, 500, 1000};
    /** @brief The available update time intervals for the plot. */
    public static final String[] UPDATE_TIMES = {"0.5s", "1s", "2s", "3s", "4s", "5s", "10s", "20s"};

    // --- File and Resource constants ---
    /** @brief The default filename suggested when saving recorded data. */
    public static final String DEFAULT_DATA_FILE = "Data.txt";
    /** @brief The filename for the application's icon in .ico format. */
    public static final String LOGO_ICO = "smallLogoMPclean.ico";
    /** @brief The filename for the application's icon in .png format. */
    public static final String LOGO_PNG = "smallLogoMP.png";
}