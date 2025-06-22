package microplotter.utils;

public class Constants {
    // Application constants
    public static final String APP_TITLE = "MicroPlotter V1.0 - By DMPT";
    public static final String APP_VERSION = "1.0";
    public static final int DEFAULT_WIDTH = 990;
    public static final int DEFAULT_HEIGHT = 730;
    
    // Serial port constants
    public static final String[] BAUD_RATES = {
        "300", "600", "1200", "2400", "4800", "9600", 
        "14400", "19200", "28800", "38400", "57600", "115200"
    };
    public static final String DEFAULT_BAUD_RATE = "9600";
    
    // Plot constants
    public static final Integer[] LINE_WIDTHS = {1, 2, 3, 4};
    public static final Integer DEFAULT_LINE_WIDTH = 2;
    public static final String[] PLOT_PRESENTATIONS = {"Dynamic", "Static"};
    public static final Integer[] SAMPLE_LIMITS = {20, 30, 40, 50, 75, 100, 500, 1000};
    public static final String[] UPDATE_TIMES = {"1s", "2s", "3s", "4s", "5s", "10s", "20s"};
    
    // File constants
    public static final String DEFAULT_DATA_FILE = "Data.txt";
    public static final String LOGO_ICO = "smallLogoMPclean.ico";
    public static final String LOGO_PNG = "smallLogoMP.png";
}
