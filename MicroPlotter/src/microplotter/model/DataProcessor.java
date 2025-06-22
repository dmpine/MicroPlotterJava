package microplotter.model;

public class DataProcessor {
    
    public static class ParsedData {
        public final double[] values;
        public final int columnCount;
        
        public ParsedData(double[] values, int columnCount) {
            this.values = values;
            this.columnCount = columnCount;
        }
    }
    
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
