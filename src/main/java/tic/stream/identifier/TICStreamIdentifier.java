// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.stream.identifier;

/** Class representing a TIC stream identifier, which can be either by name or by ID. */
public class TICStreamIdentifier {
  private SerialPortName portName;
  private SerialPortId portId;
  private TICStreamIdentifierType type;

  public TICStreamIdentifier(SerialPortName portName) {
    this.type = TICStreamIdentifierType.PORT_NAME;
    this.setPortName(portName);
  }

  public TICStreamIdentifier(SerialPortId portId) {
    this.type = TICStreamIdentifierType.PORT_ID;
    this.setPortId(portId);
  }

  public String getPortName() {
    return this.portName.getPortName();
  }

  public String getPortId() {
    return this.portId.getPortId();
  }

  public TICStreamIdentifierType getType() {
    return this.type;
  }

  private void setPortName(SerialPortName portName) {
    if (this.type == TICStreamIdentifierType.PORT_ID) {
      throw new IllegalStateException("Cannot set port name when identifier type is PORT_ID");
    }
    if (portName == null) {
      throw new IllegalArgumentException("Port name cannot be null");
    }
    this.portName = portName;
  }

  private void setPortId(SerialPortId portId) {
    if (this.type == TICStreamIdentifierType.PORT_NAME) {
      throw new IllegalStateException("Cannot set port ID when identifier type is PORT_NAME");
    }
    if (portId == null) {
      throw new IllegalArgumentException("Port ID cannot be null");
    }
    this.portId = portId;
  }
}
