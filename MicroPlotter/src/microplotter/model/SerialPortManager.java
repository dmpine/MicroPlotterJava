package microplotter.model;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Manages serial port interactions and connection lifecycle.
 * @details This model class encapsulates all direct interactions with the jSerialComm library.
 * It handles discovering available ports, establishing and closing connections,
 * and writing data, providing a simplified interface for the controllers.
 */
public class SerialPortManager {
    /** @brief The active serial port object from the jSerialComm library. */
    private SerialPort currentPort;
    /** @brief A flag indicating the current connection state. */
    private boolean isConnected = false;

    /**
     * @brief Scans the system for available serial ports.
     * @details It filters out ports that typically represent modems or other non-standard devices.
     * If no valid ports are found, it returns a list containing a "No port found" message.
     * @return A list of strings containing the names of available serial ports.
     */
    public List<String> getAvailablePorts() {
        List<String> portNames = new ArrayList<>();
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort port : ports) {
            if (port != null && !port.getDescriptivePortName().contains("Dial")) {
                portNames.add(port.getSystemPortName());
            }
        }

        if (portNames.isEmpty()) {
            portNames.add("No port found");
        }

        return portNames;
    }

    /**
     * @brief Connects to a specified serial port.
     * @details Attempts to open a connection to the given port with the specified baud rate
     * and standard 8N1 (8 data bits, no parity, 1 stop bit) parameters.
     * @param portName The system name of the port to connect to (e.g., "COM3" or "/dev/ttyUSB0").
     * @param baudRate The baud rate for the connection (e.g., 9600).
     * @return True if the connection was successfully opened, false otherwise.
     */
    public boolean connect(String portName, int baudRate) {
        if (isConnected) {
            disconnect();
        }

        try {
            currentPort = SerialPort.getCommPort(portName);
            currentPort.setComPortParameters(baudRate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

            if (currentPort.openPort()) {
                isConnected = true;
                return true;
            }
        } catch (Exception e) {
            System.err.println("Failed to connect to port: " + e.getMessage());
        }

        return false;
    }

    /**
     * @brief Disconnects from the currently active serial port.
     * @details If a port is open, it closes the connection and updates the connection state.
     */
    public void disconnect() {
        if (currentPort != null && currentPort.isOpen()) {
            currentPort.closePort();
            isConnected = false;
        }
    }

    /**
     * @brief Attaches a data listener to the current port.
     * @details The provided listener will be notified when serial port events, such as
     * data arrival, occur. This is typically used to attach a controller.
     * @param listener The SerialPortDataListener to attach.
     */
    public void addDataListener(SerialPortDataListener listener) {
        if (currentPort != null) {
            currentPort.addDataListener(listener);
        }
    }

    /**
     * @brief Writes a string of data to the connected serial port.
     * @param data The string data to be sent.
     * @return True if the data was written successfully, false otherwise.
     */
    public boolean writeData(String data) {
        if (currentPort != null && isConnected) {
            byte[] bytes = data.getBytes();
            return currentPort.writeBytes(bytes, bytes.length) == bytes.length;
        }
        return false;
    }

    /**
     * @brief Checks the current connection state.
     * @return True if a port is connected, false otherwise.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * @brief Gets the underlying SerialPort object.
     * @return The current SerialPort instance, or null if not connected.
     */
    public SerialPort getCurrentPort() {
        return currentPort;
    }
}