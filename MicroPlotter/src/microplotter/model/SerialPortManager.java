package microplotter.model;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SerialPortManager {
    private SerialPort currentPort;
    private boolean isConnected = false;
    
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
    
    public void disconnect() {
        if (currentPort != null && currentPort.isOpen()) {
            currentPort.closePort();
            isConnected = false;
        }
    }
    
    public void addDataListener(SerialPortDataListener listener) {
        if (currentPort != null) {
            currentPort.addDataListener(listener);
        }
    }
    
    public boolean writeData(String data) {
        if (currentPort != null && isConnected) {
            byte[] bytes = data.getBytes();
            return currentPort.writeBytes(bytes, bytes.length) == bytes.length;
        }
        return false;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public SerialPort getCurrentPort() {
        return currentPort;
    }
}
