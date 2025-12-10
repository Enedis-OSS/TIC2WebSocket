// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

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
