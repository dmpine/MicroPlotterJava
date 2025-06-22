package microplotter.model;

/**
 * @brief A static utility class for parsing serial data strings.
 * (Doxygen comments omitted for brevity)
 */
public class DataProcessor {

    /**
     * @brief A simple data structure to hold the results of a parsing operation.
     */
    public static class ParsedData {
        public final double[] values;
        public final String[] tags;
        public final int columnCount;

        public ParsedData(double[] values, String[] tags, int columnCount) {
            this.values = values;
            this.tags = tags;
            this.columnCount = columnCount;
        }
    }

    /**
     * @brief Parses a string of delimited data into tags and values.
     * @param data The raw string data received from the serial port.
     * @param useTagsAsNames If true, attempts to parse 'tag:value' pairs. If false, treats all tokens as values.
     * @return A `ParsedData` object containing the parsed data.
     */
    public static ParsedData parseSerialData(String data, boolean useTagsAsNames) {
        String delimiters = "\t\r\n ";
        String[] tokens = data.split("[" + delimiters + "]+");

        double[] seriesValues = new double[10];
        String[] seriesTags = new String[10];
        int columns = 0;

        // When not using tags, use the simple, original logic
        if (!useTagsAsNames) {
            for (String token : tokens) {
                if (columns >= 10 || token.isEmpty()) continue;
                try {
                    seriesValues[columns] = Double.parseDouble(token.trim());
                    seriesTags[columns] = null; // No tag
                    columns++;
                } catch (NumberFormatException e) { /* Skip invalid numbers */ }
            }
            return new ParsedData(seriesValues, seriesTags, columns);
        }

        for (int i = 0; i < tokens.length; i++) {
            if (columns >= 10) break;
            String token = tokens[i];
            if (token.isEmpty()) continue;

            // Case 1: Token contains a colon (e.g., "SINE:0999")
            if (token.contains(":")) {
                String[] parts = token.split(":", 2);
                String tag = parts[0].trim();
                
                // Case 1a: Value is in the same token ("SINE:0999")
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    try {
                        double value = Double.parseDouble(parts[1].trim());
                        seriesTags[columns] = tag;
                        seriesValues[columns] = value;
                        columns++;
                    } catch (NumberFormatException e) { /* Skip */ }
                }
                // Case 1b: Value is the NEXT token ("SINE:", "0999")
                else if (i + 1 < tokens.length) {
                    try {
                        double value = Double.parseDouble(tokens[i + 1].trim());
                        seriesTags[columns] = tag;
                        seriesValues[columns] = value;
                        columns++;
                        i++; // IMPORTANT: Skip the next token as we've already consumed it.
                    } catch (NumberFormatException e) { /* Skip */ }
                }
            }
        }
        return new ParsedData(seriesValues, seriesTags, columns);
    }
}