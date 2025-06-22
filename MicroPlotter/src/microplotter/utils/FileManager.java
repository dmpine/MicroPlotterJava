package microplotter.utils;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Handles file operations like saving recorded data.
 * This logic was extracted from the btn_record ActionListener and the
 * update_terminal method in Layout.java.
 */
public class FileManager {

    private PrintWriter printWriter;
    private boolean isRecording = false;

    /**
     * Opens a JFileChooser to select a file and prepares for writing.
     * @param parent The parent component for the dialog.
     * @return true if a file was selected and opened, false otherwise.
     */
    public boolean startRecording(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Put a name to your file");
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter(".txt files", "txt"); 
        fileChooser.setFileFilter(txtFilter);
        fileChooser.setSelectedFile(new File(Constants.DEFAULT_DATA_FILE)); 

        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile(); 
            try {
                printWriter = new PrintWriter(new FileWriter(fileToSave.getAbsolutePath(), true));
                isRecording = true;
                return true;
            } catch (IOException e) {
                System.err.println("Error opening file for recording: " + e.getMessage());
                isRecording = false;
                return false;
            }
        }
        return false;
    }

    /**
     * Stops the recording and closes the file stream.
     */
    public void stopRecording() {
        if (printWriter != null) {
            printWriter.close();
        }
        isRecording = false;
    }

    /**
     * Writes a line of data to the open file.
     * @param data The string data to write.
     */
    public void writeData(String data) {
        if (isRecording && printWriter != null) {
            printWriter.println(data); 
        }
    }

    public boolean isRecording() {
        return isRecording;
    }
}