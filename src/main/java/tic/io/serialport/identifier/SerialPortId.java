package tic.io.serialport.identifier;

/**
 * Class representing a serial port identifier by its ID.
 */
public class SerialPortId {
    private String portId;

    public SerialPortId(String portId) {
        this.setPortId(portId);
    }

    public String getPortId() {
        return portId;
    }

    private void setPortId(String portId) {
        if (portId == null) {
            throw new IllegalArgumentException("Port ID cannot be null");
        }
        this.portId = portId;
    }
}
