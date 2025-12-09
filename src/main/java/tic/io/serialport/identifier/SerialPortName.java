package tic.io.serialport.identifier;

/**
 * Class representing a serial port identified by its name.
 */
public class SerialPortName {
    private String portName;

    public SerialPortName(String portName) {
        this.setPortName(portName);
    }

    public String getPortName() {
        return portName;
    }

    private void setPortName(String portName) {
        if (portName == null) {
            throw new IllegalArgumentException("Port name cannot be null");
        }
        this.portName = portName;
    }
}
