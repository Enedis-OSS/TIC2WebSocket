// Copyright (C) 2025 Enedis Smarties team <dt-dsi-nexus-lab-smarties@enedis.fr>
//
// SPDX-FileContributor: Jehan BOUSCH
// SPDX-FileContributor: Mathieu SABARTHES
//
// SPDX-License-Identifier: Apache-2.0

package tic.io.serialport;

import java.util.Objects;
import tic.util.ValueChecker;

/**
 * Descriptor class for serial port information and USB device properties.
 *
 * <p>This class provides a structured way to store and access serial port information, including
 * both native serial ports and USB-based serial devices, without relying on the legacy
 * DataDictionary infrastructure. It encapsulates all relevant properties needed to identify,
 * configure, and connect to serial ports.
 *
 * <p>The descriptor includes the following information:
 *
 * <ul>
 *   <li>Port unique identifier
 *   <li>Port name used to open the serial port
 *   <li>Port description
 *   <li>USB device product identifier (PID)
 *   <li>USB device vendor identifier (VID)
 *   <li>USB device product name
 *   <li>USB device manufacturer
 *   <li>USB device serial number
 * </ul>
 *
 * <p>The class supports distinguishing between native serial ports (e.g., COM ports) and USB serial
 * devices through the {@link #isNative()} method.
 *
 * @author Enedis Smarties team
 */
public class SerialPortDescriptor {

  private String portId;
  private String portName;
  private String description;
  private Short productId;
  private Short vendorId;
  private String productName;
  private String manufacturer;
  private String serialNumber;

  /** Builder class for constructing SerialPortDescriptor instances. */
  public static class Builder<T extends Builder<T>> {
    protected String portId;
    protected String portName;
    protected String description;
    protected Short productId;
    protected Short vendorId;
    protected String productName;
    protected String manufacturer;
    protected String serialNumber;

    @SuppressWarnings("unchecked")
    public T self() {
      return (T) this;
    }

    public T portId(String portId) {
      this.portId = portId;
      return self();
    }

    public T portName(String portName) {
      this.portName = portName;
      return self();
    }

    public T description(String description) {
      this.description = description;
      return self();
    }

    public T productId(Short productId) {
      this.productId = productId;
      return self();
    }

    public T vendorId(Short vendorId) {
      this.vendorId = vendorId;
      return self();
    }

    public T productName(String productName) {
      this.productName = productName;
      return self();
    }

    public T manufacturer(String manufacturer) {
      this.manufacturer = manufacturer;
      return self();
    }

    public T serialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
      return self();
    }

    /**
     * Validates the builder's fields.
     *
     * @throws IllegalArgumentException if any required field is invalid
     */
    protected void validate() {
      ValueChecker.validateString(this.portId, "portId", true, false);
      ValueChecker.validateString(this.portName, "portName", true, false);
      ValueChecker.validateString(this.description, "description", true, false);
      ValueChecker.validateNumber(this.productId, "productId", true);
      ValueChecker.validateNumber(this.vendorId, "vendorId", true);
      ValueChecker.validateString(this.productName, "productName", true, false);
      ValueChecker.validateString(this.manufacturer, "manufacturer", true, false);
      ValueChecker.validateString(this.serialNumber, "serialNumber", true, false);
    }

    /**
     * Builds the SerialPortDescriptor instance with the provided parameters.
     *
     * @return the constructed SerialPortDescriptor instance
     * @throws IllegalArgumentException if any required field is invalid
     */
    public SerialPortDescriptor build() {
      this.validate();

      return new SerialPortDescriptor(this);
    }
  }

  /**
   * Constructs a SerialPortDescriptor with all parameters explicitly set.
   *
   * <p>This constructor creates a descriptor with all serial port and USB device properties
   * specified.
   *
   * @param builder the builder instance containing all the parameters
   */
  protected SerialPortDescriptor(Builder<?> builder) {
    this.portId = builder.portId;
    this.portName = builder.portName;
    this.description = builder.description;
    this.productId = builder.productId;
    this.vendorId = builder.vendorId;
    this.productName = builder.productName;
    this.manufacturer = builder.manufacturer;
    this.serialNumber = builder.serialNumber;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SerialPortDescriptor)) {
      return false;
    }
    SerialPortDescriptor other = (SerialPortDescriptor) obj;
    return Objects.equals(this.portId, other.portId)
        && Objects.equals(this.portName, other.portName)
        && Objects.equals(this.description, other.description)
        && Objects.equals(this.productId, other.productId)
        && Objects.equals(this.vendorId, other.vendorId)
        && Objects.equals(this.productName, other.productName)
        && Objects.equals(this.manufacturer, other.manufacturer)
        && Objects.equals(this.serialNumber, other.serialNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.portId,
        this.portName,
        this.description,
        this.productId,
        this.vendorId,
        this.productName,
        this.manufacturer,
        this.serialNumber);
  }

  /**
   * Returns the port unique identifier.
   *
   * @return the port unique identifier
   */
  public String portId() {
    return this.portId;
  }

  /**
   * Returns the port name used to open the serial port.
   *
   * @return the port name used to open the serial port
   */
  public String portName() {
    return this.portName;
  }

  /**
   * Returns the port description.
   *
   * @return the port description
   */
  public String description() {
    return this.description;
  }

  /**
   * Returns the USB device product identifier (PID).
   *
   * @return the USB device product identifier in range [0-65535]
   */
  public Short productId() {
    return this.productId;
  }

  /**
   * Returns the USB device vendor identifier (VID).
   *
   * @return the USB device vendor identifier in range [0-65535]
   */
  public Short vendorId() {
    return this.vendorId;
  }

  /**
   * Returns the USB device product name.
   *
   * @return the USB device product name
   */
  public String productName() {
    return this.productName;
  }

  /**
   * Returns the USB device manufacturer.
   *
   * @return the USB device manufacturer
   */
  public String manufacturer() {
    return this.manufacturer;
  }

  /**
   * Returns the USB device serial number.
   *
   * @return the USB device serial number
   */
  public String serialNumber() {
    return this.serialNumber;
  }

  /**
   * Checks if this serial port is a native port (not USB).
   *
   * <p>A port is considered native if it has a port name but no port ID. This typically indicates a
   * built-in serial port (e.g., COM1) rather than a USB-to-serial adapter.
   *
   * @return true if this is a native serial port, false if it's a USB device
   */
  public boolean isNative() {
    return this.portName != null && this.portId == null;
  }
}
