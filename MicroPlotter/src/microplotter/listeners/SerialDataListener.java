package microplotter.listeners;

/**
 * @brief Defines a listener contract for serial communication events.
 * @details This interface can be implemented by any class that needs to be notified
 * of key events related to the serial port, such as data arrival or connection
 * status changes. It forms the basis of an Observer pattern for serial events.
 * Note: While this interface exists, the current implementation uses the
 * com.fazecast.jSerialComm.SerialPortDataListener directly in SerialController.
 */
public interface SerialDataListener {
    /**
     * @brief Called when a new, complete line of data is received from the serial port.
     * @param data The string data received, typically ending without a newline character.
     */
    void onDataReceived(String data);

    /**
     * @brief Called when the serial port connection status changes.
     * @param connected True if the port has just connected, false if it has disconnected.
     */
    void onConnectionStatusChanged(boolean connected);

    /**
     * @brief Called when an error occurs during serial communication.
     * @param error A string describing the error that occurred.
     */
    void onError(String error);
}