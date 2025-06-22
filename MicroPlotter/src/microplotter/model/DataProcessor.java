package microplotter.model;

/**
 * @brief A static utility class for parsing serial data strings.
 * @details This class provides a method to convert a raw string of text received
 * from the serial port into an array of numerical values that can be used for plotting.
 * It is designed to be robust against common formatting issues like multiple delimiters
 * and non-numeric characters.
 */
public class DataProcessor {

    /**
     * @brief A simple data structure to hold the results of a parsing operation.
     * @details This static nested class acts as a container to return both the array
     * of parsed values and the number of valid columns found in a single object.
     */
    public static class ParsedData {
        /** @brief An array holding the parsed numerical values. */
        public final double[] values;
        /** @brief The number of valid numerical values found and placed in the array. */
        public final int columnCount;

        /**
         * @brief Constructs a ParsedData object.
         * @param values The array of parsed values.
         * @param columnCount The count of valid values in the array.
         */
        public ParsedData(double[] values, int columnCount) {
            this.values = values;
            this.columnCount = columnCount;
        }
    }

    /**
     * @brief Parses a string of delimited data into an array of doubles.
     * @details This method takes a raw string, splits it into tokens using space, tab,
     * or newline characters as delimiters. It then attempts to parse each token into a
     * double, skipping any non-numeric tokens. The parsing is limited to a maximum
     * of 10 columns.
     * @param data The raw string data received from the serial port.
     * @return A `ParsedData` object containing the array of parsed values and the number of valid columns found.
     */
    public static ParsedData parseSerialData(String data) {
        String delimiters = "\t\r\n ";
        String[] tokens = data.split("[" + delimiters + "]+");

        double[] series = new double[10]; // Max 10 series
        int columns = 0;

        for (String token : tokens) {
            if (columns >= 10) break; // Limit to 10 columns

            try {
                double value = Double.parseDouble(token.trim());
                series[columns] = value;
                columns++;
            } catch (NumberFormatException e) {
                // Skip invalid numbers
            }
        }

        return new ParsedData(series, columns);
    }
}