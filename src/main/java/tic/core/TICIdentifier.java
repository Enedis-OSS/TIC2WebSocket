// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.core;

import java.util.Objects;

/**
 * Class representing a core identifier with port ID, port name, and serial number.
 *
 * <p>This class provides mechanisms for constructing, accessing, and managing identifier
 * information including port IDs, port names, and serial numbers. It is designed for
 * general-purpose identifier handling.
 *
 * <p>Common use cases include:
 *
 * <ul>
 *   <li>Representing identifiers with structured data
 *   <li>Matching identifiers for streams and devices
 *   <li>Managing port and serial information
 * </ul>
 *
 * @author Enedis Smarties team
 */
public class TICIdentifier {
  private final String portId;
  private final String portName;
  private final String serialNumber;

  public static class Builder {
    private String portId;
    private String portName;
    private String serialNumber;

    /**
     * Sets the portId field.
     *
     * @param portId the port ID
     * @return the Builder instance
     */
    public Builder portId(String portId) {
      this.portId = portId;
      return this;
    }

    /**
     * Sets the portName field.
     *
     * @param portName the port name
     * @return the Builder instance
     */
    public Builder portName(String portName) {
      this.portName = portName;
      return this;
    }

    /**
     * Sets the serialNumber field.
     *
     * @param serialNumber the serial number
     * @return the Builder instance
     */
    public Builder serialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
      return this;
    }

    /**
     * Validates the builder's fields.
     *
     * @throws IllegalArgumentException if any required field is invalid
     */
    protected void validate() {
      if (this.portId == null && this.portName == null && this.serialNumber == null) {
        throw new IllegalArgumentException("Empty TICIdentifier not allowed!");
      }
    }

    public TICIdentifier build() {
      this.validate();
      return new TICIdentifier(this.portId, this.portName, this.serialNumber);
    }
  }

  private TICIdentifier(String portId, String portName, String serialNumber) {
    this.portId = portId;
    this.portName = portName;
    this.serialNumber = serialNumber;
  }

  public boolean matches(TICIdentifier identifier) {
    if (identifier != null) {
      if (this.getSerialNumber() != null && identifier.getSerialNumber() != null) {
        return this.getSerialNumber().equals(identifier.getSerialNumber());
      }
      if (this.getPortId() != null && identifier.getPortId() != null) {
        return this.getPortId().equals(identifier.getPortId());
      }
      if (this.getPortName() != null && identifier.getPortName() != null) {
        return this.getPortName().equals(identifier.getPortName());
      }
    }

    return false;
  }

  /**
   * Get port id
   *
   * @return the port id
   */
  public String getPortId() {
    return this.portId;
  }

  /**
   * Get port name
   *
   * @return the port name
   */
  public String getPortName() {
    return this.portName;
  }

  /**
   * Get serial number
   *
   * @return the serial number
   */
  public String getSerialNumber() {
    return this.serialNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TICIdentifier)) {
      return false;
    }
    TICIdentifier that = (TICIdentifier) o;
    return Objects.equals(this.portId, that.portId)
        && Objects.equals(this.portName, that.portName)
        && Objects.equals(this.serialNumber, that.serialNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.portId, this.portName, this.serialNumber);
  }

  @Override
  public String toString() {
    return "{portId="
        + this.portId
        + ", portName="
        + this.portName
        + ", serialNumber="
        + this.serialNumber
        + "}";
  }
}
