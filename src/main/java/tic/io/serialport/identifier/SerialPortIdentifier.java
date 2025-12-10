// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport.identifier;

/** Class representing a serial port identifier, which can be either by name or by ID. */
public class SerialPortIdentifier {
  private SerialPortName portName;
  private SerialPortId portId;
  private SerialPortIdentifierType type;

  public SerialPortIdentifier(SerialPortName portName) {
    this.type = SerialPortIdentifierType.PORT_NAME;
    this.setPortName(portName);
  }

  public SerialPortIdentifier(SerialPortId portId) {
    this.type = SerialPortIdentifierType.PORT_ID;
    this.setPortId(portId);
  }

  public String getPortName() {
    return this.portName.getPortName();
  }

  public String getPortId() {
    return this.portId.getPortId();
  }

  public SerialPortIdentifierType getType() {
    return this.type;
  }

  private void setPortName(SerialPortName portName) {
    if (this.type == SerialPortIdentifierType.PORT_ID) {
      throw new IllegalStateException("Cannot set port name when identifier type is PORT_ID");
    }
    if (portName == null) {
      throw new IllegalArgumentException("Port name cannot be null");
    }
    this.portName = portName;
  }

  private void setPortId(SerialPortId portId) {
    if (this.type == SerialPortIdentifierType.PORT_NAME) {
      throw new IllegalStateException("Cannot set port ID when identifier type is PORT_NAME");
    }
    if (portId == null) {
      throw new IllegalArgumentException("Port ID cannot be null");
    }
    this.portId = portId;
  }
}
