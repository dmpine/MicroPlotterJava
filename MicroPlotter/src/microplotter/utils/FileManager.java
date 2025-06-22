package microplotter.utils;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @brief Handles file operations for saving recorded terminal data.
 * @details This utility class encapsulates all logic related to file I/O for the
 * data recording feature. It manages the file chooser dialog, opening and closing
 * the file stream, and writing data. This logic was extracted from the original
 * Layout class.
 */
public class FileManager {

    /** @brief The PrintWriter used to write data to the target file. */
    private PrintWriter printWriter;
    /** @brief A flag indicating if data is currently being recorded to the file. */
    private boolean isRecording = false;

    /**
     * @brief Opens a JFileChooser to select a file and prepares for writing.
     * @details This method displays a "Save" dialog to the user. If the user selects a
     * file and approves, it initializes the PrintWriter to that file in append mode
     * and sets the recording state to true.
     * @param parent The parent component for the dialog, typically the main window.
     * @return True if a file was selected and successfully opened for writing, false otherwise.
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
     * @brief Stops the recording and safely closes the file stream.
     * @details If a file is currently open for writing, this method closes the
     * PrintWriter to ensure all buffered data is written to disk and resources are released.
     * It then resets the recording state.
     */
    public void stopRecording() {
        if (printWriter != null) {
            printWriter.close();
        }
        isRecording = false;
    }

    /**
     * @brief Writes a single line of data to the open file.
     * @details This method will only write the data if recording is currently active.
     * @param data The string data to write.
     */
    public void writeData(String data) {
        if (isRecording && printWriter != null) {
            printWriter.println(data);
        }
    }

    /**
     * @brief Checks the current recording status.
     * @return True if recording is active, false otherwise.
     */
    public boolean isRecording() {
        return isRecording;
    }
}